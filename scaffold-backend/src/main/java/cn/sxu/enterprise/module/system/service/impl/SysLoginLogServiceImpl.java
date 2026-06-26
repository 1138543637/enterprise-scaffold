package cn.sxu.enterprise.module.system.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.system.entity.SysLoginLog;
import cn.sxu.enterprise.module.system.mapper.SysLoginLogMapper;
import cn.sxu.enterprise.module.system.service.SysLoginLogService;
import cn.sxu.enterprise.module.system.vo.SysLoginLogPageQuery;
import cn.sxu.enterprise.module.system.vo.SysLoginLogPageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {

    @Override
    public void recordLogin(String username, Integer status, String msg) {
        try {
            HttpServletRequest request = getCurrentRequest();

            SysLoginLog log = new SysLoginLog();
            log.setUsername(StringUtils.hasText(username) ? username : "unknown");
            log.setIpaddr(getClientIp(request));
            log.setLoginLocation(getLoginLocation(log.getIpaddr()));
            log.setBrowser(getBrowser(request));
            log.setOs(getOs(request));
            log.setStatus(status);
            log.setMsg(msg);
            log.setLoginTime(LocalDateTime.now());
            log.setCreateBy(log.getUsername());
            log.setCreateTime(LocalDateTime.now());
            log.setDeleted(0);

            this.save(log);
        } catch (Exception ignored) {
            // 日志记录失败不能影响登录主流程
        }
    }

    @Override
    public PageResult<SysLoginLogPageVO> pageLoginLogs(SysLoginLogPageQuery query) {
        long pageNo = query.getPageNo() == null || query.getPageNo() < 1 ? 1 : query.getPageNo();
        long pageSize = query.getPageSize() == null || query.getPageSize() < 1 ? 10 : query.getPageSize();
        pageSize = Math.min(pageSize, 100);

        LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getUsername()), SysLoginLog::getUsername, query.getUsername());
        wrapper.eq(query.getStatus() != null, SysLoginLog::getStatus, query.getStatus());
        wrapper.orderByDesc(SysLoginLog::getLoginTime);

        Page<SysLoginLog> page = this.page(new Page<>(pageNo, pageSize), wrapper);

        List<SysLoginLogPageVO> records = page.getRecords()
                .stream()
                .map(SysLoginLogPageVO::fromEntity)
                .toList();

        return PageResult.of(
                page.getCurrent(),
                page.getSize(),
                page.getTotal(),
                page.getPages(),
                records
        );
    }

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return null;
        }

        return attributes.getRequest();
    }

    private String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }

        String ip = request.getHeader("X-Forwarded-For");

        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();
        }

        ip = request.getHeader("Proxy-Client-IP");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        return request.getRemoteAddr();
    }

    private String getLoginLocation(String ip) {
        if (!StringUtils.hasText(ip)) {
            return "未知";
        }

        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "localhost".equalsIgnoreCase(ip)) {
            return "本地开发环境";
        }

        return "未知";
    }

    private String getBrowser(HttpServletRequest request) {
        String userAgent = getUserAgent(request);

        if (userAgent.contains("Edg")) {
            return "Edge";
        }
        if (userAgent.contains("Chrome")) {
            return "Chrome";
        }
        if (userAgent.contains("Firefox")) {
            return "Firefox";
        }
        if (userAgent.contains("Safari")) {
            return "Safari";
        }

        return "Other";
    }

    private String getOs(HttpServletRequest request) {
        String userAgent = getUserAgent(request);

        if (userAgent.contains("Windows")) {
            return "Windows";
        }
        if (userAgent.contains("Mac OS")) {
            return "Mac OS";
        }
        if (userAgent.contains("Linux")) {
            return "Linux";
        }
        if (userAgent.contains("Android")) {
            return "Android";
        }
        if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
            return "iOS";
        }

        return "Other";
    }

    private String getUserAgent(HttpServletRequest request) {
        if (request == null) {
            return "";
        }

        String userAgent = request.getHeader("User-Agent");
        return userAgent == null ? "" : userAgent;
    }
}