package cn.sxu.enterprise.module.system.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.system.entity.SysOperLog;
import cn.sxu.enterprise.module.system.mapper.SysOperLogMapper;
import cn.sxu.enterprise.module.system.service.SysOperLogService;
import cn.sxu.enterprise.module.system.vo.SysOperLogPageQuery;
import cn.sxu.enterprise.module.system.vo.SysOperLogPageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService {

    @Override
    public PageResult<SysOperLogPageVO> pageOperLogs(SysOperLogPageQuery query) {
        long pageNo = query.getPageNo() == null || query.getPageNo() < 1 ? 1 : query.getPageNo();
        long pageSize = query.getPageSize() == null || query.getPageSize() < 1 ? 10 : query.getPageSize();
        pageSize = Math.min(pageSize, 100);

        LambdaQueryWrapper<SysOperLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getTitle()), SysOperLog::getTitle, query.getTitle());
        wrapper.like(StringUtils.hasText(query.getBusinessType()), SysOperLog::getBusinessType, query.getBusinessType());
        wrapper.like(StringUtils.hasText(query.getOperName()), SysOperLog::getOperName, query.getOperName());
        wrapper.eq(query.getStatus() != null, SysOperLog::getStatus, query.getStatus());
        wrapper.orderByDesc(SysOperLog::getOperTime);

        Page<SysOperLog> page = this.page(new Page<>(pageNo, pageSize), wrapper);

        List<SysOperLogPageVO> records = page.getRecords()
                .stream()
                .map(SysOperLogPageVO::fromEntity)
                .toList();

        return PageResult.of(
                page.getCurrent(),
                page.getSize(),
                page.getTotal(),
                page.getPages(),
                records
        );
    }
}