package cn.sxu.enterprise.module.system.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.system.entity.SysDictData;
import cn.sxu.enterprise.module.system.mapper.SysDictDataMapper;
import cn.sxu.enterprise.module.system.service.SysDictDataService;
import cn.sxu.enterprise.module.system.vo.SysDictDataPageQuery;
import cn.sxu.enterprise.module.system.vo.SysDictDataPageVO;
import cn.sxu.enterprise.module.system.vo.SysDictDataVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SysDictDataServiceImpl implements SysDictDataService {

    private final SysDictDataMapper sysDictDataMapper;

    public SysDictDataServiceImpl(SysDictDataMapper sysDictDataMapper) {
        this.sysDictDataMapper = sysDictDataMapper;
    }

    @Override
    public PageResult<SysDictDataPageVO> pageDictData(SysDictDataPageQuery query) {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(StringUtils.hasText(query.getDictType()), SysDictData::getDictType, query.getDictType())
                .like(StringUtils.hasText(query.getDictLabel()), SysDictData::getDictLabel, query.getDictLabel())
                .eq(query.getStatus() != null, SysDictData::getStatus, query.getStatus())
                .orderByAsc(SysDictData::getDictSort)
                .orderByDesc(SysDictData::getId);

        Page<SysDictData> page = sysDictDataMapper.selectPage(
                new Page<>(query.getPageNo(), query.getPageSize()),
                wrapper
        );

        List<SysDictDataPageVO> records = page.getRecords()
                .stream()
                .map(SysDictDataPageVO::fromEntity)
                .toList();

        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages(), records);
    }

    @Override
    public List<SysDictDataVO> listByDictType(String dictType) {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getStatus, 0)
                .orderByAsc(SysDictData::getDictSort)
                .orderByAsc(SysDictData::getId);

        return sysDictDataMapper.selectList(wrapper)
                .stream()
                .map(SysDictDataVO::fromEntity)
                .toList();
    }
}
