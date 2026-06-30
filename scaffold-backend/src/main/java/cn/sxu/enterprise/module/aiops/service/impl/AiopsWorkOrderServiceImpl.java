package cn.sxu.enterprise.module.aiops.service.impl;

import cn.sxu.enterprise.common.core.exception.BusinessException;
import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.common.security.model.LoginUser;
import cn.sxu.enterprise.module.aiops.dto.AiopsWorkOrderCloseRequest;
import cn.sxu.enterprise.module.aiops.dto.AiopsWorkOrderCreateRequest;
import cn.sxu.enterprise.module.aiops.dto.AiopsWorkOrderHandleRequest;
import cn.sxu.enterprise.module.aiops.entity.AiopsAlertEvent;
import cn.sxu.enterprise.module.aiops.entity.AiopsWorkOrder;
import cn.sxu.enterprise.module.aiops.mapper.AiopsAlertEventMapper;
import cn.sxu.enterprise.module.aiops.mapper.AiopsWorkOrderMapper;
import cn.sxu.enterprise.module.aiops.service.AiopsWorkOrderService;
import cn.sxu.enterprise.module.aiops.vo.AiopsWorkOrderPageQuery;
import cn.sxu.enterprise.module.aiops.vo.AiopsWorkOrderPageVO;
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
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AiopsWorkOrderServiceImpl implements AiopsWorkOrderService {
    private static final DateTimeFormatter CODE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final AiopsWorkOrderMapper aiopsWorkOrderMapper;
    private final AiopsAlertEventMapper aiopsAlertEventMapper;
    public AiopsWorkOrderServiceImpl(AiopsWorkOrderMapper aiopsWorkOrderMapper, AiopsAlertEventMapper aiopsAlertEventMapper) { this.aiopsWorkOrderMapper = aiopsWorkOrderMapper; this.aiopsAlertEventMapper = aiopsAlertEventMapper; }
    @Override
    public PageResult<AiopsWorkOrderPageVO> pageWorkOrders(AiopsWorkOrderPageQuery query) {
        if (query == null) query = new AiopsWorkOrderPageQuery();
        Page<AiopsWorkOrder> page = new Page<>(query.getPageNo(), query.getPageSize());
        LambdaQueryWrapper<AiopsWorkOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getWorkOrderCode()), AiopsWorkOrder::getWorkOrderCode, query.getWorkOrderCode())
                .like(StringUtils.hasText(query.getEventCode()), AiopsWorkOrder::getEventCode, query.getEventCode())
                .eq(query.getAlertLevel() != null, AiopsWorkOrder::getAlertLevel, query.getAlertLevel())
                .like(StringUtils.hasText(query.getResourceCode()), AiopsWorkOrder::getResourceCode, query.getResourceCode())
                .like(StringUtils.hasText(query.getResourceName()), AiopsWorkOrder::getResourceName, query.getResourceName())
                .eq(StringUtils.hasText(query.getResourceType()), AiopsWorkOrder::getResourceType, query.getResourceType())
                .like(StringUtils.hasText(query.getIpAddr()), AiopsWorkOrder::getIpAddr, query.getIpAddr())
                .eq(StringUtils.hasText(query.getMetricType()), AiopsWorkOrder::getMetricType, query.getMetricType())
                .eq(query.getOrderStatus() != null, AiopsWorkOrder::getOrderStatus, query.getOrderStatus())
                .eq(query.getStatus() != null, AiopsWorkOrder::getStatus, query.getStatus())
                .orderByDesc(AiopsWorkOrder::getCreateTime).orderByDesc(AiopsWorkOrder::getId);
        Page<AiopsWorkOrder> resultPage = aiopsWorkOrderMapper.selectPage(page, wrapper);
        List<AiopsWorkOrderPageVO> records = resultPage.getRecords().stream().map(AiopsWorkOrderPageVO::fromEntity).toList();
        return PageResult.of(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal(), resultPage.getPages(), records);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiopsWorkOrderPageVO createFromAlert(AiopsWorkOrderCreateRequest request) {
        if (request == null || request.getAlertEventId() == null) throw new BusinessException(500, "告警事件ID不能为空");
        AiopsAlertEvent event = aiopsAlertEventMapper.selectById(request.getAlertEventId());
        if (event == null) throw new BusinessException(500, "告警事件不存在，无法生成运维工单");
        if (Integer.valueOf(2).equals(event.getHandleStatus())) throw new BusinessException(500, "告警事件已关闭，不能再生成运维工单");
        AiopsWorkOrder existed = aiopsWorkOrderMapper.selectOne(new LambdaQueryWrapper<AiopsWorkOrder>().eq(AiopsWorkOrder::getAlertEventId, request.getAlertEventId()).last("LIMIT 1"));
        if (existed != null) return AiopsWorkOrderPageVO.fromEntity(existed);
        LoginUser user = currentLoginUser(); LocalDateTime now = LocalDateTime.now();
        AiopsWorkOrder wo = new AiopsWorkOrder();
        wo.setWorkOrderCode("AIOPS-WO-" + now.format(CODE_TIME_FORMATTER) + "-" + ThreadLocalRandom.current().nextInt(1000, 10000));
        wo.setAlertEventId(event.getId()); wo.setEventCode(event.getEventCode()); wo.setAlertLevel(event.getAlertLevel()); wo.setWorkOrderTitle("AIOps告警工单-" + event.getAlertTitle()); wo.setWorkOrderContent(event.getAlertContent());
        wo.setResourceId(event.getResourceId()); wo.setResourceCode(event.getResourceCode()); wo.setResourceName(event.getResourceName()); wo.setResourceType(event.getResourceType()); wo.setIpAddr(event.getIpAddr());
        wo.setMetricCode(event.getMetricCode()); wo.setMetricName(event.getMetricName()); wo.setMetricType(event.getMetricType()); wo.setOrderStatus(0); wo.setStatus(0); wo.setCreateBy(username(user)); wo.setCreateTime(now); wo.setDeleted(0); wo.setRemark(request.getRemark());
        aiopsWorkOrderMapper.insert(wo);
        event.setHandleStatus(1); event.setUpdateBy(username(user)); event.setUpdateTime(now); aiopsAlertEventMapper.updateById(event);
        return AiopsWorkOrderPageVO.fromEntity(wo);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiopsWorkOrderPageVO handle(Long id, AiopsWorkOrderHandleRequest request) {
        AiopsWorkOrder wo = getWorkOrder(id); if (Integer.valueOf(3).equals(wo.getOrderStatus())) throw new BusinessException(500, "运维工单已关闭，不能重复处理");
        LoginUser user = currentLoginUser(); LocalDateTime now = LocalDateTime.now();
        wo.setOrderStatus(2); wo.setHandlerUserId(user == null ? null : user.getUserId()); wo.setHandlerUsername(username(user)); wo.setHandleTime(now); wo.setHandleResult(request.getHandleResult()); wo.setUpdateBy(username(user)); wo.setUpdateTime(now); if (StringUtils.hasText(request.getRemark())) wo.setRemark(request.getRemark());
        aiopsWorkOrderMapper.updateById(wo); return AiopsWorkOrderPageVO.fromEntity(wo);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiopsWorkOrderPageVO close(Long id, AiopsWorkOrderCloseRequest request) {
        AiopsWorkOrder wo = getWorkOrder(id); if (Integer.valueOf(3).equals(wo.getOrderStatus())) throw new BusinessException(500, "运维工单已关闭，不能重复关闭");
        LoginUser user = currentLoginUser(); LocalDateTime now = LocalDateTime.now();
        wo.setOrderStatus(3); wo.setCloseUserId(user == null ? null : user.getUserId()); wo.setCloseUsername(username(user)); wo.setCloseTime(now); wo.setCloseResult(request.getCloseResult()); wo.setUpdateBy(username(user)); wo.setUpdateTime(now); if (StringUtils.hasText(request.getRemark())) wo.setRemark(request.getRemark());
        aiopsWorkOrderMapper.updateById(wo);
        AiopsAlertEvent event = aiopsAlertEventMapper.selectById(wo.getAlertEventId());
        if (event != null) { event.setHandleStatus(2); event.setUpdateBy(username(user)); event.setUpdateTime(now); aiopsAlertEventMapper.updateById(event); }
        return AiopsWorkOrderPageVO.fromEntity(wo);
    }
    private AiopsWorkOrder getWorkOrder(Long id) { if (id == null) throw new BusinessException(500, "工单ID不能为空"); AiopsWorkOrder wo = aiopsWorkOrderMapper.selectById(id); if (wo == null) throw new BusinessException(500, "运维工单不存在"); return wo; }
    private LoginUser currentLoginUser() { Authentication a = SecurityContextHolder.getContext().getAuthentication(); return a != null && a.getPrincipal() instanceof LoginUser u ? u : null; }
    private String username(LoginUser u) { return u == null || !StringUtils.hasText(u.getUsername()) ? "system" : u.getUsername(); }
}
