package cn.sxu.enterprise.module.datahub.service.impl;

import cn.sxu.enterprise.common.core.exception.BusinessException;
import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.datahub.dto.DatahubQualityCheckRequest;
import cn.sxu.enterprise.module.datahub.entity.DatahubQualityResult;
import cn.sxu.enterprise.module.datahub.entity.DatahubQualityRule;
import cn.sxu.enterprise.module.datahub.mapper.DatahubQualityResultMapper;
import cn.sxu.enterprise.module.datahub.mapper.DatahubQualityRuleMapper;
import cn.sxu.enterprise.module.datahub.service.DatahubQualityService;
import cn.sxu.enterprise.module.datahub.vo.DatahubQualityResultPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubQualityResultPageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubQualityRulePageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubQualityRulePageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DatahubQualityServiceImpl implements DatahubQualityService {

    private static final DateTimeFormatter CODE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private final DatahubQualityRuleMapper qualityRuleMapper;
    private final DatahubQualityResultMapper qualityResultMapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public PageResult<DatahubQualityRulePageVO> pageRules(DatahubQualityRulePageQuery query) {
        long pageNo = normalizePageNo(query.getPageNo());
        long pageSize = normalizePageSize(query.getPageSize());

        LambdaQueryWrapper<DatahubQualityRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getRuleName()), DatahubQualityRule::getRuleName, query.getRuleName())
                .eq(StringUtils.hasText(query.getRuleType()), DatahubQualityRule::getRuleType, query.getRuleType())
                .eq(StringUtils.hasText(query.getTargetType()), DatahubQualityRule::getTargetType, query.getTargetType())
                .eq(query.getTargetTableId() != null, DatahubQualityRule::getTargetTableId, query.getTargetTableId())
                .eq(query.getTargetColumnId() != null, DatahubQualityRule::getTargetColumnId, query.getTargetColumnId())
                .eq(query.getStatus() != null, DatahubQualityRule::getStatus, query.getStatus())
                .orderByDesc(DatahubQualityRule::getId);

        IPage<DatahubQualityRule> page = qualityRuleMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);
        List<DatahubQualityRulePageVO> records = page.getRecords().stream()
                .map(this::toRuleVO)
                .collect(Collectors.toList());
        return buildPageResult(page, records);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DatahubQualityResultPageVO> check(DatahubQualityCheckRequest request) {
        List<DatahubQualityRule> rules = findCheckRules(request);
        if (rules.isEmpty()) {
            throw new BusinessException(500, "未找到可执行的数据质量规则，请先确认 datahub_quality_rule 中存在启用规则");
        }

        List<DatahubQualityResultPageVO> resultVOList = new ArrayList<>();
        for (DatahubQualityRule rule : rules) {
            DatahubQualityResult result = executeOneRule(rule);
            qualityResultMapper.insert(result);
            resultVOList.add(toResultVO(result));
        }
        return resultVOList;
    }

    @Override
    public PageResult<DatahubQualityResultPageVO> pageResults(DatahubQualityResultPageQuery query) {
        long pageNo = normalizePageNo(query.getPageNo());
        long pageSize = normalizePageSize(query.getPageSize());

        LambdaQueryWrapper<DatahubQualityResult> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getRuleName()), DatahubQualityResult::getRuleName, query.getRuleName())
                .like(StringUtils.hasText(query.getRuleCode()), DatahubQualityResult::getRuleCode, query.getRuleCode())
                .like(StringUtils.hasText(query.getTableCode()), DatahubQualityResult::getTableCode, query.getTableCode())
                .like(StringUtils.hasText(query.getColumnName()), DatahubQualityResult::getColumnName, query.getColumnName())
                .eq(query.getCheckStatus() != null, DatahubQualityResult::getCheckStatus, query.getCheckStatus())
                .eq(query.getTableId() != null, DatahubQualityResult::getTableId, query.getTableId())
                .eq(query.getColumnId() != null, DatahubQualityResult::getColumnId, query.getColumnId())
                .orderByDesc(DatahubQualityResult::getCheckTime)
                .orderByDesc(DatahubQualityResult::getId);

        IPage<DatahubQualityResult> page = qualityResultMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);
        List<DatahubQualityResultPageVO> records = page.getRecords().stream()
                .map(this::toResultVO)
                .collect(Collectors.toList());
        return buildPageResult(page, records);
    }

    private List<DatahubQualityRule> findCheckRules(DatahubQualityCheckRequest request) {
        LambdaQueryWrapper<DatahubQualityRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DatahubQualityRule::getStatus, 0)
                .orderByAsc(DatahubQualityRule::getId);
        if (request != null && request.getRuleId() != null) {
            wrapper.eq(DatahubQualityRule::getId, request.getRuleId());
        }
        if (request != null && request.getRuleId() == null && request.getTargetTableId() != null) {
            wrapper.eq(DatahubQualityRule::getTargetTableId, request.getTargetTableId());
        }
        return qualityRuleMapper.selectList(wrapper);
    }

    private DatahubQualityResult executeOneRule(DatahubQualityRule rule) {
        TargetInfo targetInfo = queryTargetInfo(rule);
        DataSourceInfo dataSourceInfo = queryDataSourceInfo(targetInfo.datasourceId);
        if (!"MYSQL".equalsIgnoreCase(dataSourceInfo.datasourceType)) {
            throw new BusinessException(500, "D4-04 当前仅支持 MySQL 数据源质量检测");
        }

        String fullTableName = buildFullTableName(targetInfo.schemaName, targetInfo.tableName);
        Long checkTotal;
        Long errorTotal;
        try (Connection connection = DriverManager.getConnection(dataSourceInfo.jdbcUrl, dataSourceInfo.username, dataSourceInfo.password);
             Statement statement = connection.createStatement()) {
            checkTotal = queryLong(statement, "SELECT COUNT(*) FROM " + fullTableName);
            errorTotal = queryErrorTotal(statement, rule, targetInfo, fullTableName);
        } catch (SQLException ex) {
            throw new BusinessException(500, "数据质量检测执行失败：" + ex.getMessage());
        }

        BigDecimal errorRate = BigDecimal.ZERO;
        if (checkTotal != null && checkTotal > 0) {
            errorRate = BigDecimal.valueOf(errorTotal)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(checkTotal), 4, RoundingMode.HALF_UP);
        }

        DatahubQualityResult result = new DatahubQualityResult();
        result.setResultCode("DQR" + LocalDateTime.now().format(CODE_TIME_FORMATTER));
        result.setRuleId(rule.getId());
        result.setRuleCode(rule.getRuleCode());
        result.setRuleName(rule.getRuleName());
        result.setDatasourceId(targetInfo.datasourceId);
        result.setDatasourceCode(dataSourceInfo.datasourceName);
        result.setTableId(targetInfo.tableId);
        result.setTableCode(targetInfo.tableName);
        result.setColumnId(targetInfo.columnId);
        result.setColumnName(targetInfo.columnName);
        result.setCheckTotal(checkTotal == null ? 0L : checkTotal);
        result.setErrorTotal(errorTotal == null ? 0L : errorTotal);
        result.setErrorRate(errorRate);
        result.setCheckStatus(errorTotal != null && errorTotal > 0 ? 1 : 0);
        result.setCheckTime(LocalDateTime.now());
        result.setStatus(0);
        result.setCreateBy("system");
        result.setCreateTime(LocalDateTime.now());
        result.setUpdateBy("system");
        result.setUpdateTime(LocalDateTime.now());
        result.setDeleted(0);
        result.setRemark(buildResultRemark(rule, errorTotal));
        return result;
    }

    private Long queryErrorTotal(Statement statement, DatahubQualityRule rule, TargetInfo targetInfo, String fullTableName) throws SQLException {
        String ruleType = safeUpper(rule.getRuleType());
        if ("TABLE".equalsIgnoreCase(rule.getTargetType())) {
            if ("NOT_NULL".equals(ruleType) || "UNIQUE".equals(ruleType) || "RANGE".equals(ruleType)
                    || "FORMAT".equals(ruleType) || "ENUM".equals(ruleType)) {
                throw new BusinessException(500, "规则 " + rule.getRuleCode() + " 是字段级规则，请设置 targetColumnId");
            }
        }

        String columnName = targetInfo.columnName;
        if (!StringUtils.hasText(columnName)) {
            throw new BusinessException(500, "规则 " + rule.getRuleCode() + " 缺少目标字段，无法执行字段级检测");
        }
        String column = quoteIdentifier(columnName);

        if ("NOT_NULL".equals(ruleType)) {
            String sql = "SELECT COUNT(*) FROM " + fullTableName + " WHERE " + column + " IS NULL OR TRIM(CAST(" + column + " AS CHAR)) = ''";
            return queryLong(statement, sql);
        }
        if ("UNIQUE".equals(ruleType)) {
            String sql = "SELECT COALESCE(SUM(t.duplicate_count), 0) FROM ("
                    + "SELECT COUNT(*) - 1 AS duplicate_count FROM " + fullTableName
                    + " WHERE " + column + " IS NOT NULL"
                    + " GROUP BY " + column
                    + " HAVING COUNT(*) > 1) t";
            return queryLong(statement, sql);
        }
        if ("RANGE".equals(ruleType)) {
            Range range = parseRange(rule.getCheckExpression(), rule.getRuleCode());
            String sql = "SELECT COUNT(*) FROM " + fullTableName
                    + " WHERE " + column + " IS NOT NULL AND (" + column + " < " + range.min + " OR " + column + " > " + range.max + ")";
            return queryLong(statement, sql);
        }
        if ("FORMAT".equals(ruleType)) {
            String expression = requireExpression(rule);
            String sql = "SELECT COUNT(*) FROM " + fullTableName
                    + " WHERE " + column + " IS NOT NULL AND " + column + " NOT REGEXP " + quoteSqlValue(expression);
            return queryLong(statement, sql);
        }
        if ("ENUM".equals(ruleType)) {
            List<String> values = parseEnumValues(requireExpression(rule));
            if (values.isEmpty()) {
                throw new BusinessException(500, "规则 " + rule.getRuleCode() + " 的 ENUM 表达式为空");
            }
            String valueSql = values.stream().map(this::quoteSqlValue).collect(Collectors.joining(", "));
            String sql = "SELECT COUNT(*) FROM " + fullTableName
                    + " WHERE " + column + " IS NOT NULL AND " + column + " NOT IN (" + valueSql + ")";
            return queryLong(statement, sql);
        }

        throw new BusinessException(500, "不支持的质量规则类型：" + rule.getRuleType());
    }

    private TargetInfo queryTargetInfo(DatahubQualityRule rule) {
        if ("COLUMN".equalsIgnoreCase(rule.getTargetType())) {
            if (rule.getTargetColumnId() == null) {
                throw new BusinessException(500, "字段级规则必须配置 target_column_id");
            }
            try {
                Map<String, Object> row = jdbcTemplate.queryForMap(
                        "SELECT c.id AS column_id, c.column_name, c.table_id, c.table_name, "
                                + "c.data_source_id, c.data_source_name, t.schema_name "
                                + "FROM datahub_metadata_column c "
                                + "LEFT JOIN datahub_metadata_table t ON c.table_id = t.id AND t.deleted = 0 "
                                + "WHERE c.id = ? AND c.deleted = 0",
                        rule.getTargetColumnId());
                return TargetInfo.fromColumnRow(row);
            } catch (EmptyResultDataAccessException ex) {
                throw new BusinessException(500, "未找到目标字段元数据，targetColumnId=" + rule.getTargetColumnId());
            }
        }

        if (rule.getTargetTableId() == null) {
            throw new BusinessException(500, "表级规则必须配置 target_table_id");
        }
        try {
            Map<String, Object> row = jdbcTemplate.queryForMap(
                    "SELECT id AS table_id, table_name, data_source_id, data_source_name, schema_name "
                            + "FROM datahub_metadata_table WHERE id = ? AND deleted = 0",
                    rule.getTargetTableId());
            return TargetInfo.fromTableRow(row);
        } catch (EmptyResultDataAccessException ex) {
            throw new BusinessException(500, "未找到目标表元数据，targetTableId=" + rule.getTargetTableId());
        }
    }

    private DataSourceInfo queryDataSourceInfo(Long datasourceId) {
        if (datasourceId == null) {
            throw new BusinessException(500, "目标元数据缺少 data_source_id，无法定位 datahub_datasource");
        }
        try {
            Map<String, Object> row = jdbcTemplate.queryForMap(
                    "SELECT id, datasource_name, datasource_type, jdbc_url, username, password "
                            + "FROM datahub_datasource WHERE id = ? AND deleted = 0",
                    datasourceId);
            DataSourceInfo info = new DataSourceInfo();
            info.id = toLong(row.get("id"));
            info.datasourceName = toStringValue(row.get("datasource_name"));
            info.datasourceType = toStringValue(row.get("datasource_type"));
            info.jdbcUrl = toStringValue(row.get("jdbc_url"));
            info.username = toStringValue(row.get("username"));
            info.password = toStringValue(row.get("password"));
            if (!StringUtils.hasText(info.jdbcUrl)) {
                throw new BusinessException(500, "datahub_datasource.jdbc_url 为空，无法执行质量检测");
            }
            if (!StringUtils.hasText(info.username)) {
                throw new BusinessException(500, "datahub_datasource.username 为空，无法执行质量检测");
            }
            return info;
        } catch (EmptyResultDataAccessException ex) {
            throw new BusinessException(500, "未找到数据源，id=" + datasourceId);
        }
    }

    private String buildFullTableName(String schemaName, String tableName) {
        if (!StringUtils.hasText(tableName)) {
            throw new BusinessException(500, "目标表名为空，无法执行质量检测");
        }
        if (StringUtils.hasText(schemaName)) {
            return quoteIdentifier(schemaName) + "." + quoteIdentifier(tableName);
        }
        return quoteIdentifier(tableName);
    }

    private String quoteIdentifier(String identifier) {
        if (!StringUtils.hasText(identifier)) {
            throw new BusinessException(500, "SQL 标识符不能为空");
        }
        String trimmed = identifier.trim();
        if (!trimmed.matches("[A-Za-z0-9_]+")) {
            throw new BusinessException(500, "SQL 标识符只允许字母、数字和下划线：" + identifier);
        }
        return "`" + trimmed + "`";
    }

    private String quoteSqlValue(String value) {
        return "'" + value.replace("'", "''") + "'";
    }

    private Long queryLong(Statement statement, String sql) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        }
        return 0L;
    }

    private Range parseRange(String expression, String ruleCode) {
        if (!StringUtils.hasText(expression)) {
            throw new BusinessException(500, "规则 " + ruleCode + " 的 RANGE 表达式不能为空，格式示例：0,100");
        }
        String[] parts = expression.split(",");
        if (parts.length != 2) {
            throw new BusinessException(500, "规则 " + ruleCode + " 的 RANGE 表达式格式错误，格式示例：0,100");
        }
        try {
            Range range = new Range();
            range.min = new BigDecimal(parts[0].trim());
            range.max = new BigDecimal(parts[1].trim());
            return range;
        } catch (NumberFormatException ex) {
            throw new BusinessException(500, "规则 " + ruleCode + " 的 RANGE 表达式必须是数字范围，格式示例：0,100");
        }
    }

    private String requireExpression(DatahubQualityRule rule) {
        if (!StringUtils.hasText(rule.getCheckExpression())) {
            throw new BusinessException(500, "规则 " + rule.getRuleCode() + " 的 checkExpression 不能为空");
        }
        return rule.getCheckExpression().trim();
    }

    private List<String> parseEnumValues(String expression) {
        if (!StringUtils.hasText(expression)) {
            return Collections.emptyList();
        }
        return Arrays.stream(expression.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
    }

    private DatahubQualityRulePageVO toRuleVO(DatahubQualityRule entity) {
        DatahubQualityRulePageVO vo = new DatahubQualityRulePageVO();
        BeanUtils.copyProperties(entity, vo);
        vo.setTargetTableName(queryTableName(entity.getTargetTableId()));
        vo.setTargetColumnName(queryColumnName(entity.getTargetColumnId()));
        return vo;
    }

    private DatahubQualityResultPageVO toResultVO(DatahubQualityResult entity) {
        DatahubQualityResultPageVO vo = new DatahubQualityResultPageVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    private String queryTableName(Long tableId) {
        if (tableId == null) {
            return null;
        }
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT table_name FROM datahub_metadata_table WHERE id = ? AND deleted = 0",
                    String.class,
                    tableId);
        } catch (Exception ex) {
            return null;
        }
    }

    private String queryColumnName(Long columnId) {
        if (columnId == null) {
            return null;
        }
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT column_name FROM datahub_metadata_column WHERE id = ? AND deleted = 0",
                    String.class,
                    columnId);
        } catch (Exception ex) {
            return null;
        }
    }

    private String buildResultRemark(DatahubQualityRule rule, Long errorTotal) {
        if (errorTotal != null && errorTotal > 0) {
            return "规则 " + rule.getRuleCode() + " 检测不通过，发现异常数据 " + errorTotal + " 条";
        }
        return "规则 " + rule.getRuleCode() + " 检测通过";
    }

    private String safeUpper(String value) {
        return value == null ? "" : value.toUpperCase(Locale.ROOT);
    }

    private long normalizePageNo(Long pageNo) {
        return pageNo == null || pageNo < 1 ? 1L : pageNo;
    }

    private long normalizePageSize(Long pageSize) {
        if (pageSize == null || pageSize < 1) {
            return 10L;
        }
        return Math.min(pageSize, 100L);
    }

    private <E, V> PageResult<V> buildPageResult(IPage<E> page, List<V> records) {
        PageResult<V> result = new PageResult<>();
        result.setPageNo(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setRecords(records);
        return result;
    }

    private static Long toLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.valueOf(String.valueOf(value));
    }

    private static String toStringValue(Object value) {
        return Objects.toString(value, null);
    }

    private static class TargetInfo {
        private Long datasourceId;
        private String datasourceName;
        private Long tableId;
        private String tableName;
        private Long columnId;
        private String columnName;
        private String schemaName;

        private static TargetInfo fromColumnRow(Map<String, Object> row) {
            TargetInfo info = new TargetInfo();
            info.datasourceId = toLong(row.get("data_source_id"));
            info.datasourceName = toStringValue(row.get("data_source_name"));
            info.tableId = toLong(row.get("table_id"));
            info.tableName = toStringValue(row.get("table_name"));
            info.columnId = toLong(row.get("column_id"));
            info.columnName = toStringValue(row.get("column_name"));
            info.schemaName = toStringValue(row.get("schema_name"));
            return info;
        }

        private static TargetInfo fromTableRow(Map<String, Object> row) {
            TargetInfo info = new TargetInfo();
            info.datasourceId = toLong(row.get("data_source_id"));
            info.datasourceName = toStringValue(row.get("data_source_name"));
            info.tableId = toLong(row.get("table_id"));
            info.tableName = toStringValue(row.get("table_name"));
            info.schemaName = toStringValue(row.get("schema_name"));
            return info;
        }
    }

    private static class DataSourceInfo {
        private Long id;
        private String datasourceName;
        private String datasourceType;
        private String jdbcUrl;
        private String username;
        private String password;
    }

    private static class Range {
        private BigDecimal min;
        private BigDecimal max;
    }
}
