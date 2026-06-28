SET NAMES utf8mb4;

USE enterprise_scaffold;

CREATE TABLE IF NOT EXISTS mine_sensor_data (
                                                id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                sensor_id BIGINT NOT NULL COMMENT '传感器ID，逻辑关联 mine_sensor.id',
                                                sensor_code VARCHAR(64) NOT NULL COMMENT '传感器编码',
    sensor_name VARCHAR(100) NOT NULL COMMENT '传感器名称',
    sensor_type VARCHAR(50) NOT NULL COMMENT '传感器类型：GAS/TEMPERATURE/VIBRATION 等',
    device_id BIGINT DEFAULT NULL COMMENT '所属设备ID，逻辑关联 mine_device.id',
    area_name VARCHAR(100) DEFAULT NULL COMMENT '所属区域',
    location VARCHAR(200) DEFAULT NULL COMMENT '安装位置',
    data_value DECIMAL(12,4) NOT NULL COMMENT '采集数值',
    unit VARCHAR(20) DEFAULT NULL COMMENT '单位',
    alarm_threshold DECIMAL(12,4) DEFAULT NULL COMMENT '告警阈值',
    alarm_flag TINYINT NOT NULL DEFAULT 0 COMMENT '是否告警：0否 1是',
    collect_time DATETIME NOT NULL COMMENT '采集时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '数据状态：0正常 1异常',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_sensor_id (sensor_id),
    KEY idx_sensor_code (sensor_code),
    KEY idx_sensor_type (sensor_type),
    KEY idx_area_name (area_name),
    KEY idx_collect_time (collect_time),
    KEY idx_alarm_flag (alarm_flag),
    KEY idx_status (status)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智能矿山传感器数据表';