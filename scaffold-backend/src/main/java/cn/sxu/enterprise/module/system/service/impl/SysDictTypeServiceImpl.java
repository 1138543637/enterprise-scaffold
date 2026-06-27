package cn.sxu.enterprise.module.system.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.system.entity.SysDictType;
import cn.sxu.enterprise.module.system.mapper.SysDictTypeMapper;
import cn.sxu.enterprise.module.system.service.SysDictTypeService;
import cn.sxu.enterprise.module.system.vo.SysDictTypePageQuery;
import cn.sxu.enterprise.module.system.vo.SysDictTypePageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SysDictTypeServiceImpl implements SysDictTypeService {

    private final SysDictTypeMapper sysDictTypeMapper;

    public SysDictTypeServiceImpl(SysDictTypeMapper sysDictTypeMapper) {
        this.sysDictTypeMapper = sysDictTypeMapper;
    }

    @Override
    public PageResult<SysDictTypePageVO> pageDictTypes(SysDictTypePageQuery query) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.hasText(query.getDictName()), SysDictType::getDictName, query.getDictName())
                .like(StringUtils.hasText(query.getDictType()), SysDictType::getDictType, query.getDictType())
                .eq(query.getStatus() != null, SysDictType::getStatus, query.getStatus())
                .orderByDesc(SysDictType::getCreateTime)
                .orderByDesc(SysDictType::getId);

        Page<SysDictType> page = sysDictTypeMapper.selectPage(
                new Page<>(query.getPageNo(), query.getPageSize()),
                wrapper
        );

        List<SysDictTypePageVO> records = page.getRecords()
                .stream()
                .map(SysDictTypePageVO::fromEntity)
                .toList();

        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), records);
    }
}
