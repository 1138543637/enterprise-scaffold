package cn.sxu.enterprise.module.mine.service.impl;

import cn.sxu.enterprise.common.core.exception.BusinessException;
import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.security.model.LoginUser;
import cn.sxu.enterprise.module.mine.dto.MineWorkOrderCloseRequest;
import cn.sxu.enterprise.module.mine.dto.MineWorkOrderCreateRequest;
import cn.sxu.enterprise.module.mine.dto.MineWorkOrderHandleRequest;
import cn.sxu.enterprise.module.mine.entity.MineAlarmEvent;
import cn.sxu.enterprise.module.mine.entity.MineWorkOrder;
import cn.sxu.enterprise.module.mine.mapper.MineAlarmEventMapper;
import cn.sxu.enterprise.module.mine.mapper.MineWorkOrderMapper;
import cn.sxu.enterprise.module.mine.service.MineWorkOrderService;
import cn.sxu.enterprise.module.mine.vo.MineWorkOrderPageQuery;
import cn.sxu.enterprise.module.mine.vo.MineWorkOrderPageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MineWorkOrderServiceImpl implements MineWorkOrderService {

    private static final DateTimeFormatter CODE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final MineWorkOrderMapper mineWorkOrderMapper;

    private final MineAlarmEventMapper mineAlarmEventMapper;

    public MineWorkOrderServiceImpl(MineWorkOrderMapper mineWorkOrderMapper,
                                    MineAlarmEventMapper mineAlarmEventMapper) {
        this.mineWorkOrderMapper = mineWorkOrderMapper;
        this.mineAlarmEventMapper = mineAlarmEventMapper;
    }

    @Override
    public PageResult<MineWorkOrderPageVO> pageWorkOrders(MineWorkOrderPageQuery query) {
        if (query == null) {
            query = new MineWorkOrderPageQuery();
        }

        Long pageNo = query.getPageNo() == null || query.getPageNo() < 1 ? 1L : query.getPageNo();
        Long pageSize = query.getPageSize() == null || query.getPageSize() < 1 ? 10L : query.getPageSize();

        Page<MineWorkOrder> page = new Page<>(pageNo, pageSize);

        LambdaQueryWrapper<MineWorkOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getWorkOrderCode()), MineWorkOrder::getWorkOrderCode, query.getWorkOrderCode())
                .like(StringUtils.hasText(query.getEventCode()), MineWorkOrder::getEventCode, query.getEventCode())
                .eq(query.getAlarmLevel() != null, MineWorkOrder::getAlarmLevel, query.getAlarmLevel())
                .like(StringUtils.hasText(query.getSensorCode()), MineWorkOrder::getSensorCode, query.getSensorCode())
                .like(StringUtils.hasText(query.getSensorName()), MineWorkOrder::getSensorName, query.getSensorName())
                .eq(StringUtils.hasText(query.getSensorType()), MineWorkOrder::getSensorType, query.getSensorType())
                .like(StringUtils.hasText(query.getAreaName()), MineWorkOrder::getAreaName, query.getAreaName())
                .eq(query.getOrderStatus() != null, MineWorkOrder::getOrderStatus, query.getOrderStatus())
                .eq(query.getStatus() != null, MineWorkOrder::getStatus, query.getStatus())
                .orderByDesc(MineWorkOrder::getCreateTime)
                .orderByDesc(MineWorkOrder::getId);

        Page<MineWorkOrder> result = mineWorkOrderMapper.selectPage(page, wrapper);

        List<MineWorkOrderPageVO> records = result.getRecords()
                .stream()
                .map(MineWorkOrderPageVO::fromEntity)
                .toList();

        return PageResult.of(
                result.getCurrent(),
                result.getSize(),
                result.getTotal(),
                result.getPages(),
                records
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MineWorkOrderPageVO createFromAlarm(MineWorkOrderCreateRequest request) {
        if (request == null || request.getAlarmEventId() == null) {
            throw new BusinessException(500, "告警事件ID不能为空");
        }

        MineAlarmEvent alarmEvent = mineAlarmEventMapper.selectById(request.getAlarmEventId());
        if (alarmEvent == null) {
            throw new BusinessException(500, "告警事件不存在");
        }

        if (Objects.equals(alarmEvent.getStatus(), 1)) {
            throw new BusinessException(500, "告警事件已失效，不能转工单");
        }

        LambdaQueryWrapper<MineWorkOrder> existsWrapper = new LambdaQueryWrapper<>();
        existsWrapper.eq(MineWorkOrder::getAlarmEventId, request.getAlarmEventId());
        MineWorkOrder exists = mineWorkOrderMapper.selectOne(existsWrapper);
        if (exists != null) {
            return MineWorkOrderPageVO.fromEntity(exists);
        }

        if (Objects.equals(alarmEvent.getHandleStatus(), 2)) {
            throw new BusinessException(500, "告警事件已关闭，不能转工单");
        }

        LoginUser loginUser = getCurrentLoginUser();
        String username = getUsername(loginUser);
        LocalDateTime now = LocalDateTime.now();

        MineWorkOrder entity = new MineWorkOrder();
        entity.setWorkOrderCode(generateWorkOrderCode());
        entity.setAlarmEventId(alarmEvent.getId());
        entity.setEventCode(alarmEvent.getEventCode());
        entity.setAlarmLevel(alarmEvent.getAlarmLevel());
        entity.setWorkOrderTitle(StringUtils.hasText(alarmEvent.getAlarmTitle()) ? alarmEvent.getAlarmTitle() : "智能矿山告警处置工单");
        entity.setWorkOrderContent(alarmEvent.getAlarmContent());
        entity.setDeviceId(alarmEvent.getDeviceId());
        entity.setSensorId(alarmEvent.getSensorId());
        entity.setSensorCode(alarmEvent.getSensorCode());
        entity.setSensorName(alarmEvent.getSensorName());
        entity.setSensorType(alarmEvent.getSensorType());
        entity.setAreaName(alarmEvent.getAreaName());
        entity.setLocation(alarmEvent.getLocation());
        entity.setOrderStatus(0);
        entity.setStatus(0);
        entity.setCreateBy(username);
        entity.setCreateTime(now);
        entity.setDeleted(0);
        entity.setRemark(request.getRemark());

        mineWorkOrderMapper.insert(entity);

        alarmEvent.setHandleStatus(1);
        alarmEvent.setUpdateBy(username);
        alarmEvent.setUpdateTime(now);
        mineAlarmEventMapper.updateById(alarmEvent);

        return MineWorkOrderPageVO.fromEntity(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MineWorkOrderPageVO handle(Long id, MineWorkOrderHandleRequest request) {
        if (id == null) {
            throw new BusinessException(500, "工单ID不能为空");
        }
        if (request == null || !StringUtils.hasText(request.getHandleResult())) {
            throw new BusinessException(500, "处理结果不能为空");
        }

        MineWorkOrder entity = mineWorkOrderMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(500, "工单不存在");
        }

        if (Objects.equals(entity.getStatus(), 1)) {
            throw new BusinessException(500, "工单已失效，不能处理");
        }

        if (Objects.equals(entity.getOrderStatus(), 3)) {
            throw new BusinessException(500, "工单已关闭，不能再次处理");
        }

        LoginUser loginUser = getCurrentLoginUser();
        String username = getUsername(loginUser);
        LocalDateTime now = LocalDateTime.now();

        entity.setOrderStatus(2);
        entity.setHandlerUserId(loginUser == null ? null : loginUser.getUserId());
        entity.setHandlerUsername(username);
        entity.setHandleTime(now);
        entity.setHandleResult(request.getHandleResult());
        entity.setUpdateBy(username);
        entity.setUpdateTime(now);

        if (StringUtils.hasText(request.getRemark())) {
            entity.setRemark(request.getRemark());
        }

        mineWorkOrderMapper.updateById(entity);

        updateAlarmEventHandleStatus(entity.getAlarmEventId(), 1, username);

        return MineWorkOrderPageVO.fromEntity(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MineWorkOrderPageVO close(Long id, MineWorkOrderCloseRequest request) {
        if (id == null) {
            throw new BusinessException(500, "工单ID不能为空");
        }
        if (request == null || !StringUtils.hasText(request.getCloseResult())) {
            throw new BusinessException(500, "关闭结果不能为空");
        }

        MineWorkOrder entity = mineWorkOrderMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(500, "工单不存在");
        }

        if (Objects.equals(entity.getStatus(), 1)) {
            throw new BusinessException(500, "工单已失效，不能关闭");
        }

        if (Objects.equals(entity.getOrderStatus(), 3)) {
            throw new BusinessException(500, "工单已关闭，不能重复关闭");
        }

        if (!Objects.equals(entity.getOrderStatus(), 2)) {
            throw new BusinessException(500, "工单未处理，不能关闭");
        }

        LoginUser loginUser = getCurrentLoginUser();
        String username = getUsername(loginUser);
        LocalDateTime now = LocalDateTime.now();

        entity.setOrderStatus(3);
        entity.setCloseUserId(loginUser == null ? null : loginUser.getUserId());
        entity.setCloseUsername(username);
        entity.setCloseTime(now);
        entity.setCloseResult(request.getCloseResult());
        entity.setUpdateBy(username);
        entity.setUpdateTime(now);

        if (StringUtils.hasText(request.getRemark())) {
            entity.setRemark(request.getRemark());
        }

        mineWorkOrderMapper.updateById(entity);

        updateAlarmEventHandleStatus(entity.getAlarmEventId(), 2, username);

        return MineWorkOrderPageVO.fromEntity(entity);
    }

    private void updateAlarmEventHandleStatus(Long alarmEventId, Integer handleStatus, String username) {
        if (alarmEventId == null) {
            return;
        }

        MineAlarmEvent alarmEvent = mineAlarmEventMapper.selectById(alarmEventId);
        if (alarmEvent == null) {
            return;
        }

        alarmEvent.setHandleStatus(handleStatus);
        alarmEvent.setUpdateBy(username);
        alarmEvent.setUpdateTime(LocalDateTime.now());
        mineAlarmEventMapper.updateById(alarmEvent);
    }

    private String generateWorkOrderCode() {
        int random = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return "WO-" + LocalDateTime.now().format(CODE_TIME_FORMATTER) + "-" + random;
    }

    private LoginUser getCurrentLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof LoginUser loginUser) {
            return loginUser;
        }

        return null;
    }

    private String getUsername(LoginUser loginUser) {
        if (loginUser == null || !StringUtils.hasText(loginUser.getUsername())) {
            return "system";
        }
        return loginUser.getUsername();
    }
}
