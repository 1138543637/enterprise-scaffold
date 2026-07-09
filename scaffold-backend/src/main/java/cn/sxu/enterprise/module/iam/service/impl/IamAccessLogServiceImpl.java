package cn.sxu.enterprise.module.iam.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.iam.entity.IamAccessLog;
import cn.sxu.enterprise.module.iam.mapper.IamAccessLogMapper;
import cn.sxu.enterprise.module.iam.service.IamAccessLogService;
import cn.sxu.enterprise.module.iam.vo.IamAccessLogPageQuery;
import cn.sxu.enterprise.module.iam.vo.IamAccessLogPageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * IAM 接口访问日志 Service 实现。
 */
@Service
public class IamAccessLogServiceImpl implements IamAccessLogService {

    @Resource
    private IamAccessLogMapper iamAccessLogMapper;

    @Override
    public PageResult<IamAccessLogPageVO> pageAccessLogs(IamAccessLogPageQuery query) {
        Long pageNo = normalizePageNo(query.getPageNo());
        Long pageSize = normalizePageSize(query.getPageSize());

        Page<IamAccessLog> page = new Page<>(pageNo, pageSize);

        LambdaQueryWrapper<IamAccessLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getRequestUri()), IamAccessLog::getRequestUri, query.getRequestUri());
        wrapper.eq(StringUtils.hasText(query.getRequestMethod()), IamAccessLog::getRequestMethod, query.getRequestMethod());
        wrapper.like(StringUtils.hasText(query.getUsername()), IamAccessLog::getUsername, query.getUsername());
        wrapper.like(StringUtils.hasText(query.getClientIp()), IamAccessLog::getClientIp, query.getClientIp());
        wrapper.eq(query.getAccessStatus() != null, IamAccessLog::getAccessStatus, query.getAccessStatus());
        wrapper.ge(query.getBeginTime() != null, IamAccessLog::getAccessTime, query.getBeginTime());
        wrapper.le(query.getEndTime() != null, IamAccessLog::getAccessTime, query.getEndTime());
        wrapper.orderByDesc(IamAccessLog::getAccessTime);
        wrapper.orderByDesc(IamAccessLog::getId);

        Page<IamAccessLog> resultPage = iamAccessLogMapper.selectPage(page, wrapper);

        List<IamAccessLogPageVO> records = resultPage.getRecords()
                .stream()
                .map(IamAccessLogPageVO::fromEntity)
                .collect(Collectors.toList());

        PageResult<IamAccessLogPageVO> pageResult = new PageResult<>();
        pageResult.setRecords(records);
        pageResult.setTotal(resultPage.getTotal());
        pageResult.setPageNo(pageNo);
        pageResult.setPageSize(pageSize);
        return pageResult;
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
