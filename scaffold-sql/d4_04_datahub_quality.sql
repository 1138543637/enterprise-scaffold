SET NAMES utf8mb4;

USE enterprise_scaffold;

CREATE TABLE IF NOT EXISTS datahub_quality_rule (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    rule_code VARCHAR(64) NOT NULL COMMENT '规则编码',
    rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
    rule_type VARCHAR(32) NOT NULL COMMENT '规则类型：NOT_NULL/UNIQUE/RANGE/FORMAT/ENUM',
    target_type VARCHAR(16) NOT NULL COMMENT '检测对象类型：TABLE/COLUMN',
    target_table_id BIGINT NULL COMMENT '目标元数据表ID，对应 datahub_metadata_table.id',
    target_column_id BIGINT NULL COMMENT '目标元数据字段ID，对应 datahub_metadata_column.id',
    check_expression VARCHAR(500) NULL COMMENT '检测表达式：RANGE使用最小值,最大值；FORMAT使用正则；ENUM使用逗号分隔枚举值',
    quality_level TINYINT NOT NULL DEFAULT 1 COMMENT '质量等级：1一般，2重要，3严重',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0启用，1停用',
    create_by VARCHAR(64) NULL COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NULL COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_datahub_quality_rule_code (rule_code),
    KEY idx_datahub_quality_rule_target_table (target_table_id),
    KEY idx_datahub_quality_rule_target_column (target_column_id),
    KEY idx_datahub_quality_rule_type_status (rule_type, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据质量规则表';

CREATE TABLE IF NOT EXISTS datahub_quality_result (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    result_code VARCHAR(64) NOT NULL COMMENT '检测结果编码',
    rule_id BIGINT NOT NULL COMMENT '质量规则ID',
    rule_code VARCHAR(64) NOT NULL COMMENT '规则编码',
    rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
    datasource_id BIGINT NULL COMMENT '数据源ID，对应 datahub_datasource.id',
    datasource_code VARCHAR(100) NULL COMMENT '数据源标识，当前使用 datahub_datasource.datasource_name 填充',
    table_id BIGINT NULL COMMENT '元数据表ID，对应 datahub_metadata_table.id',
    table_code VARCHAR(100) NULL COMMENT '表标识，当前使用 datahub_metadata_table.table_name 填充',
    column_id BIGINT NULL COMMENT '元数据字段ID，对应 datahub_metadata_column.id',
    column_name VARCHAR(100) NULL COMMENT '字段名称',
    check_total BIGINT NOT NULL DEFAULT 0 COMMENT '检测总数',
    error_total BIGINT NOT NULL DEFAULT 0 COMMENT '异常总数',
    error_rate DECIMAL(10,4) NOT NULL DEFAULT 0.0000 COMMENT '异常率，0到100',
    check_status TINYINT NOT NULL DEFAULT 0 COMMENT '检测状态：0通过，1不通过',
    check_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '检测时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0有效，1无效',
    create_by VARCHAR(64) NULL COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NULL COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_datahub_quality_result_code (result_code),
    KEY idx_datahub_quality_result_rule_id (rule_id),
    KEY idx_datahub_quality_result_table_id (table_id),
    KEY idx_datahub_quality_result_column_id (column_id),
    KEY idx_datahub_quality_result_status_time (check_status, check_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据质量检测结果表';

INSERT INTO datahub_quality_rule (
    rule_code,
    rule_name,
    rule_type,
    target_type,
    target_table_id,
    target_column_id,
    check_expression,
    quality_level,
    status,
    create_by,
    remark
)
SELECT
    CONCAT('DQ_NOT_NULL_', c.id) AS rule_code,
    CONCAT(c.table_name, '.', c.column_name, ' 非空检测') AS rule_name,
    'NOT_NULL' AS rule_type,
    'COLUMN' AS target_type,
    c.table_id AS target_table_id,
    c.id AS target_column_id,
    NULL AS check_expression,
    CASE WHEN c.primary_key_flag = 'YES' THEN 3 ELSE 2 END AS quality_level,
    0 AS status,
    'system' AS create_by,
    'D4-04 初始化：根据元数据字段 nullable_flag 自动生成非空检测规则' AS remark
FROM datahub_metadata_column c
WHERE c.deleted = 0
  AND UPPER(IFNULL(c.nullable_flag, '')) IN ('NO', 'N', '0', 'FALSE')
  AND NOT EXISTS (
      SELECT 1
      FROM datahub_quality_rule r
      WHERE r.rule_code = CONCAT('DQ_NOT_NULL_', c.id)
        AND r.deleted = 0
  );
