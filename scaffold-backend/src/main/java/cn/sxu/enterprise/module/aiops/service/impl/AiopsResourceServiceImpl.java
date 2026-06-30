package cn.sxu.enterprise.module.aiops.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.aiops.entity.AiopsResource;
import cn.sxu.enterprise.module.aiops.mapper.AiopsResourceMapper;
import cn.sxu.enterprise.module.aiops.service.AiopsResourceService;
import cn.sxu.enterprise.module.aiops.vo.AiopsResourcePageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsResourcePageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AiopsResourceServiceImpl implements AiopsResourceService {

    private final AiopsResourceMapper aiopsResourceMapper;

    public AiopsResourceServiceImpl(AiopsResourceMapper aiopsResourceMapper) {
        this.aiopsResourceMapper = aiopsResourceMapper;
    }

    @Override
    public PageResult<AiopsResourcePageVO> pageResources(AiopsResourcePageQuery query) {
        if (query == null) {
            query = new AiopsResourcePageQuery();
        }

        Page<AiopsResource> page = new Page<>(query.getPageNo(), query.getPageSize());

        LambdaQueryWrapper<AiopsResource> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getResourceCode()), AiopsResource::getResourceCode, query.getResourceCode())
                .like(StringUtils.hasText(query.getResourceName()), AiopsResource::getResourceName, query.getResourceName())
                .eq(StringUtils.hasText(query.getResourceType()), AiopsResource::getResourceType, query.getResourceType())
                .like(StringUtils.hasText(query.getIpAddr()), AiopsResource::getIpAddr, query.getIpAddr())
                .eq(StringUtils.hasText(query.getEnvType()), AiopsResource::getEnvType, query.getEnvType())
                .like(StringUtils.hasText(query.getSystemName()), AiopsResource::getSystemName, query.getSystemName())
                .like(StringUtils.hasText(query.getOwnerName()), AiopsResource::getOwnerName, query.getOwnerName())
                .eq(query.getCollectEnabled() != null, AiopsResource::getCollectEnabled, query.getCollectEnabled())
                .eq(query.getStatus() != null, AiopsResource::getStatus, query.getStatus())
                .orderByDesc(AiopsResource::getCreateTime)
                .orderByDesc(AiopsResource::getId);

        Page<AiopsResource> resultPage = aiopsResourceMapper.selectPage(page, wrapper);

        List<AiopsResourcePageVO> records = resultPage.getRecords()
                .stream()
                .map(AiopsResourcePageVO::fromEntity)
                .toList();

        return PageResult.of(
                resultPage.getCurrent(),
                resultPage.getSize(),
                resultPage.getTotal(),
                resultPage.getPages(),
                records
        );
    }
}
