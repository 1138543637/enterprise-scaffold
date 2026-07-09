package cn.sxu.enterprise.module.datahub.service.impl;

import cn.sxu.enterprise.common.core.exception.BusinessException;
import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.datahub.dto.DatahubMaskPreviewRequest;
import cn.sxu.enterprise.module.datahub.dto.DatahubSensitiveScanRequest;
import cn.sxu.enterprise.module.datahub.entity.DatahubMaskResult;
import cn.sxu.enterprise.module.datahub.entity.DatahubMaskRule;
import cn.sxu.enterprise.module.datahub.entity.DatahubMetadataColumn;
import cn.sxu.enterprise.module.datahub.entity.DatahubSensitiveField;
import cn.sxu.enterprise.module.datahub.mapper.DatahubMaskResultMapper;
import cn.sxu.enterprise.module.datahub.mapper.DatahubMaskRuleMapper;
import cn.sxu.enterprise.module.datahub.mapper.DatahubMetadataColumnMapper;
import cn.sxu.enterprise.module.datahub.mapper.DatahubSensitiveFieldMapper;
import cn.sxu.enterprise.module.datahub.service.DatahubSensitiveService;
import cn.sxu.enterprise.module.datahub.vo.DatahubMaskResultPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubMaskResultPageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubMaskRulePageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubMaskRulePageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubSensitiveFieldPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubSensitiveFieldPageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class DatahubSensitiveServiceImpl implements DatahubSensitiveService {

    private final DatahubMetadataColumnMapper metadataColumnMapper;
    private final DatahubSensitiveFieldMapper sensitiveFieldMapper;
    private final DatahubMaskRuleMapper maskRuleMapper;
    private final DatahubMaskResultMapper maskResultMapper;

    public DatahubSensitiveServiceImpl(DatahubMetadataColumnMapper metadataColumnMapper,
                                       DatahubSensitiveFieldMapper sensitiveFieldMapper,
                                       DatahubMaskRuleMapper maskRuleMapper,
                                       DatahubMaskResultMapper maskResultMapper) {
        this.metadataColumnMapper = metadataColumnMapper;
        this.sensitiveFieldMapper = sensitiveFieldMapper;
        this.maskRuleMapper = maskRuleMapper;
        this.maskResultMapper = maskResultMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DatahubSensitiveFieldPageVO> scanSensitiveFields(DatahubSensitiveScanRequest request) {
        if (request == null) {
            request = new DatahubSensitiveScanRequest();
        }

        int limit = request.getLimit() == null ? 500 : request.getLimit();

        LambdaQueryWrapper<DatahubMetadataColumn> columnWrapper = new LambdaQueryWrapper<>();
        columnWrapper.eq(DatahubMetadataColumn::getDeleted, 0);
        columnWrapper.eq(DatahubMetadataColumn::getStatus, 0);
        if (request.getTargetTableId() != null) {
            columnWrapper.eq(DatahubMetadataColumn::getTableId, request.getTargetTableId());
        }
        columnWrapper.orderByAsc(DatahubMetadataColumn::getTableId);
        columnWrapper.orderByAsc(DatahubMetadataColumn::getOrdinalPosition);
        columnWrapper.last("LIMIT " + limit);

        List<DatahubMetadataColumn> columns = metadataColumnMapper.selectList(columnWrapper);
        List<DatahubSensitiveFieldPageVO> result = new ArrayList<>();

        for (DatahubMetadataColumn column : columns) {
            SensitiveDetectResult detectResult = detect(column);
            if (detectResult == null) {
                continue;
            }
            if (StringUtils.hasText(request.getSensitiveType())
                    && !request.getSensitiveType().equalsIgnoreCase(detectResult.sensitiveType())) {
                continue;
            }

            DatahubSensitiveField field = sensitiveFieldMapper.selectOne(
                    new LambdaQueryWrapper<DatahubSensitiveField>()
                            .eq(DatahubSensitiveField::getColumnId, column.getId())
                            .last("LIMIT 1")
            );

            LocalDateTime now = LocalDateTime.now();
            if (field == null) {
                field = new DatahubSensitiveField();
                field.setFieldCode("SF-" + now.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + column.getId());
                field.setColumnId(column.getId());
                field.setCreateBy("system");
                field.setCreateTime(now);
                field.setDeleted(0);
            }

            field.setDatasourceId(column.getDataSourceId());
            field.setDatasourceName(column.getDataSourceName());
            field.setTableId(column.getTableId());
            field.setSchemaName(column.getSchemaName());
            field.setTableName(column.getTableName());
            field.setColumnName(column.getColumnName());
            field.setColumnComment(column.getColumnComment());
            field.setDataType(column.getDataType());
            field.setSensitiveType(detectResult.sensitiveType());
            field.setSensitiveLevel(detectResult.sensitiveLevel());
            field.setDetectRule(detectResult.detectRule());
            field.setConfidence(detectResult.confidence());
            field.setConfirmStatus(0);
            field.setMaskType(defaultMaskType(detectResult.sensitiveType()));
            field.setStatus(0);
            field.setUpdateBy("system");
            field.setUpdateTime(now);
            field.setRemark("D4-05 自动识别生成");

            if (field.getId() == null) {
                sensitiveFieldMapper.insert(field);
            } else {
                sensitiveFieldMapper.updateById(field);
            }

            column.setSensitiveFlag(1);
            column.setSensitiveType(detectResult.sensitiveType());
            column.setMaskType(defaultMaskType(detectResult.sensitiveType()));
            column.setUpdateTime(now);
            metadataColumnMapper.updateById(column);

            result.add(DatahubSensitiveFieldPageVO.fromEntity(field));
        }

        return result;
    }

    @Override
    public PageResult<DatahubSensitiveFieldPageVO> pageSensitiveFields(DatahubSensitiveFieldPageQuery query) {
        if (query == null) {
            query = new DatahubSensitiveFieldPageQuery();
        }

        Page<DatahubSensitiveField> page = new Page<>(query.getPageNo(), query.getPageSize());
        LambdaQueryWrapper<DatahubSensitiveField> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.hasText(query.getDatasourceName()), DatahubSensitiveField::getDatasourceName, query.getDatasourceName());
        wrapper.like(StringUtils.hasText(query.getTableName()), DatahubSensitiveField::getTableName, query.getTableName());
        wrapper.like(StringUtils.hasText(query.getColumnName()), DatahubSensitiveField::getColumnName, query.getColumnName());
        wrapper.eq(StringUtils.hasText(query.getSensitiveType()), DatahubSensitiveField::getSensitiveType, query.getSensitiveType());
        wrapper.eq(query.getConfirmStatus() != null, DatahubSensitiveField::getConfirmStatus, query.getConfirmStatus());
        wrapper.eq(query.getStatus() != null, DatahubSensitiveField::getStatus, query.getStatus());
        wrapper.orderByDesc(DatahubSensitiveField::getCreateTime);
        wrapper.orderByDesc(DatahubSensitiveField::getId);

        Page<DatahubSensitiveField> resultPage = sensitiveFieldMapper.selectPage(page, wrapper);
        List<DatahubSensitiveFieldPageVO> records = resultPage.getRecords()
                .stream()
                .map(DatahubSensitiveFieldPageVO::fromEntity)
                .toList();

        return PageResult.of(
                resultPage.getCurrent(),
                resultPage.getSize(),
                resultPage.getTotal(),
                resultPage.getPages(),
                records
        );
    }

    @Override
    public PageResult<DatahubMaskRulePageVO> pageMaskRules(DatahubMaskRulePageQuery query) {
        if (query == null) {
            query = new DatahubMaskRulePageQuery();
        }

        Page<DatahubMaskRule> page = new Page<>(query.getPageNo(), query.getPageSize());
        LambdaQueryWrapper<DatahubMaskRule> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.hasText(query.getRuleCode()), DatahubMaskRule::getRuleCode, query.getRuleCode());
        wrapper.like(StringUtils.hasText(query.getRuleName()), DatahubMaskRule::getRuleName, query.getRuleName());
        wrapper.eq(StringUtils.hasText(query.getSensitiveType()), DatahubMaskRule::getSensitiveType, query.getSensitiveType());
        wrapper.eq(StringUtils.hasText(query.getMaskMethod()), DatahubMaskRule::getMaskMethod, query.getMaskMethod());
        wrapper.eq(query.getStatus() != null, DatahubMaskRule::getStatus, query.getStatus());
        wrapper.orderByAsc(DatahubMaskRule::getSensitiveType);
        wrapper.orderByAsc(DatahubMaskRule::getId);

        Page<DatahubMaskRule> resultPage = maskRuleMapper.selectPage(page, wrapper);
        List<DatahubMaskRulePageVO> records = resultPage.getRecords()
                .stream()
                .map(DatahubMaskRulePageVO::fromEntity)
                .toList();

        return PageResult.of(
                resultPage.getCurrent(),
                resultPage.getSize(),
                resultPage.getTotal(),
                resultPage.getPages(),
                records
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DatahubMaskResultPageVO previewMask(DatahubMaskPreviewRequest request) {
        DatahubSensitiveField field = sensitiveFieldMapper.selectById(request.getFieldId());
        if (field == null) {
            throw new BusinessException(500, "敏感字段不存在，请先执行敏感字段识别");
        }

        DatahubMaskRule rule;
        if (request.getMaskRuleId() != null) {
            rule = maskRuleMapper.selectById(request.getMaskRuleId());
        } else {
            rule = maskRuleMapper.selectOne(
                    new LambdaQueryWrapper<DatahubMaskRule>()
                            .eq(DatahubMaskRule::getSensitiveType, field.getSensitiveType())
                            .eq(DatahubMaskRule::getStatus, 0)
                            .orderByAsc(DatahubMaskRule::getId)
                            .last("LIMIT 1")
            );
        }

        if (rule == null) {
            rule = maskRuleMapper.selectOne(
                    new LambdaQueryWrapper<DatahubMaskRule>()
                            .eq(DatahubMaskRule::getSensitiveType, "GENERAL")
                            .eq(DatahubMaskRule::getStatus, 0)
                            .orderByAsc(DatahubMaskRule::getId)
                            .last("LIMIT 1")
            );
        }

        if (rule == null) {
            throw new BusinessException(500,"未找到可用脱敏规则，请先确认 datahub_mask_rule 初始化数据存在");
        }

        String before = StringUtils.hasText(request.getRawValue())
                ? request.getRawValue()
                : defaultSampleValue(field.getSensitiveType());
        String after = applyMask(before, rule);

        LocalDateTime now = LocalDateTime.now();
        DatahubMaskResult result = new DatahubMaskResult();
        result.setResultCode("MR-" + now.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
        result.setFieldId(field.getId());
        result.setRuleId(rule.getId());
        result.setDatasourceId(field.getDatasourceId());
        result.setDatasourceName(field.getDatasourceName());
        result.setTableId(field.getTableId());
        result.setTableName(field.getTableName());
        result.setColumnId(field.getColumnId());
        result.setColumnName(field.getColumnName());
        result.setSensitiveType(field.getSensitiveType());
        result.setMaskMethod(rule.getMaskMethod());
        result.setSampleBefore(before);
        result.setSampleAfter(after);
        result.setMaskStatus(0);
        result.setMaskTime(now);
        result.setStatus(0);
        result.setCreateBy("system");
        result.setCreateTime(now);
        result.setUpdateBy("system");
        result.setUpdateTime(now);
        result.setDeleted(0);
        result.setRemark("D4-05 脱敏预览生成");

        maskResultMapper.insert(result);
        return DatahubMaskResultPageVO.fromEntity(result);
    }

    @Override
    public PageResult<DatahubMaskResultPageVO> pageMaskResults(DatahubMaskResultPageQuery query) {
        if (query == null) {
            query = new DatahubMaskResultPageQuery();
        }

        Page<DatahubMaskResult> page = new Page<>(query.getPageNo(), query.getPageSize());
        LambdaQueryWrapper<DatahubMaskResult> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.hasText(query.getTableName()), DatahubMaskResult::getTableName, query.getTableName());
        wrapper.like(StringUtils.hasText(query.getColumnName()), DatahubMaskResult::getColumnName, query.getColumnName());
        wrapper.eq(StringUtils.hasText(query.getSensitiveType()), DatahubMaskResult::getSensitiveType, query.getSensitiveType());
        wrapper.eq(StringUtils.hasText(query.getMaskMethod()), DatahubMaskResult::getMaskMethod, query.getMaskMethod());
        wrapper.eq(query.getMaskStatus() != null, DatahubMaskResult::getMaskStatus, query.getMaskStatus());
        wrapper.eq(query.getStatus() != null, DatahubMaskResult::getStatus, query.getStatus());
        wrapper.orderByDesc(DatahubMaskResult::getMaskTime);
        wrapper.orderByDesc(DatahubMaskResult::getId);

        Page<DatahubMaskResult> resultPage = maskResultMapper.selectPage(page, wrapper);
        List<DatahubMaskResultPageVO> records = resultPage.getRecords()
                .stream()
                .map(DatahubMaskResultPageVO::fromEntity)
                .toList();

        return PageResult.of(
                resultPage.getCurrent(),
                resultPage.getSize(),
                resultPage.getTotal(),
                resultPage.getPages(),
                records
        );
    }

    private SensitiveDetectResult detect(DatahubMetadataColumn column) {
        String name = safeLower(column.getColumnName());
        String comment = safeLower(column.getColumnComment());
        String combined = name + " " + comment;

        if (isIpField(name, comment)) {
            return null;
        }

        if (containsAny(combined, "id_card", "idcard", "identity", "cert_no", "certificate_no", "身份证", "证件号码")) {
            return new SensitiveDetectResult("ID_CARD", 3, "字段名或注释命中身份证规则", new BigDecimal("0.95"));
        }

        if (containsAny(combined, "phone", "mobile", "tel", "telephone", "手机号", "手机", "电话", "联系方式")) {
            return new SensitiveDetectResult("PHONE", 2, "字段名或注释命中手机号规则", new BigDecimal("0.92"));
        }

        if (containsAny(combined, "email", "mail", "邮箱", "电子邮件")) {
            return new SensitiveDetectResult("EMAIL", 2, "字段名或注释命中邮箱规则", new BigDecimal("0.90"));
        }

        if (containsAny(combined, "bank_card", "bankcard", "card_no", "account_no", "bank_account", "银行卡", "卡号", "银行账号")) {
            return new SensitiveDetectResult("BANK_CARD", 3, "字段名或注释命中银行卡规则", new BigDecimal("0.93"));
        }

        if (containsAny(combined, "real_name", "user_name", "customer_name", "contact_name", "姓名", "客户姓名", "联系人")) {
            return new SensitiveDetectResult("NAME", 2, "字段名或注释命中姓名规则", new BigDecimal("0.85"));
        }

        if (containsAny(combined, "address", "home_address", "contact_address", "location_detail", "住址", "地址", "详细地址")) {
            return new SensitiveDetectResult("ADDRESS", 2, "字段名或注释命中地址规则", new BigDecimal("0.86"));
        }

        return null;
    }

    private boolean isIpField(String name, String comment) {
        String normalizedName = name == null ? "" : name.replace("_", "").replace("-", "").toLowerCase(Locale.ROOT);
        String normalizedComment = comment == null ? "" : comment.toLowerCase(Locale.ROOT);

        return "ip".equals(name)
                || name.endsWith("_ip")
                || name.startsWith("ip_")
                || normalizedName.contains("ipaddr")
                || normalizedName.contains("ipaddress")
                || "oper_ip".equals(name)
                || "login_ip".equals(name)
                || "last_login_ip".equals(name)
                || normalizedComment.contains("ip地址")
                || normalizedComment.contains("登录ip")
                || normalizedComment.contains("操作ip");
    }

    private String defaultMaskType(String sensitiveType) {
        if (Objects.equals("ID_CARD", sensitiveType)
                || Objects.equals("PHONE", sensitiveType)
                || Objects.equals("EMAIL", sensitiveType)
                || Objects.equals("BANK_CARD", sensitiveType)
                || Objects.equals("NAME", sensitiveType)
                || Objects.equals("ADDRESS", sensitiveType)) {
            return "PARTIAL";
        }
        return "FULL";
    }

    private String defaultSampleValue(String sensitiveType) {
        if (Objects.equals("PHONE", sensitiveType)) {
            return "13812345678";
        }
        if (Objects.equals("ID_CARD", sensitiveType)) {
            return "110101199001011234";
        }
        if (Objects.equals("EMAIL", sensitiveType)) {
            return "zhangsan@example.com";
        }
        if (Objects.equals("BANK_CARD", sensitiveType)) {
            return "6222021001112221234";
        }
        if (Objects.equals("NAME", sensitiveType)) {
            return "张三丰";
        }
        if (Objects.equals("ADDRESS", sensitiveType)) {
            return "山西省太原市小店区学府街100号";
        }
        return "SensitiveValue123";
    }

    private String applyMask(String value, DatahubMaskRule rule) {
        if (!StringUtils.hasText(value)) {
            return value;
        }

        String method = rule.getMaskMethod();
        if (Objects.equals("FULL", method)) {
            return "******";
        }
        if (Objects.equals("HASH", method)) {
            return hash(value);
        }

        int keepPrefix = rule.getKeepPrefix() == null ? 0 : rule.getKeepPrefix();
        int keepSuffix = rule.getKeepSuffix() == null ? 0 : rule.getKeepSuffix();
        String maskChar = StringUtils.hasText(rule.getMaskChar()) ? rule.getMaskChar() : "*";

        if (value.length() <= keepPrefix + keepSuffix) {
            return maskChar.repeat(Math.max(value.length(), 1));
        }

        String prefix = keepPrefix > 0 ? value.substring(0, keepPrefix) : "";
        String suffix = keepSuffix > 0 ? value.substring(value.length() - keepSuffix) : "";
        int maskLength = value.length() - keepPrefix - keepSuffix;
        return prefix + maskChar.repeat(Math.max(maskLength, 1)) + suffix;
    }

    private String hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes).substring(0, 32);
        } catch (Exception e) {
            throw new BusinessException(500,"脱敏哈希计算失败：" + e.getMessage());
        }
    }

    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }

    private record SensitiveDetectResult(
            String sensitiveType,
            Integer sensitiveLevel,
            String detectRule,
            BigDecimal confidence
    ) {
    }
}
