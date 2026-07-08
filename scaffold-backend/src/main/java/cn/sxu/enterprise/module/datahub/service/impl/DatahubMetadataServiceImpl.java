package cn.sxu.enterprise.module.datahub.service.impl;

import cn.sxu.enterprise.common.core.exception.BusinessException;
import cn.sxu.enterprise.common.core.page.PageResult;
import cn.sxu.enterprise.module.datahub.dto.DatahubMetadataCollectRequest;
import cn.sxu.enterprise.module.datahub.entity.DatahubMetadataCollectLog;
import cn.sxu.enterprise.module.datahub.entity.DatahubMetadataColumn;
import cn.sxu.enterprise.module.datahub.entity.DatahubMetadataTable;
import cn.sxu.enterprise.module.datahub.mapper.DatahubMetadataCollectLogMapper;
import cn.sxu.enterprise.module.datahub.mapper.DatahubMetadataColumnMapper;
import cn.sxu.enterprise.module.datahub.mapper.DatahubMetadataTableMapper;
import cn.sxu.enterprise.module.datahub.service.DatahubMetadataService;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataCollectLogPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataCollectLogPageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataCollectResultVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataColumnPageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataColumnPageVO;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataTablePageQuery;
import cn.sxu.enterprise.module.datahub.vo.DatahubMetadataTablePageVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class DatahubMetadataServiceImpl implements DatahubMetadataService {

    private static final int BUSINESS_ERROR_CODE = 500;

    /**
     * 当前 docker-compose 中 MySQL 容器名。
     * 你的 docker ps 里显示的容器名就是 enterprise-scaffold-mysql。
     */
    private static final String DEFAULT_DOCKER_MYSQL_HOST = "enterprise-scaffold-mysql";

    private final JdbcTemplate jdbcTemplate;
    private final DatahubMetadataTableMapper metadataTableMapper;
    private final DatahubMetadataColumnMapper metadataColumnMapper;
    private final DatahubMetadataCollectLogMapper collectLogMapper;

    public DatahubMetadataServiceImpl(
            JdbcTemplate jdbcTemplate,
            DatahubMetadataTableMapper metadataTableMapper,
            DatahubMetadataColumnMapper metadataColumnMapper,
            DatahubMetadataCollectLogMapper collectLogMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.metadataTableMapper = metadataTableMapper;
        this.metadataColumnMapper = metadataColumnMapper;
        this.collectLogMapper = collectLogMapper;
    }

    @Override
    public DatahubMetadataCollectResultVO collect(DatahubMetadataCollectRequest request) {
        if (request == null || request.dataSourceId == null) {
            throw new BusinessException(BUSINESS_ERROR_CODE, "数据源ID不能为空");
        }

        Map<String, Object> dataSource = queryDataSource(request.dataSourceId);

        Integer status = readInteger(dataSource, "status");
        if (status != null && status != 0) {
            throw new BusinessException(BUSINESS_ERROR_CODE, "数据源不是正常状态，不能采集元数据");
        }

        String dbType = readString(
                dataSource,
                "datasource_type",
                "data_source_type",
                "source_type",
                "db_type",
                "database_type",
                "type",
                "dbType",
                "databaseType"
        );

        if (!isMysqlDataSource(dbType)) {
            throw new BusinessException(
                    BUSINESS_ERROR_CODE,
                    "D4-03 当前仅支持 MySQL 数据源元数据采集，当前数据源类型为：" + dbType
            );
        }

        String dataSourceName = readString(
                dataSource,
                "datasource_name",
                "data_source_name",
                "source_name",
                "name",
                "datasource_code",
                "dataSourceName",
                "sourceName"
        );

        if (!StringUtils.hasText(dataSourceName)) {
            dataSourceName = "未命名数据源";
        }

        String databaseName = readString(
                dataSource,
                "database_name",
                "db_name",
                "schema_name",
                "databaseName",
                "dbName",
                "schemaName"
        );

        if (!StringUtils.hasText(databaseName)) {
            throw new BusinessException(BUSINESS_ERROR_CODE, "数据源 database_name 不能为空");
        }

        String username = readString(
                dataSource,
                "username",
                "user_name",
                "userName",
                "user"
        );

        if (!StringUtils.hasText(username)) {
            throw new BusinessException(BUSINESS_ERROR_CODE, "数据源 username 不能为空");
        }

        String password = readString(
                dataSource,
                "password",
                "password_value",
                "passwordValue",
                "pwd"
        );

        String jdbcUrl = buildJdbcUrl(dataSource, databaseName);

        String collectBatchNo = buildCollectBatchNo();
        LocalDateTime startTime = LocalDateTime.now();

        List<DatahubMetadataTable> scannedTables = new ArrayList<>();
        List<DatahubMetadataColumn> scannedColumns = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            DatabaseMetaData metaData = connection.getMetaData();
            LocalDateTime collectTime = LocalDateTime.now();

            try (ResultSet tableRs = metaData.getTables(databaseName, null, "%", new String[]{"TABLE", "VIEW"})) {
                while (tableRs.next()) {
                    DatahubMetadataTable table = new DatahubMetadataTable();
                    table.dataSourceId = request.dataSourceId;
                    table.dataSourceName = dataSourceName;
                    table.schemaName = databaseName;
                    table.tableName = tableRs.getString("TABLE_NAME");
                    table.tableComment = tableRs.getString("REMARKS");
                    table.tableType = tableRs.getString("TABLE_TYPE");
                    table.rowCount = countRows(connection, databaseName, table.tableName, table.tableType);
                    table.collectBatchNo = collectBatchNo;
                    table.collectTime = collectTime;
                    table.status = 0;
                    table.createTime = LocalDateTime.now();
                    table.deleted = 0;
                    scannedTables.add(table);
                }
            }

            for (DatahubMetadataTable table : scannedTables) {
                Set<String> primaryKeys = readPrimaryKeys(metaData, databaseName, table.tableName);

                try (ResultSet columnRs = metaData.getColumns(databaseName, null, table.tableName, "%")) {
                    while (columnRs.next()) {
                        DatahubMetadataColumn column = new DatahubMetadataColumn();
                        column.dataSourceId = request.dataSourceId;
                        column.dataSourceName = dataSourceName;
                        column.schemaName = databaseName;
                        column.tableName = table.tableName;
                        column.columnName = columnRs.getString("COLUMN_NAME");
                        column.columnComment = columnRs.getString("REMARKS");
                        column.dataType = columnRs.getString("TYPE_NAME");
                        column.columnType = buildColumnType(columnRs);
                        column.columnLength = safeInt(columnRs, "COLUMN_SIZE");
                        column.numericPrecision = safeInt(columnRs, "COLUMN_SIZE");
                        column.numericScale = safeInt(columnRs, "DECIMAL_DIGITS");
                        column.nullableFlag = columnRs.getInt("NULLABLE") == DatabaseMetaData.columnNullable ? 1 : 0;
                        column.primaryKeyFlag = primaryKeys.contains(column.columnName) ? 1 : 0;
                        column.columnDefault = columnRs.getString("COLUMN_DEF");
                        column.ordinalPosition = safeInt(columnRs, "ORDINAL_POSITION");
                        column.collectBatchNo = collectBatchNo;
                        column.collectTime = collectTime;
                        column.status = 0;
                        column.createTime = LocalDateTime.now();
                        column.deleted = 0;
                        scannedColumns.add(column);
                    }
                }
            }

            clearOldMetadata(request.dataSourceId);

            for (DatahubMetadataTable table : scannedTables) {
                metadataTableMapper.insert(table);

                for (DatahubMetadataColumn column : scannedColumns) {
                    if (table.tableName.equals(column.tableName) && table.schemaName.equals(column.schemaName)) {
                        column.tableId = table.id;
                        metadataColumnMapper.insert(column);
                    }
                }
            }

            LocalDateTime endTime = LocalDateTime.now();
            long costTime = Duration.between(startTime, endTime).toMillis();

            DatahubMetadataCollectLog log = new DatahubMetadataCollectLog();
            log.collectBatchNo = collectBatchNo;
            log.dataSourceId = request.dataSourceId;
            log.dataSourceName = dataSourceName;
            log.collectStatus = 0;
            log.tableTotal = scannedTables.size();
            log.columnTotal = scannedColumns.size();
            log.errorMsg = null;
            log.startTime = startTime;
            log.endTime = endTime;
            log.costTime = costTime;
            log.status = 0;
            log.createTime = LocalDateTime.now();
            log.deleted = 0;
            safeInsertCollectLog(log);

            return DatahubMetadataCollectResultVO.of(
                    collectBatchNo,
                    request.dataSourceId,
                    dataSourceName,
                    scannedTables.size(),
                    scannedColumns.size(),
                    costTime
            );
        } catch (Exception e) {
            LocalDateTime endTime = LocalDateTime.now();
            long costTime = Duration.between(startTime, endTime).toMillis();

            DatahubMetadataCollectLog log = new DatahubMetadataCollectLog();
            log.collectBatchNo = collectBatchNo;
            log.dataSourceId = request.dataSourceId;
            log.dataSourceName = dataSourceName;
            log.collectStatus = 1;
            log.tableTotal = 0;
            log.columnTotal = 0;
            log.errorMsg = limitErrorMsg(e.getMessage());
            log.startTime = startTime;
            log.endTime = endTime;
            log.costTime = costTime;
            log.status = 1;
            log.createTime = LocalDateTime.now();
            log.deleted = 0;
            safeInsertCollectLog(log);

            throw new BusinessException(BUSINESS_ERROR_CODE, "元数据采集失败：" + e.getMessage());
        }
    }

    @Override
    public PageResult<DatahubMetadataTablePageVO> pageTables(DatahubMetadataTablePageQuery query) {
        if (query == null) {
            query = new DatahubMetadataTablePageQuery();
        }

        Page<DatahubMetadataTable> page = new Page<>(safePageNo(query.pageNo), safePageSize(query.pageSize));
        QueryWrapper<DatahubMetadataTable> wrapper = new QueryWrapper<>();

        if (query.dataSourceId != null) {
            wrapper.eq("data_source_id", query.dataSourceId);
        }
        if (StringUtils.hasText(query.schemaName)) {
            wrapper.like("schema_name", query.schemaName);
        }
        if (StringUtils.hasText(query.tableName)) {
            wrapper.like("table_name", query.tableName);
        }
        if (StringUtils.hasText(query.tableType)) {
            wrapper.eq("table_type", query.tableType);
        }
        if (query.status != null) {
            wrapper.eq("status", query.status);
        }

        wrapper.orderByDesc("collect_time").orderByAsc("table_name");

        Page<DatahubMetadataTable> result = metadataTableMapper.selectPage(page, wrapper);
        List<DatahubMetadataTablePageVO> records = result.getRecords()
                .stream()
                .map(DatahubMetadataTablePageVO::fromEntity)
                .collect(Collectors.toList());

        return PageResult.of(result.getCurrent(), result.getSize(), result.getTotal(), result.getPages(), records);
    }

    @Override
    public PageResult<DatahubMetadataColumnPageVO> pageColumns(DatahubMetadataColumnPageQuery query) {
        if (query == null) {
            query = new DatahubMetadataColumnPageQuery();
        }

        Page<DatahubMetadataColumn> page = new Page<>(safePageNo(query.pageNo), safePageSize(query.pageSize));
        QueryWrapper<DatahubMetadataColumn> wrapper = new QueryWrapper<>();

        if (query.tableId != null) {
            wrapper.eq("table_id", query.tableId);
        }
        if (query.dataSourceId != null) {
            wrapper.eq("data_source_id", query.dataSourceId);
        }
        if (StringUtils.hasText(query.schemaName)) {
            wrapper.like("schema_name", query.schemaName);
        }
        if (StringUtils.hasText(query.tableName)) {
            wrapper.like("table_name", query.tableName);
        }
        if (StringUtils.hasText(query.columnName)) {
            wrapper.like("column_name", query.columnName);
        }
        if (StringUtils.hasText(query.dataType)) {
            wrapper.like("data_type", query.dataType);
        }
        if (query.nullableFlag != null) {
            wrapper.eq("nullable_flag", query.nullableFlag);
        }
        if (query.primaryKeyFlag != null) {
            wrapper.eq("primary_key_flag", query.primaryKeyFlag);
        }
        if (query.status != null) {
            wrapper.eq("status", query.status);
        }

        wrapper.orderByAsc("ordinal_position").orderByAsc("id");

        Page<DatahubMetadataColumn> result = metadataColumnMapper.selectPage(page, wrapper);
        List<DatahubMetadataColumnPageVO> records = result.getRecords()
                .stream()
                .map(DatahubMetadataColumnPageVO::fromEntity)
                .collect(Collectors.toList());

        return PageResult.of(result.getCurrent(), result.getSize(), result.getTotal(), result.getPages(), records);
    }

    @Override
    public PageResult<DatahubMetadataCollectLogPageVO> pageCollectLogs(DatahubMetadataCollectLogPageQuery query) {
        if (query == null) {
            query = new DatahubMetadataCollectLogPageQuery();
        }

        Page<DatahubMetadataCollectLog> page = new Page<>(safePageNo(query.pageNo), safePageSize(query.pageSize));
        QueryWrapper<DatahubMetadataCollectLog> wrapper = new QueryWrapper<>();

        if (query.dataSourceId != null) {
            wrapper.eq("data_source_id", query.dataSourceId);
        }
        if (StringUtils.hasText(query.collectBatchNo)) {
            wrapper.like("collect_batch_no", query.collectBatchNo);
        }
        if (query.collectStatus != null) {
            wrapper.eq("collect_status", query.collectStatus);
        }
        if (query.status != null) {
            wrapper.eq("status", query.status);
        }

        wrapper.orderByDesc("start_time").orderByDesc("id");

        Page<DatahubMetadataCollectLog> result = collectLogMapper.selectPage(page, wrapper);
        List<DatahubMetadataCollectLogPageVO> records = result.getRecords()
                .stream()
                .map(DatahubMetadataCollectLogPageVO::fromEntity)
                .collect(Collectors.toList());

        return PageResult.of(result.getCurrent(), result.getSize(), result.getTotal(), result.getPages(), records);
    }

    private Map<String, Object> queryDataSource(Long dataSourceId) {
        try {
            return jdbcTemplate.queryForMap(
                    "SELECT * FROM datahub_datasource WHERE id = ? AND deleted = 0",
                    dataSourceId
            );
        } catch (EmptyResultDataAccessException e) {
            throw new BusinessException(BUSINESS_ERROR_CODE, "数据源不存在");
        } catch (Exception e) {
            throw new BusinessException(BUSINESS_ERROR_CODE, "查询数据源失败：" + e.getMessage());
        }
    }

    private void clearOldMetadata(Long dataSourceId) {
        QueryWrapper<DatahubMetadataColumn> oldColumnWrapper = new QueryWrapper<>();
        oldColumnWrapper.eq("data_source_id", dataSourceId);
        metadataColumnMapper.delete(oldColumnWrapper);

        QueryWrapper<DatahubMetadataTable> oldTableWrapper = new QueryWrapper<>();
        oldTableWrapper.eq("data_source_id", dataSourceId);
        metadataTableMapper.delete(oldTableWrapper);
    }

    private String buildJdbcUrl(Map<String, Object> dataSource, String databaseName) {
        String jdbcUrl = readString(
                dataSource,
                "jdbc_url",
                "connection_url",
                "url",
                "jdbcUrl",
                "connectionUrl"
        );

        if (StringUtils.hasText(jdbcUrl)) {
            jdbcUrl = normalizeJdbcUrlForDocker(jdbcUrl);
            return appendInformationSchema(jdbcUrl);
        }

        String host = readString(
                dataSource,
                "host",
                "host_name",
                "hostName"
        );

        Integer port = readInteger(dataSource, "port");

        if (!StringUtils.hasText(host)) {
            throw new BusinessException(BUSINESS_ERROR_CODE, "数据源 host 不能为空");
        }

        if (port == null) {
            port = 3306;
        }

        host = normalizeHostForDocker(host);

        return "jdbc:mysql://" + host + ":" + port + "/" + databaseName
                + "?useUnicode=true"
                + "&characterEncoding=utf8"
                + "&serverTimezone=Asia/Shanghai"
                + "&useSSL=false"
                + "&allowPublicKeyRetrieval=true"
                + "&useInformationSchema=true";
    }

    private String appendInformationSchema(String jdbcUrl) {
        if (!StringUtils.hasText(jdbcUrl)) {
            return jdbcUrl;
        }

        if (jdbcUrl.contains("useInformationSchema=true")) {
            return jdbcUrl;
        }

        if (jdbcUrl.contains("?")) {
            return jdbcUrl + "&useInformationSchema=true";
        }

        return jdbcUrl + "?useInformationSchema=true";
    }

    private String normalizeJdbcUrlForDocker(String jdbcUrl) {
        if (!StringUtils.hasText(jdbcUrl)) {
            return jdbcUrl;
        }

        if (!isRunningInDocker()) {
            return jdbcUrl;
        }

        String normalizedUrl = jdbcUrl;
        normalizedUrl = normalizedUrl.replace("jdbc:mysql://localhost:", "jdbc:mysql://" + DEFAULT_DOCKER_MYSQL_HOST + ":");
        normalizedUrl = normalizedUrl.replace("jdbc:mysql://127.0.0.1:", "jdbc:mysql://" + DEFAULT_DOCKER_MYSQL_HOST + ":");

        return normalizedUrl;
    }

    private String normalizeHostForDocker(String host) {
        if (!StringUtils.hasText(host)) {
            return host;
        }

        if (!isRunningInDocker()) {
            return host;
        }

        if ("localhost".equalsIgnoreCase(host) || "127.0.0.1".equals(host)) {
            return DEFAULT_DOCKER_MYSQL_HOST;
        }

        return host;
    }

    private boolean isRunningInDocker() {
        return new File("/.dockerenv").exists();
    }

    private boolean isMysqlDataSource(String dbType) {
        if (!StringUtils.hasText(dbType)) {
            return false;
        }

        String normalized = dbType.trim().toLowerCase();
        return "mysql".equals(normalized) || normalized.contains("mysql");
    }

    private String buildCollectBatchNo() {
        return "META"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + ThreadLocalRandom.current().nextInt(1000, 9999);
    }

    private Set<String> readPrimaryKeys(DatabaseMetaData metaData, String databaseName, String tableName) throws Exception {
        Set<String> primaryKeys = new HashSet<>();

        try (ResultSet pkRs = metaData.getPrimaryKeys(databaseName, null, tableName)) {
            while (pkRs.next()) {
                primaryKeys.add(pkRs.getString("COLUMN_NAME"));
            }
        }

        return primaryKeys;
    }

    private Long countRows(Connection connection, String databaseName, String tableName, String tableType) {
        if (!"TABLE".equalsIgnoreCase(tableType)) {
            return null;
        }

        String sql = "SELECT COUNT(*) FROM " + quoteName(databaseName) + "." + quoteName(tableName);

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (Exception ignored) {
            return null;
        }

        return null;
    }

    private String quoteName(String name) {
        if (name == null) {
            return "``";
        }

        return "`" + name.replace("`", "``") + "`";
    }

    private String buildColumnType(ResultSet columnRs) {
        try {
            String typeName = columnRs.getString("TYPE_NAME");
            int size = columnRs.getInt("COLUMN_SIZE");
            int scale = columnRs.getInt("DECIMAL_DIGITS");

            if (!StringUtils.hasText(typeName)) {
                return null;
            }

            if (size <= 0) {
                return typeName;
            }

            if (scale > 0) {
                return typeName + "(" + size + "," + scale + ")";
            }

            return typeName + "(" + size + ")";
        } catch (Exception e) {
            return null;
        }
    }

    private Integer safeInt(ResultSet rs, String columnName) {
        try {
            int value = rs.getInt(columnName);
            return rs.wasNull() ? null : value;
        } catch (Exception e) {
            return null;
        }
    }

    private long safePageNo(Long pageNo) {
        if (pageNo == null || pageNo < 1) {
            return 1L;
        }

        return pageNo;
    }

    private long safePageSize(Long pageSize) {
        if (pageSize == null || pageSize < 1) {
            return 10L;
        }

        return Math.min(pageSize, 100L);
    }

    private String readString(Map<String, Object> map, String... names) {
        Object value = readValue(map, names);

        if (value == null) {
            return null;
        }

        return String.valueOf(value).trim();
    }

    private Integer readInteger(Map<String, Object> map, String... names) {
        Object value = readValue(map, names);

        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (Exception e) {
            return null;
        }
    }

    private Object readValue(Map<String, Object> map, String... names) {
        if (map == null || names == null) {
            return null;
        }

        for (String name : names) {
            if (map.containsKey(name)) {
                return map.get(name);
            }
        }

        for (String name : names) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey() != null && entry.getKey().equalsIgnoreCase(name)) {
                    return entry.getValue();
                }
            }
        }

        for (String name : names) {
            String normalizedName = normalizeKey(name);

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey() != null && normalizeKey(entry.getKey()).equals(normalizedName)) {
                    return entry.getValue();
                }
            }
        }

        return null;
    }

    private String normalizeKey(String key) {
        if (key == null) {
            return "";
        }

        return key.replace("_", "").replace("-", "").toLowerCase();
    }

    private void safeInsertCollectLog(DatahubMetadataCollectLog log) {
        try {
            collectLogMapper.insert(log);
        } catch (Exception ignored) {
            // 避免采集日志写入失败时覆盖真正的采集异常。
        }
    }

    private String limitErrorMsg(String errorMsg) {
        if (errorMsg == null) {
            return null;
        }

        if (errorMsg.length() <= 500) {
            return errorMsg;
        }

        return errorMsg.substring(0, 500);
    }
}