SET NAMES utf8mb4;

USE enterprise_scaffold;

-- D4-05：敏感数据识别和脱敏
-- 说明：
-- 1. 本 SQL 新增 datahub_sensitive_field、datahub_mask_rule、datahub_mask_result 三张表。
-- 2. 本 SQL 会给 datahub_metadata_column 补充 sensitive_flag、sensitive_type、mask_type 三个字段。
-- 3. 如果你已经手动加过这些字段，本 SQL 会自动跳过，不会重复添加。

SET @exist_sensitive_flag := (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'datahub_metadata_column'
      AND COLUMN_NAME = 'sensitive_flag'
);
SET @sql_sensitive_flag := IF(
    @exist_sensitive_flag = 0,
    'ALTER TABLE datahub_metadata_column ADD COLUMN sensitive_flag TINYINT NOT NULL DEFAULT 0 COMMENT ''是否敏感字段：0否，1是'' AFTER primary_key_flag',
    'SELECT ''sensitive_flag already exists'''
);
PREPARE stmt FROM @sql_sensitive_flag;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exist_sensitive_type := (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'datahub_metadata_column'
      AND COLUMN_NAME = 'sensitive_type'
);
SET @sql_sensitive_type := IF(
    @exist_sensitive_type = 0,
    'ALTER TABLE datahub_metadata_column ADD COLUMN sensitive_type VARCHAR(32) DEFAULT NULL COMMENT ''敏感类型：ID_CARD/PHONE/EMAIL/BANK_CARD/NAME/ADDRESS'' AFTER sensitive_flag',
    'SELECT ''sensitive_type already exists'''
);
PREPARE stmt FROM @sql_sensitive_type;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @exist_mask_type := (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'datahub_metadata_column'
      AND COLUMN_NAME = 'mask_type'
);
SET @sql_mask_type := IF(
    @exist_mask_type = 0,
    'ALTER TABLE datahub_metadata_column ADD COLUMN mask_type VARCHAR(32) DEFAULT ''NONE'' COMMENT ''脱敏类型：NONE/PARTIAL/HASH/FULL'' AFTER sensitive_type',
    'SELECT ''mask_type already exists'''
);
PREPARE stmt FROM @sql_mask_type;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

CREATE TABLE IF NOT EXISTS datahub_sensitive_field (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    field_code VARCHAR(64) NOT NULL COMMENT '敏感字段编码',
    datasource_id BIGINT DEFAULT NULL COMMENT '数据源ID，对应 datahub_datasource.id',
    datasource_name VARCHAR(100) DEFAULT NULL COMMENT '数据源名称',
    table_id BIGINT NOT NULL COMMENT '元数据表ID，对应 datahub_metadata_table.id',
    schema_name VARCHAR(100) DEFAULT NULL COMMENT 'Schema名称',
    table_name VARCHAR(128) NOT NULL COMMENT '表名',
    column_id BIGINT NOT NULL COMMENT '元数据字段ID，对应 datahub_metadata_column.id',
    column_name VARCHAR(128) NOT NULL COMMENT '字段名',
    column_comment VARCHAR(500) DEFAULT NULL COMMENT '字段注释',
    data_type VARCHAR(64) DEFAULT NULL COMMENT '数据类型',
    sensitive_type VARCHAR(32) NOT NULL COMMENT '敏感类型：ID_CARD/PHONE/EMAIL/BANK_CARD/NAME/ADDRESS',
    sensitive_level TINYINT NOT NULL DEFAULT 2 COMMENT '敏感级别：1一般，2重要，3严重',
    detect_rule VARCHAR(200) DEFAULT NULL COMMENT '命中的识别规则',
    confidence DECIMAL(5,2) NOT NULL DEFAULT 0.80 COMMENT '识别置信度',
    confirm_status TINYINT NOT NULL DEFAULT 0 COMMENT '确认状态：0待确认，1已确认，2误报',
    mask_type VARCHAR(32) NOT NULL DEFAULT 'PARTIAL' COMMENT '默认脱敏类型：NONE/PARTIAL/HASH/FULL',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0有效，1无效',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_datahub_sensitive_field_code (field_code),
    UNIQUE KEY uk_datahub_sensitive_field_column (column_id),
    KEY idx_datahub_sensitive_field_table (table_id),
    KEY idx_datahub_sensitive_field_type (sensitive_type),
    KEY idx_datahub_sensitive_field_confirm (confirm_status),
    KEY idx_datahub_sensitive_field_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据治理-敏感字段识别结果表';

CREATE TABLE IF NOT EXISTS datahub_mask_rule (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    rule_code VARCHAR(64) NOT NULL COMMENT '脱敏规则编码',
    rule_name VARCHAR(100) NOT NULL COMMENT '脱敏规则名称',
    sensitive_type VARCHAR(32) NOT NULL COMMENT '适用敏感类型：ID_CARD/PHONE/EMAIL/BANK_CARD/NAME/ADDRESS',
    mask_method VARCHAR(32) NOT NULL COMMENT '脱敏方式：PARTIAL/HASH/FULL',
    keep_prefix INT NOT NULL DEFAULT 3 COMMENT '前端保留位数',
    keep_suffix INT NOT NULL DEFAULT 4 COMMENT '末尾保留位数',
    mask_char VARCHAR(8) NOT NULL DEFAULT '*' COMMENT '掩码字符',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0启用，1停用',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_datahub_mask_rule_code (rule_code),
    KEY idx_datahub_mask_rule_type (sensitive_type),
    KEY idx_datahub_mask_rule_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据治理-脱敏规则表';

CREATE TABLE IF NOT EXISTS datahub_mask_result (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    result_code VARCHAR(64) NOT NULL COMMENT '脱敏结果编码',
    field_id BIGINT NOT NULL COMMENT '敏感字段ID，对应 datahub_sensitive_field.id',
    rule_id BIGINT DEFAULT NULL COMMENT '脱敏规则ID，对应 datahub_mask_rule.id',
    datasource_id BIGINT DEFAULT NULL COMMENT '数据源ID',
    datasource_name VARCHAR(100) DEFAULT NULL COMMENT '数据源名称',
    table_id BIGINT NOT NULL COMMENT '元数据表ID',
    table_name VARCHAR(128) NOT NULL COMMENT '表名',
    column_id BIGINT NOT NULL COMMENT '元数据字段ID',
    column_name VARCHAR(128) NOT NULL COMMENT '字段名',
    sensitive_type VARCHAR(32) NOT NULL COMMENT '敏感类型',
    mask_method VARCHAR(32) NOT NULL COMMENT '脱敏方式',
    sample_before VARCHAR(500) DEFAULT NULL COMMENT '脱敏前示例值',
    sample_after VARCHAR(500) DEFAULT NULL COMMENT '脱敏后示例值',
    mask_status TINYINT NOT NULL DEFAULT 0 COMMENT '脱敏状态：0成功，1失败',
    mask_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '脱敏时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0有效，1无效',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_datahub_mask_result_code (result_code),
    KEY idx_datahub_mask_result_field (field_id),
    KEY idx_datahub_mask_result_rule (rule_id),
    KEY idx_datahub_mask_result_table (table_id),
    KEY idx_datahub_mask_result_type (sensitive_type),
    KEY idx_datahub_mask_result_status (mask_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据治理-脱敏预览结果表';

INSERT IGNORE INTO datahub_mask_rule
(rule_code, rule_name, sensitive_type, mask_method, keep_prefix, keep_suffix, mask_char, status, create_by, remark)
VALUES
('MASK-PHONE-PARTIAL', '手机号保留前三后四', 'PHONE', 'PARTIAL', 3, 4, '*', 0, 'system', '手机号示例：138****5678'),
('MASK-IDCARD-PARTIAL', '身份证保留前三后四', 'ID_CARD', 'PARTIAL', 3, 4, '*', 0, 'system', '身份证示例：110***********1234'),
('MASK-EMAIL-PARTIAL', '邮箱用户名部分脱敏', 'EMAIL', 'PARTIAL', 2, 8, '*', 0, 'system', '邮箱示例：zh***@example.com'),
('MASK-BANKCARD-PARTIAL', '银行卡保留前六后四', 'BANK_CARD', 'PARTIAL', 6, 4, '*', 0, 'system', '银行卡示例：622202******1234'),
('MASK-NAME-PARTIAL', '姓名保留首字', 'NAME', 'PARTIAL', 1, 0, '*', 0, 'system', '姓名示例：张**'),
('MASK-ADDRESS-PARTIAL', '地址部分脱敏', 'ADDRESS', 'PARTIAL', 6, 0, '*', 0, 'system', '地址示例：山西省太原市******'),
('MASK-GENERAL-HASH', '通用哈希脱敏', 'GENERAL', 'HASH', 0, 0, '*', 0, 'system', '用于不可逆哈希脱敏'),
('MASK-GENERAL-FULL', '通用全量遮盖', 'GENERAL', 'FULL', 0, 0, '*', 0, 'system', '用于全量替换为 ******');
