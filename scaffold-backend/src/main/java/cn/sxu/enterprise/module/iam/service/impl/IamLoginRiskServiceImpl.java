package cn.sxu.enterprise.module.iam.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.iam.entity.IamLoginRisk;
import cn.sxu.enterprise.module.iam.mapper.IamLoginRiskMapper;
import cn.sxu.enterprise.module.iam.service.IamLoginRiskService;
import cn.sxu.enterprise.module.iam.vo.IamLoginRiskPageQuery;
import cn.sxu.enterprise.module.iam.vo.IamLoginRiskPageVO;
import cn.sxu.enterprise.module.system.entity.SysLoginLog;
import cn.sxu.enterprise.module.system.mapper.SysLoginLogMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * IAM 异常登录风险 Service 实现。
 */
@Service
public class IamLoginRiskServiceImpl implements IamLoginRiskService {

    private static final String RISK_TYPE_LOGIN_FAILED = "LOGIN_FAILED";

    private static final String RISK_TYPE_SHORT_TIME_FAILED = "SHORT_TIME_FAILED";

    private static final String RISK_TYPE_ABNORMAL_IP = "ABNORMAL_IP";

    private static final DateTimeFormatter RISK_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Resource
    private IamLoginRiskMapper iamLoginRiskMapper;

    @Resource
    private SysLoginLogMapper sysLoginLogMapper;

    @Override
    public PageResult<IamLoginRiskPageVO> pageLoginRisks(IamLoginRiskPageQuery query) {
        Long pageNo = normalizePageNo(query.getPageNo());
        Long pageSize = normalizePageSize(query.getPageSize());

        Page<IamLoginRisk> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<IamLoginRisk> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getRiskCode()), IamLoginRisk::getRiskCode, query.getRiskCode());
        wrapper.like(StringUtils.hasText(query.getUsername()), IamLoginRisk::getUsername, query.getUsername());
        wrapper.like(StringUtils.hasText(query.getClientIp()), IamLoginRisk::getClientIp, query.getClientIp());
        wrapper.eq(StringUtils.hasText(query.getRiskType()), IamLoginRisk::getRiskType, query.getRiskType());
        wrapper.eq(query.getRiskLevel() != null, IamLoginRisk::getRiskLevel, query.getRiskLevel());
        wrapper.eq(query.getHandleStatus() != null, IamLoginRisk::getHandleStatus, query.getHandleStatus());
        wrapper.ge(query.getBeginTime() != null, IamLoginRisk::getDetectTime, query.getBeginTime());
        wrapper.le(query.getEndTime() != null, IamLoginRisk::getDetectTime, query.getEndTime());
        wrapper.orderByDesc(IamLoginRisk::getDetectTime);
        wrapper.orderByDesc(IamLoginRisk::getId);

        Page<IamLoginRisk> resultPage = iamLoginRiskMapper.selectPage(page, wrapper);
        List<IamLoginRiskPageVO> records = resultPage.getRecords()
                .stream()
                .map(IamLoginRiskPageVO::fromEntity)
                .collect(Collectors.toList());

        PageResult<IamLoginRiskPageVO> pageResult = new PageResult<>();
        pageResult.setRecords(records);
        pageResult.setTotal(resultPage.getTotal());
        pageResult.setPageNo(pageNo);
        pageResult.setPageSize(pageSize);
        pageResult.setPages(resultPage.getPages());
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<IamLoginRiskPageVO> detectLoginRisks() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayAgo = now.minusDays(1);
        LocalDateTime tenMinutesAgo = now.minusMinutes(10);

        List<SysLoginLog> failedLogs = sysLoginLogMapper.selectList(
                new LambdaQueryWrapper<SysLoginLog>()
                        .eq(SysLoginLog::getStatus, 1)
                        .ge(SysLoginLog::getLoginTime, oneDayAgo)
                        .orderByDesc(SysLoginLog::getLoginTime)
                        .last("LIMIT 500")
        );

        List<IamLoginRisk> generatedRisks = new ArrayList<>();
        generatedRisks.addAll(buildUsernameIpRisks(failedLogs, now, tenMinutesAgo));
        generatedRisks.addAll(buildIpRisks(failedLogs, now));

        List<IamLoginRiskPageVO> result = new ArrayList<>();
        for (IamLoginRisk risk : generatedRisks) {
            if (!existsByRiskCode(risk.getRiskCode())) {
                iamLoginRiskMapper.insert(risk);
                result.add(IamLoginRiskPageVO.fromEntity(risk));
            }
        }
        return result;
    }

    private List<IamLoginRisk> buildUsernameIpRisks(List<SysLoginLog> failedLogs,
                                                     LocalDateTime now,
                                                     LocalDateTime tenMinutesAgo) {
        Map<String, List<SysLoginLog>> groupMap = failedLogs.stream()
                .collect(Collectors.groupingBy(
                        log -> safeText(log.getUsername()) + "@@" + safeText(log.getIpaddr()),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<IamLoginRisk> risks = new ArrayList<>();
        for (List<SysLoginLog> logs : groupMap.values()) {
            if (logs.isEmpty()) {
                continue;
            }

            String username = safeText(logs.get(0).getUsername());
            String clientIp = safeText(logs.get(0).getIpaddr());
            int failCount = logs.size();
            LocalDateTime firstFailTime = minLoginTime(logs);
            LocalDateTime lastFailTime = maxLoginTime(logs);

            if (failCount >= 1) {
                risks.add(buildRisk(
                        username,
                        clientIp,
                        RISK_TYPE_LOGIN_FAILED,
                        resolveLevelByFailCount(failCount),
                        failCount,
                        firstFailTime,
                        lastFailTime,
                        now,
                        "最近24小时登录失败 " + failCount + " 次"
                ));
            }

            long recentFailCount = logs.stream()
                    .filter(log -> log.getLoginTime() != null && !log.getLoginTime().isBefore(tenMinutesAgo))
                    .count();

            if (recentFailCount >= 3) {
                risks.add(buildRisk(
                        username,
                        clientIp,
                        RISK_TYPE_SHORT_TIME_FAILED,
                        3,
                        Math.toIntExact(recentFailCount),
                        firstFailTime,
                        lastFailTime,
                        now,
                        "最近10分钟内登录失败 " + recentFailCount + " 次，判定为高风险"
                ));
            }
        }
        return risks;
    }

    private List<IamLoginRisk> buildIpRisks(List<SysLoginLog> failedLogs, LocalDateTime now) {
        Map<String, List<SysLoginLog>> ipGroupMap = failedLogs.stream()
                .collect(Collectors.groupingBy(
                        log -> safeText(log.getIpaddr()),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<IamLoginRisk> risks = new ArrayList<>();
        for (Map.Entry<String, List<SysLoginLog>> entry : ipGroupMap.entrySet()) {
            List<SysLoginLog> logs = entry.getValue();
            if (logs.size() < 5) {
                continue;
            }

            String clientIp = entry.getKey();
            LocalDateTime firstFailTime = minLoginTime(logs);
            LocalDateTime lastFailTime = maxLoginTime(logs);

            risks.add(buildRisk(
                    "*",
                    clientIp,
                    RISK_TYPE_ABNORMAL_IP,
                    2,
                    logs.size(),
                    firstFailTime,
                    lastFailTime,
                    now,
                    "最近24小时该 IP 登录失败 " + logs.size() + " 次，建议重点关注"
            ));
        }
        return risks;
    }

    private IamLoginRisk buildRisk(String username,
                                   String clientIp,
                                   String riskType,
                                   Integer riskLevel,
                                   Integer failCount,
                                   LocalDateTime firstFailTime,
                                   LocalDateTime lastFailTime,
                                   LocalDateTime detectTime,
                                   String remark) {
        IamLoginRisk risk = new IamLoginRisk();
        risk.setRiskCode(buildRiskCode(username, clientIp, riskType, firstFailTime, lastFailTime));
        risk.setUsername(username);
        risk.setClientIp(clientIp);
        risk.setRiskType(riskType);
        risk.setRiskLevel(riskLevel);
        risk.setFailCount(failCount);
        risk.setFirstFailTime(firstFailTime);
        risk.setLastFailTime(lastFailTime);
        risk.setDetectTime(detectTime);
        risk.setHandleStatus(0);
        risk.setStatus(0);
        risk.setCreateBy("system");
        risk.setCreateTime(detectTime);
        risk.setDeleted(0);
        risk.setRemark(remark);
        return risk;
    }

    private boolean existsByRiskCode(String riskCode) {
        Long count = iamLoginRiskMapper.selectCount(
                new LambdaQueryWrapper<IamLoginRisk>()
                        .eq(IamLoginRisk::getRiskCode, riskCode)
        );
        return count != null && count > 0;
    }

    private String buildRiskCode(String username,
                                 String clientIp,
                                 String riskType,
                                 LocalDateTime firstFailTime,
                                 LocalDateTime lastFailTime) {
        String first = firstFailTime == null ? "NA" : firstFailTime.format(RISK_TIME_FORMATTER);
        String last = lastFailTime == null ? "NA" : lastFailTime.format(RISK_TIME_FORMATTER);
        String source = safeText(username) + "|" + safeText(clientIp) + "|" + riskType + "|" + first + "|" + last;
        return "LR-" + last + "-" + Integer.toUnsignedString(source.hashCode());
    }

    private Integer resolveLevelByFailCount(int failCount) {
        if (failCount >= 5) {
            return 3;
        }
        if (failCount >= 3) {
            return 2;
        }
        return 1;
    }

    private LocalDateTime minLoginTime(List<SysLoginLog> logs) {
        return logs.stream()
                .map(SysLoginLog::getLoginTime)
                .filter(time -> time != null)
                .min(Comparator.naturalOrder())
                .orElse(null);
    }

    private LocalDateTime maxLoginTime(List<SysLoginLog> logs) {
        return logs.stream()
                .map(SysLoginLog::getLoginTime)
                .filter(time -> time != null)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    private String safeText(String value) {
        if (!StringUtils.hasText(value)) {
            return "unknown";
        }
        return value.trim();
    }

    private Long normalizePageNo(Long pageNo) {
        if (pageNo == null || pageNo < 1) {
            return 1L;
        }
        return pageNo;
    }

    private Long normalizePageSize(Long pageSize) {
        if (pageSize == null || pageSize < 1) {
            return 10L;
        }
        if (pageSize > 100) {
            return 100L;
        }
        return pageSize;
    }
}
