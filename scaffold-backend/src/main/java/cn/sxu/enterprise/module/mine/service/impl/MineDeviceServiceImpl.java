package cn.sxu.enterprise.module.mine.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.mine.entity.MineDevice;
import cn.sxu.enterprise.module.mine.mapper.MineDeviceMapper;
import cn.sxu.enterprise.module.mine.service.MineDeviceService;
import cn.sxu.enterprise.module.mine.vo.MineDevicePageQuery;
import cn.sxu.enterprise.module.mine.vo.MineDevicePageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class MineDeviceServiceImpl implements MineDeviceService {

    private final MineDeviceMapper mineDeviceMapper;

    public MineDeviceServiceImpl(MineDeviceMapper mineDeviceMapper) {
        this.mineDeviceMapper = mineDeviceMapper;
    }

    @Override
    public PageResult<MineDevicePageVO> pageDevices(MineDevicePageQuery query) {
        Page<MineDevice> page = new Page<>(query.getPageNo(), query.getPageSize());

        LambdaQueryWrapper<MineDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getDeviceCode()), MineDevice::getDeviceCode, query.getDeviceCode());
        wrapper.like(StringUtils.hasText(query.getDeviceName()), MineDevice::getDeviceName, query.getDeviceName());
        wrapper.eq(StringUtils.hasText(query.getDeviceType()), MineDevice::getDeviceType, query.getDeviceType());
        wrapper.like(StringUtils.hasText(query.getAreaName()), MineDevice::getAreaName, query.getAreaName());
        wrapper.eq(query.getStatus() != null, MineDevice::getStatus, query.getStatus());
        wrapper.orderByDesc(MineDevice::getId);

        Page<MineDevice> resultPage = mineDeviceMapper.selectPage(page, wrapper);

        List<MineDevicePageVO> records = resultPage.getRecords()
                .stream()
                .map(MineDevicePageVO::fromEntity)
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