package cn.sxu.enterprise.module.mine.service.impl;

import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.mine.entity.MineSensor;
import cn.sxu.enterprise.module.mine.mapper.MineSensorMapper;
import cn.sxu.enterprise.module.mine.service.MineSensorService;
import cn.sxu.enterprise.module.mine.vo.MineSensorPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineSensorPageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class MineSensorServiceImpl implements MineSensorService {

    private final MineSensorMapper mineSensorMapper;

    public MineSensorServiceImpl(MineSensorMapper mineSensorMapper) {
        this.mineSensorMapper = mineSensorMapper;
    }

    @Override
    public PageResult<MineSensorPageVO> pageSensors(MineSensorPageQuery query) {
        Page<MineSensor> page = new Page<>(query.getPageNo(), query.getPageSize());

        LambdaQueryWrapper<MineSensor> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getSensorCode()), MineSensor::getSensorCode, query.getSensorCode());
        wrapper.like(StringUtils.hasText(query.getSensorName()), MineSensor::getSensorName, query.getSensorName());
        wrapper.eq(StringUtils.hasText(query.getSensorType()), MineSensor::getSensorType, query.getSensorType());
        wrapper.eq(query.getDeviceId() != null, MineSensor::getDeviceId, query.getDeviceId());
        wrapper.like(StringUtils.hasText(query.getAreaName()), MineSensor::getAreaName, query.getAreaName());
        wrapper.eq(query.getStatus() != null, MineSensor::getStatus, query.getStatus());
        wrapper.orderByDesc(MineSensor::getId);

        Page<MineSensor> resultPage = mineSensorMapper.selectPage(page, wrapper);

        List<MineSensorPageVO> records = resultPage.getRecords()
                .stream()
                .map(MineSensorPageVO::fromEntity)
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