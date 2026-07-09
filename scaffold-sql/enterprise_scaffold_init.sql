SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS enterprise_scaffold
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE enterprise_scaffold;

DROP TABLE IF EXISTS sys_user_post;
DROP TABLE IF EXISTS sys_role_menu;
DROP TABLE IF EXISTS sys_user_role;
DROP TABLE IF EXISTS sys_file;
DROP TABLE IF EXISTS sys_oper_log;
DROP TABLE IF EXISTS sys_login_log;
DROP TABLE IF EXISTS sys_dict_data;
DROP TABLE IF EXISTS sys_dict_type;
DROP TABLE IF EXISTS sys_post;
DROP TABLE IF EXISTS sys_menu;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS sys_user;
DROP TABLE IF EXISTS sys_dept;

CREATE TABLE sys_dept (
                          id BIGINT NOT NULL AUTO_INCREMENT COMMENT '部门ID',
                          parent_id BIGINT NOT NULL DEFAULT 0 COMMENT '父部门ID',
                          ancestors VARCHAR(255) NOT NULL DEFAULT '' COMMENT '祖级列表，例如 0,1,2',
                          dept_name VARCHAR(100) NOT NULL COMMENT '部门名称',
                          dept_code VARCHAR(64) NOT NULL COMMENT '部门编码',
                          order_num INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
                          leader VARCHAR(50) DEFAULT NULL COMMENT '负责人',
                          phone VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
                          email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
                          status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常 1停用',
                          create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
                          create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
                          update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
                          remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
                          PRIMARY KEY (id),
                          UNIQUE KEY uk_dept_code (dept_code),
                          KEY idx_parent_id (parent_id),
                          KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

CREATE TABLE sys_user (
                          id BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                          dept_id BIGINT DEFAULT NULL COMMENT '部门ID',
                          username VARCHAR(50) NOT NULL COMMENT '登录账号',
                          nickname VARCHAR(50) NOT NULL COMMENT '用户昵称',
                          password_hash VARCHAR(100) NOT NULL COMMENT '密码哈希，S0-05接入登录时改为BCrypt',
                          user_type VARCHAR(20) NOT NULL DEFAULT 'SYSTEM' COMMENT '用户类型：SYSTEM系统用户',
                          email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
                          phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
                          gender TINYINT NOT NULL DEFAULT 0 COMMENT '性别：0未知 1男 2女',
                          avatar VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
                          status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常 1停用',
                          last_login_ip VARCHAR(64) DEFAULT NULL COMMENT '最后登录IP',
                          last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间',
                          create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
                          create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
                          update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
                          remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
                          PRIMARY KEY (id),
                          UNIQUE KEY uk_username (username),
                          KEY idx_dept_id (dept_id),
                          KEY idx_phone (phone),
                          KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

CREATE TABLE sys_role (
                          id BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
                          role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
                          role_key VARCHAR(64) NOT NULL COMMENT '角色权限字符串，例如 admin',
                          role_sort INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
                          data_scope TINYINT NOT NULL DEFAULT 1 COMMENT '数据范围：1全部 2本部门 3本人',
                          status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常 1停用',
                          create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
                          create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
                          update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
                          remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
                          PRIMARY KEY (id),
                          UNIQUE KEY uk_role_key (role_key),
                          KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

CREATE TABLE sys_menu (
                          id BIGINT NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
                          parent_id BIGINT NOT NULL DEFAULT 0 COMMENT '父菜单ID',
                          menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
                          menu_type CHAR(1) NOT NULL COMMENT '菜单类型：M目录 C菜单 F按钮',
                          path VARCHAR(255) DEFAULT NULL COMMENT '路由地址',
                          component VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
                          perms VARCHAR(100) DEFAULT NULL COMMENT '权限标识，例如 system:user:list',
                          icon VARCHAR(100) DEFAULT NULL COMMENT '菜单图标',
                          order_num INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
                          visible TINYINT NOT NULL DEFAULT 0 COMMENT '是否显示：0显示 1隐藏',
                          status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常 1停用',
                          is_frame TINYINT NOT NULL DEFAULT 1 COMMENT '是否外链：0是 1否',
                          is_cache TINYINT NOT NULL DEFAULT 0 COMMENT '是否缓存：0缓存 1不缓存',
                          create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
                          create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
                          update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
                          remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
                          PRIMARY KEY (id),
                          KEY idx_parent_id (parent_id),
                          KEY idx_perms (perms),
                          KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

CREATE TABLE sys_post (
                          id BIGINT NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
                          post_code VARCHAR(64) NOT NULL COMMENT '岗位编码',
                          post_name VARCHAR(50) NOT NULL COMMENT '岗位名称',
                          post_sort INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
                          status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常 1停用',
                          create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
                          create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
                          update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
                          remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
                          PRIMARY KEY (id),
                          UNIQUE KEY uk_post_code (post_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='岗位表';

CREATE TABLE sys_user_role (
                               id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                               user_id BIGINT NOT NULL COMMENT '用户ID',
                               role_id BIGINT NOT NULL COMMENT '角色ID',
                               create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
                               create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
                               update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
                               remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
                               PRIMARY KEY (id),
                               UNIQUE KEY uk_user_role (user_id, role_id),
                               KEY idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

CREATE TABLE sys_role_menu (
                               id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                               role_id BIGINT NOT NULL COMMENT '角色ID',
                               menu_id BIGINT NOT NULL COMMENT '菜单ID',
                               create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
                               create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
                               update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
                               remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
                               PRIMARY KEY (id),
                               UNIQUE KEY uk_role_menu (role_id, menu_id),
                               KEY idx_menu_id (menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

CREATE TABLE sys_user_post (
                               id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                               user_id BIGINT NOT NULL COMMENT '用户ID',
                               post_id BIGINT NOT NULL COMMENT '岗位ID',
                               create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
                               create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
                               update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
                               remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
                               PRIMARY KEY (id),
                               UNIQUE KEY uk_user_post (user_id, post_id),
                               KEY idx_post_id (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户岗位关联表';

CREATE TABLE sys_dict_type (
                               id BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典类型ID',
                               dict_name VARCHAR(100) NOT NULL COMMENT '字典名称',
                               dict_type VARCHAR(100) NOT NULL COMMENT '字典类型，例如 sys_user_status',
                               status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常 1停用',
                               create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
                               create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
                               update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
                               remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
                               PRIMARY KEY (id),
                               UNIQUE KEY uk_dict_type (dict_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典类型表';

CREATE TABLE sys_dict_data (
                               id BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典数据ID',
                               dict_type VARCHAR(100) NOT NULL COMMENT '字典类型',
                               dict_label VARCHAR(100) NOT NULL COMMENT '字典标签',
                               dict_value VARCHAR(100) NOT NULL COMMENT '字典键值',
                               dict_sort INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
                               css_class VARCHAR(100) DEFAULT NULL COMMENT '样式属性',
                               list_class VARCHAR(100) DEFAULT NULL COMMENT '表格回显样式',
                               is_default TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认：0否 1是',
                               status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常 1停用',
                               create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
                               create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
                               update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
                               remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
                               PRIMARY KEY (id),
                               KEY idx_dict_type (dict_type),
                               KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典数据表';

CREATE TABLE sys_login_log (
                               id BIGINT NOT NULL AUTO_INCREMENT COMMENT '登录日志ID',
                               username VARCHAR(50) DEFAULT NULL COMMENT '登录账号',
                               ipaddr VARCHAR(64) DEFAULT NULL COMMENT '登录IP',
                               login_location VARCHAR(255) DEFAULT NULL COMMENT '登录地点',
                               browser VARCHAR(100) DEFAULT NULL COMMENT '浏览器',
                               os VARCHAR(100) DEFAULT NULL COMMENT '操作系统',
                               status TINYINT NOT NULL DEFAULT 0 COMMENT '登录状态：0成功 1失败',
                               msg VARCHAR(500) DEFAULT NULL COMMENT '提示消息',
                               login_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
                               create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
                               create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
                               update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
                               remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
                               PRIMARY KEY (id),
                               KEY idx_username (username),
                               KEY idx_status (status),
                               KEY idx_login_time (login_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志表';

CREATE TABLE sys_oper_log (
                              id BIGINT NOT NULL AUTO_INCREMENT COMMENT '操作日志ID',
                              title VARCHAR(100) DEFAULT NULL COMMENT '模块标题',
                              business_type VARCHAR(50) DEFAULT NULL COMMENT '业务类型：新增 修改 删除 查询 导出 登录等',
                              method VARCHAR(255) DEFAULT NULL COMMENT '方法名称',
                              request_method VARCHAR(20) DEFAULT NULL COMMENT '请求方式',
                              operator_type VARCHAR(50) DEFAULT NULL COMMENT '操作类别：后台用户 手机端用户 定时任务等',
                              oper_name VARCHAR(50) DEFAULT NULL COMMENT '操作人员',
                              dept_name VARCHAR(100) DEFAULT NULL COMMENT '部门名称',
                              oper_url VARCHAR(255) DEFAULT NULL COMMENT '请求URL',
                              oper_ip VARCHAR(64) DEFAULT NULL COMMENT '主机地址',
                              oper_location VARCHAR(255) DEFAULT NULL COMMENT '操作地点',
                              oper_param TEXT DEFAULT NULL COMMENT '请求参数',
                              json_result TEXT DEFAULT NULL COMMENT '返回参数',
                              status TINYINT NOT NULL DEFAULT 0 COMMENT '操作状态：0正常 1异常',
                              error_msg TEXT DEFAULT NULL COMMENT '错误消息',
                              oper_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
                              cost_time BIGINT DEFAULT NULL COMMENT '消耗时间，毫秒',
                              create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
                              create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
                              update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
                              remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
                              PRIMARY KEY (id),
                              KEY idx_oper_name (oper_name),
                              KEY idx_status (status),
                              KEY idx_oper_time (oper_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

CREATE TABLE sys_file (
                          id BIGINT NOT NULL AUTO_INCREMENT COMMENT '文件ID',
                          file_name VARCHAR(255) NOT NULL COMMENT '存储文件名',
                          original_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
                          file_type VARCHAR(100) DEFAULT NULL COMMENT '文件类型',
                          file_size BIGINT NOT NULL DEFAULT 0 COMMENT '文件大小，字节',
                          storage_type VARCHAR(50) NOT NULL DEFAULT 'LOCAL' COMMENT '存储类型：LOCAL本地 MINIO对象存储',
                          bucket_name VARCHAR(100) DEFAULT NULL COMMENT '存储桶名称',
                          object_name VARCHAR(255) DEFAULT NULL COMMENT '对象存储路径',
                          url VARCHAR(500) DEFAULT NULL COMMENT '访问地址',
                          md5 VARCHAR(64) DEFAULT NULL COMMENT '文件MD5',
                          upload_user_id BIGINT DEFAULT NULL COMMENT '上传用户ID',
                          status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常 1禁用',
                          create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
                          create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
                          update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
                          remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
                          PRIMARY KEY (id),
                          KEY idx_upload_user_id (upload_user_id),
                          KEY idx_md5 (md5),
                          KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件表';

INSERT INTO sys_dept
(id, parent_id, ancestors, dept_name, dept_code, order_num, leader, phone, email, status, create_by, remark)
VALUES
    (1, 0, '0', '平台管理部', 'PLATFORM', 1, 'admin', NULL, NULL, 0, 'system', '系统初始化部门');

INSERT INTO sys_role
(id, role_name, role_key, role_sort, data_scope, status, create_by, remark)
VALUES
    (1, '超级管理员', 'admin', 1, 1, 0, 'system', '系统初始化角色');

INSERT INTO sys_post
(id, post_code, post_name, post_sort, status, create_by, remark)
VALUES
    (1, 'admin', '系统管理员', 1, 0, 'system', '系统初始化岗位');

INSERT INTO sys_user
(id, dept_id, username, nickname, password_hash, user_type, email, phone, gender, status, create_by, remark)
VALUES
    (1, 1, 'admin', '系统管理员', '{todo}generate-bcrypt-in-s0-05', 'SYSTEM', 'admin@example.com', NULL, 0, 0, 'system', '初始化管理员账号，S0-05接登录时生成BCrypt密码');

INSERT INTO sys_menu
(id, parent_id, menu_name, menu_type, path, component, perms, icon, order_num, visible, status, create_by, remark)
VALUES
    (1, 0, '系统管理', 'M', '/system', NULL, NULL, 'setting', 1, 0, 0, 'system', '系统管理目录'),
    (2, 1, '用户管理', 'C', 'user', 'system/user/index', 'system:user:list', 'user', 1, 0, 0, 'system', '用户管理菜单'),
    (3, 1, '角色管理', 'C', 'role', 'system/role/index', 'system:role:list', 'peoples', 2, 0, 0, 'system', '角色管理菜单'),
    (4, 1, '菜单管理', 'C', 'menu', 'system/menu/index', 'system:menu:list', 'tree-table', 3, 0, 0, 'system', '菜单管理菜单'),
    (5, 2, '用户查询', 'F', NULL, NULL, 'system:user:query', NULL, 1, 0, 0, 'system', '用户查询按钮'),
    (6, 2, '用户新增', 'F', NULL, NULL, 'system:user:add', NULL, 2, 0, 0, 'system', '用户新增按钮'),
    (7, 2, '用户修改', 'F', NULL, NULL, 'system:user:edit', NULL, 3, 0, 0, 'system', '用户修改按钮'),
    (8, 2, '用户删除', 'F', NULL, NULL, 'system:user:delete', NULL, 4, 0, 0, 'system', '用户删除按钮');

INSERT INTO sys_user_role
(user_id, role_id, create_by, remark)
VALUES
    (1, 1, 'system', 'admin绑定超级管理员角色');

INSERT INTO sys_user_post
(user_id, post_id, create_by, remark)
VALUES
    (1, 1, 'system', 'admin绑定系统管理员岗位');

INSERT INTO sys_role_menu
(role_id, menu_id, create_by, remark)
SELECT 1, id, 'system', '超级管理员拥有全部初始化菜单'
FROM sys_menu;

INSERT INTO sys_dict_type
(id, dict_name, dict_type, status, create_by, remark)
VALUES
    (1, '系统状态', 'sys_status', 0, 'system', '通用启停状态'),
    (2, '用户性别', 'sys_user_gender', 0, 'system', '用户性别字典'),
    (3, '菜单类型', 'sys_menu_type', 0, 'system', '菜单类型字典');

INSERT INTO sys_dict_data
(dict_type, dict_label, dict_value, dict_sort, is_default, status, create_by, remark)
VALUES
    ('sys_status', '正常', '0', 1, 1, 0, 'system', '正常状态'),
    ('sys_status', '停用', '1', 2, 0, 0, 'system', '停用状态'),
    ('sys_user_gender', '未知', '0', 1, 1, 0, 'system', '未知性别'),
    ('sys_user_gender', '男', '1', 2, 0, 0, 'system', '男性'),
    ('sys_user_gender', '女', '2', 3, 0, 0, 'system', '女性'),
    ('sys_menu_type', '目录', 'M', 1, 0, 0, 'system', '目录'),
    ('sys_menu_type', '菜单', 'C', 2, 0, 0, 'system', '菜单'),
    ('sys_menu_type', '按钮', 'F', 3, 0, 0, 'system', '按钮');

CREATE TABLE IF NOT EXISTS mine_device (
                                           id BIGINT NOT NULL AUTO_INCREMENT COMMENT '设备ID',
                                           device_code VARCHAR(64) NOT NULL COMMENT '设备编码',
    device_name VARCHAR(100) NOT NULL COMMENT '设备名称',
    device_type VARCHAR(50) NOT NULL COMMENT '设备类型',
    area_name VARCHAR(100) DEFAULT NULL COMMENT '所属区域',
    location VARCHAR(200) DEFAULT NULL COMMENT '安装位置',
    manufacturer VARCHAR(100) DEFAULT NULL COMMENT '生产厂家',
    model VARCHAR(100) DEFAULT NULL COMMENT '设备型号',
    install_date DATE DEFAULT NULL COMMENT '安装日期',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常 1停用 2维修 3故障',
    last_maintenance_time DATETIME DEFAULT NULL COMMENT '最近维护时间',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_device_code (device_code),
    KEY idx_device_type (device_type),
    KEY idx_area_name (area_name),
    KEY idx_status (status)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智能矿山设备台账表';

CREATE TABLE IF NOT EXISTS mine_sensor (
                                           id BIGINT NOT NULL AUTO_INCREMENT COMMENT '传感器ID',
                                           sensor_code VARCHAR(64) NOT NULL COMMENT '传感器编码',
    sensor_name VARCHAR(100) NOT NULL COMMENT '传感器名称',
    sensor_type VARCHAR(50) NOT NULL COMMENT '传感器类型',
    device_id BIGINT DEFAULT NULL COMMENT '关联设备ID',
    area_name VARCHAR(100) DEFAULT NULL COMMENT '所属区域',
    location VARCHAR(200) DEFAULT NULL COMMENT '安装位置',
    unit VARCHAR(20) DEFAULT NULL COMMENT '计量单位',
    min_value DECIMAL(12,4) DEFAULT NULL COMMENT '正常最小值',
    max_value DECIMAL(12,4) DEFAULT NULL COMMENT '正常最大值',
    alarm_threshold DECIMAL(12,4) DEFAULT NULL COMMENT '报警阈值',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常 1停用 2故障',
    last_report_time DATETIME DEFAULT NULL COMMENT '最近上报时间',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_sensor_code (sensor_code),
    KEY idx_device_id (device_id),
    KEY idx_sensor_type (sensor_type),
    KEY idx_area_name (area_name),
    KEY idx_status (status)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智能矿山传感器台账表';

INSERT IGNORE INTO mine_device (
    device_code,
    device_name,
    device_type,
    area_name,
    location,
    manufacturer,
    model,
    install_date,
    status,
    last_maintenance_time,
    create_by,
    create_time,
    remark
) VALUES
(
    'DEV-FAN-001',
    '主通风机一号',
    'VENTILATION_FAN',
    '一采区',
    '一采区主通风硐室',
    '山西矿山装备厂',
    'FBD-2x55',
    '2024-03-01',
    0,
    '2026-06-01 09:00:00',
    'admin',
    NOW(),
    '用于一采区主通风'
),
(
    'DEV-PUMP-001',
    '中央水泵一号',
    'WATER_PUMP',
    '中央泵房',
    '中央泵房一号位',
    '太原泵业设备有限公司',
    'MD280-43x5',
    '2024-05-10',
    0,
    '2026-06-10 10:00:00',
    'admin',
    NOW(),
    '用于井下排水'
),
(
    'DEV-BELT-001',
    '主运输皮带一号',
    'BELT_CONVEYOR',
    '主运输巷',
    '主运输巷入口',
    '山西煤机装备有限公司',
    'DSJ100',
    '2024-07-15',
    2,
    '2026-06-15 14:00:00',
    'admin',
    NOW(),
    '当前处于计划检修状态'
);

INSERT IGNORE INTO mine_sensor (
    sensor_code,
    sensor_name,
    sensor_type,
    device_id,
    area_name,
    location,
    unit,
    min_value,
    max_value,
    alarm_threshold,
    status,
    last_report_time,
    create_by,
    create_time,
    remark
) VALUES
(
    'SEN-GAS-001',
    '一采区瓦斯传感器一号',
    'GAS',
    (SELECT id FROM mine_device WHERE device_code = 'DEV-FAN-001' LIMIT 1),
    '一采区',
    '一采区回风巷',
    '%',
    0.0000,
    1.0000,
    1.0000,
    0,
    '2026-06-27 10:00:00',
    'admin',
    NOW(),
    '瓦斯浓度监测'
),
(
    'SEN-TEMP-001',
    '中央泵房温度传感器一号',
    'TEMPERATURE',
    (SELECT id FROM mine_device WHERE device_code = 'DEV-PUMP-001' LIMIT 1),
    '中央泵房',
    '中央泵房一号水泵旁',
    '℃',
    0.0000,
    60.0000,
    60.0000,
    0,
    '2026-06-27 10:00:00',
    'admin',
    NOW(),
    '设备环境温度监测'
),
(
    'SEN-VIB-001',
    '主运输皮带振动传感器一号',
    'VIBRATION',
    (SELECT id FROM mine_device WHERE device_code = 'DEV-BELT-001' LIMIT 1),
    '主运输巷',
    '主运输皮带机头',
    'mm/s',
    0.0000,
    8.0000,
    8.0000,
    0,
    '2026-06-27 10:00:00',
    'admin',
    NOW(),
    '皮带机振动监测'
);
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


CREATE TABLE IF NOT EXISTS mine_alarm_rule (
                                               id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                               rule_code VARCHAR(64) NOT NULL COMMENT '规则编码',
    rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
    sensor_type VARCHAR(64) NOT NULL COMMENT '传感器类型，例如 GAS、TEMPERATURE、VIBRATION',
    compare_operator VARCHAR(10) NOT NULL COMMENT '比较符号：GT大于，GE大于等于，LT小于，LE小于等于，EQ等于',
    threshold_value DECIMAL(18,4) NOT NULL COMMENT '规则阈值',
    alarm_level TINYINT NOT NULL DEFAULT 1 COMMENT '告警级别：1一般，2重要，3严重',
    alarm_title VARCHAR(100) NOT NULL COMMENT '告警标题',
    alarm_content VARCHAR(500) DEFAULT NULL COMMENT '告警内容模板',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0启用，1停用',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_rule_code (rule_code),
    KEY idx_sensor_type (sensor_type),
    KEY idx_alarm_level (alarm_level),
    KEY idx_status (status)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智能矿山告警规则表';

CREATE TABLE IF NOT EXISTS mine_alarm_event (
                                                id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                event_code VARCHAR(64) NOT NULL COMMENT '告警事件编码',
    rule_id BIGINT NOT NULL COMMENT '告警规则ID，逻辑关联 mine_alarm_rule.id',
    rule_code VARCHAR(64) NOT NULL COMMENT '告警规则编码',
    rule_name VARCHAR(100) NOT NULL COMMENT '告警规则名称',
    sensor_data_id BIGINT NOT NULL COMMENT '传感器数据ID，逻辑关联 mine_sensor_data.id',
    sensor_id BIGINT NOT NULL COMMENT '传感器ID，逻辑关联 mine_sensor.id',
    sensor_code VARCHAR(64) NOT NULL COMMENT '传感器编码',
    sensor_name VARCHAR(100) NOT NULL COMMENT '传感器名称',
    sensor_type VARCHAR(64) NOT NULL COMMENT '传感器类型',
    device_id BIGINT DEFAULT NULL COMMENT '所属设备ID，逻辑关联 mine_device.id',
    area_name VARCHAR(100) DEFAULT NULL COMMENT '所属区域',
    location VARCHAR(200) DEFAULT NULL COMMENT '安装位置',
    data_value DECIMAL(18,4) NOT NULL COMMENT '触发告警的采集值',
    threshold_value DECIMAL(18,4) NOT NULL COMMENT '触发告警的规则阈值',
    compare_operator VARCHAR(10) NOT NULL COMMENT '比较符号：GT大于，GE大于等于，LT小于，LE小于等于，EQ等于',
    alarm_level TINYINT NOT NULL DEFAULT 1 COMMENT '告警级别：1一般，2重要，3严重',
    alarm_title VARCHAR(100) NOT NULL COMMENT '告警标题',
    alarm_content VARCHAR(500) DEFAULT NULL COMMENT '告警内容',
    alarm_time DATETIME NOT NULL COMMENT '告警时间',
    handle_status TINYINT NOT NULL DEFAULT 0 COMMENT '处理状态：0未处理，1已确认，2已关闭',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0有效，1无效',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_event_code (event_code),
    UNIQUE KEY uk_rule_data (rule_id, sensor_data_id),
    KEY idx_rule_id (rule_id),
    KEY idx_sensor_data_id (sensor_data_id),
    KEY idx_sensor_id (sensor_id),
    KEY idx_sensor_code (sensor_code),
    KEY idx_sensor_type (sensor_type),
    KEY idx_area_name (area_name),
    KEY idx_alarm_level (alarm_level),
    KEY idx_alarm_time (alarm_time),
    KEY idx_handle_status (handle_status),
    KEY idx_status (status)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智能矿山告警事件表';

INSERT INTO mine_alarm_rule (
    rule_code,
    rule_name,
    sensor_type,
    compare_operator,
    threshold_value,
    alarm_level,
    alarm_title,
    alarm_content,
    status,
    create_by,
    create_time,
    deleted,
    remark
) VALUES
      (
          'ALM-RULE-GAS-001',
          '瓦斯浓度超限告警',
          'GAS',
          'GE',
          1.0000,
          3,
          '瓦斯浓度超限',
          '瓦斯传感器采集值达到或超过阈值，存在安全风险。',
          0,
          'system',
          NOW(),
          0,
          'M1-04 初始化告警规则'
      ),
      (
          'ALM-RULE-TEMP-001',
          '温度过高告警',
          'TEMPERATURE',
          'GE',
          40.0000,
          2,
          '温度过高',
          '温度传感器采集值达到或超过阈值，请关注设备运行状态。',
          0,
          'system',
          NOW(),
          0,
          'M1-04 初始化告警规则'
      ),
      (
          'ALM-RULE-VIB-001',
          '振动过高告警',
          'VIBRATION',
          'GE',
          8.0000,
          2,
          '设备振动过高',
          '振动传感器采集值达到或超过阈值，设备可能存在异常。',
          0,
          'system',
          NOW(),
          0,
          'M1-04 初始化告警规则'
      )
    ON DUPLICATE KEY UPDATE
                         rule_name = VALUES(rule_name),
                         sensor_type = VALUES(sensor_type),
                         compare_operator = VALUES(compare_operator),
                         threshold_value = VALUES(threshold_value),
                         alarm_level = VALUES(alarm_level),
                         alarm_title = VALUES(alarm_title),
                         alarm_content = VALUES(alarm_content),
                         status = VALUES(status),
                         update_time = NOW();


CREATE TABLE IF NOT EXISTS mine_work_order (
                                               id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                               work_order_code VARCHAR(64) NOT NULL COMMENT '工单编码',
    alarm_event_id BIGINT NOT NULL COMMENT '告警事件ID',
    event_code VARCHAR(64) DEFAULT NULL COMMENT '告警事件编码',
    alarm_level TINYINT DEFAULT NULL COMMENT '告警级别：1一般，2重要，3严重',
    work_order_title VARCHAR(200) NOT NULL COMMENT '工单标题',
    work_order_content VARCHAR(1000) DEFAULT NULL COMMENT '工单内容',
    device_id BIGINT DEFAULT NULL COMMENT '设备ID',
    sensor_id BIGINT DEFAULT NULL COMMENT '传感器ID',
    sensor_code VARCHAR(64) DEFAULT NULL COMMENT '传感器编码',
    sensor_name VARCHAR(100) DEFAULT NULL COMMENT '传感器名称',
    sensor_type VARCHAR(64) DEFAULT NULL COMMENT '传感器类型',
    area_name VARCHAR(100) DEFAULT NULL COMMENT '所属区域',
    location VARCHAR(200) DEFAULT NULL COMMENT '安装位置',
    order_status TINYINT NOT NULL DEFAULT 0 COMMENT '工单状态：0待处理，1处理中，2已处理，3已关闭',
    handler_user_id BIGINT DEFAULT NULL COMMENT '处理人ID',
    handler_username VARCHAR(64) DEFAULT NULL COMMENT '处理人用户名',
    handle_time DATETIME DEFAULT NULL COMMENT '处理时间',
    handle_result VARCHAR(1000) DEFAULT NULL COMMENT '处理结果',
    close_user_id BIGINT DEFAULT NULL COMMENT '关闭人ID',
    close_username VARCHAR(64) DEFAULT NULL COMMENT '关闭人用户名',
    close_time DATETIME DEFAULT NULL COMMENT '关闭时间',
    close_result VARCHAR(1000) DEFAULT NULL COMMENT '关闭结果',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0有效，1无效',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_work_order_code (work_order_code),
    UNIQUE KEY uk_alarm_event_id (alarm_event_id),
    KEY idx_event_code (event_code),
    KEY idx_sensor_code (sensor_code),
    KEY idx_sensor_type (sensor_type),
    KEY idx_area_name (area_name),
    KEY idx_alarm_level (alarm_level),
    KEY idx_order_status (order_status),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智能矿山工单表';


CREATE TABLE IF NOT EXISTS mine_maintenance_task (
                                                     id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                     task_code VARCHAR(64) NOT NULL COMMENT '维护任务编码',
    device_id BIGINT NOT NULL COMMENT '设备ID',
    device_code VARCHAR(64) DEFAULT NULL COMMENT '设备编码',
    device_name VARCHAR(100) DEFAULT NULL COMMENT '设备名称',
    device_type VARCHAR(64) DEFAULT NULL COMMENT '设备类型',
    area_name VARCHAR(100) DEFAULT NULL COMMENT '所属区域',
    location VARCHAR(200) DEFAULT NULL COMMENT '安装位置',
    health_score INT DEFAULT NULL COMMENT '生成任务时设备健康分',
    risk_level INT DEFAULT NULL COMMENT '风险等级：0健康，1关注，2风险，3高危',
    risk_level_name VARCHAR(32) DEFAULT NULL COMMENT '风险等级名称',
    task_title VARCHAR(200) NOT NULL COMMENT '任务标题',
    task_content VARCHAR(1000) DEFAULT NULL COMMENT '任务内容',
    task_type INT NOT NULL DEFAULT 1 COMMENT '任务类型：0例行维护，1预测性维护',
    task_source INT NOT NULL DEFAULT 1 COMMENT '任务来源：0手动创建，1设备健康评分',
    task_status INT NOT NULL DEFAULT 0 COMMENT '任务状态：0待安排，1待执行，2处理中，3已关闭',
    priority INT NOT NULL DEFAULT 1 COMMENT '优先级：0低，1中，2高，3紧急',
    plan_start_time DATETIME DEFAULT NULL COMMENT '计划开始时间',
    plan_end_time DATETIME DEFAULT NULL COMMENT '计划结束时间',
    maintainer_user_id BIGINT DEFAULT NULL COMMENT '维护人员用户ID',
    maintainer_username VARCHAR(64) DEFAULT NULL COMMENT '维护人员用户名',
    handle_time DATETIME DEFAULT NULL COMMENT '处理时间',
    handle_result VARCHAR(1000) DEFAULT NULL COMMENT '处理结果',
    close_time DATETIME DEFAULT NULL COMMENT '关闭时间',
    close_result VARCHAR(1000) DEFAULT NULL COMMENT '关闭结果',
    status INT NOT NULL DEFAULT 0 COMMENT '状态：0有效，1无效',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_task_code (task_code),
    KEY idx_device_id (device_id),
    KEY idx_device_code (device_code),
    KEY idx_device_type (device_type),
    KEY idx_area_name (area_name),
    KEY idx_risk_level (risk_level),
    KEY idx_task_status (task_status),
    KEY idx_priority (priority),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智能矿山预测性维护任务表';

CREATE TABLE IF NOT EXISTS aiops_resource (
                                              id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                              resource_code VARCHAR(64) NOT NULL COMMENT '资源编码',
    resource_name VARCHAR(100) NOT NULL COMMENT '资源名称',
    resource_type VARCHAR(32) NOT NULL COMMENT '资源类型：SERVER服务器、DATABASE数据库、MIDDLEWARE中间件、NETWORK网络设备',
    ip_addr VARCHAR(64) DEFAULT NULL COMMENT 'IP地址',
    port INT DEFAULT NULL COMMENT '端口',
    env_type VARCHAR(32) DEFAULT NULL COMMENT '环境类型：DEV开发、TEST测试、PROD生产',
    system_name VARCHAR(100) DEFAULT NULL COMMENT '所属系统',
    owner_name VARCHAR(64) DEFAULT NULL COMMENT '负责人',
    collect_enabled TINYINT NOT NULL DEFAULT 0 COMMENT '是否启用采集：0启用，1停止',
    last_collect_time DATETIME DEFAULT NULL COMMENT '最后采集时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常，1停用，2异常',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_resource_code (resource_code),
    KEY idx_resource_type (resource_type),
    KEY idx_ip_addr (ip_addr),
    KEY idx_env_type (env_type),
    KEY idx_system_name (system_name),
    KEY idx_collect_enabled (collect_enabled),
    KEY idx_status (status)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AIOps资源台账表';

INSERT IGNORE INTO aiops_resource (
    resource_code,
    resource_name,
    resource_type,
    ip_addr,
    port,
    env_type,
    system_name,
    owner_name,
    collect_enabled,
    last_collect_time,
    status,
    create_by,
    create_time,
    remark
) VALUES
('AIOPS-SERVER-001', '政务云应用服务器一号', 'SERVER', '10.10.1.11', 22, 'PROD', '政务云统一门户', 'aiops_engineer', 0, NOW(), 0, 'admin', NOW(), 'A2-02 初始化服务器资源'),
('AIOPS-SERVER-002', '运营商业务服务器一号', 'SERVER', '10.10.1.12', 22, 'PROD', '运营商云网监控平台', 'aiops_engineer', 0, NOW(), 0, 'admin', NOW(), 'A2-02 初始化服务器资源'),
('AIOPS-MYSQL-001', '核心业务 MySQL 数据库', 'DATABASE', '10.10.2.21', 3306, 'PROD', '核心业务系统', 'dba_admin', 0, NOW(), 0, 'admin', NOW(), 'A2-02 初始化数据库资源'),
('AIOPS-REDIS-001', '统一缓存 Redis 中间件', 'MIDDLEWARE', '10.10.3.31', 6379, 'PROD', '统一缓存平台', 'middleware_admin', 0, NOW(), 0, 'admin', NOW(), 'A2-02 初始化中间件资源'),
('AIOPS-SWITCH-001', '核心交换机一号', 'NETWORK', '10.10.4.41', 161, 'PROD', '数据中心网络', 'network_admin', 0, NOW(), 0, 'admin', NOW(), 'A2-02 初始化网络设备资源');

CREATE TABLE IF NOT EXISTS aiops_metric_data (
                                                 id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                 resource_id BIGINT NOT NULL COMMENT '资源ID，逻辑关联 aiops_resource.id',
                                                 resource_code VARCHAR(64) NOT NULL COMMENT '资源编码',
    resource_name VARCHAR(100) NOT NULL COMMENT '资源名称',
    resource_type VARCHAR(32) NOT NULL COMMENT '资源类型：SERVER / DATABASE / MIDDLEWARE / NETWORK',
    ip_addr VARCHAR(64) DEFAULT NULL COMMENT '资源IP地址',
    metric_code VARCHAR(64) NOT NULL COMMENT '指标编码，例如 CPU_USAGE',
    metric_name VARCHAR(100) NOT NULL COMMENT '指标名称，例如 CPU使用率',
    metric_type VARCHAR(32) NOT NULL COMMENT '指标类型：CPU / MEMORY / DISK / NETWORK / MYSQL / REDIS',
    metric_value DECIMAL(18,4) NOT NULL COMMENT '指标值',
    metric_unit VARCHAR(32) DEFAULT NULL COMMENT '指标单位，例如 %、ms、MB、count',
    threshold_value DECIMAL(18,4) DEFAULT NULL COMMENT '告警阈值',
    alarm_flag TINYINT NOT NULL DEFAULT 0 COMMENT '是否触发告警：0否，1是',
    collect_time DATETIME NOT NULL COMMENT '采集时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常，1异常',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_resource_id (resource_id),
    KEY idx_resource_code (resource_code),
    KEY idx_resource_type (resource_type),
    KEY idx_ip_addr (ip_addr),
    KEY idx_metric_code (metric_code),
    KEY idx_metric_type (metric_type),
    KEY idx_collect_time (collect_time),
    KEY idx_alarm_flag (alarm_flag),
    KEY idx_status (status)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AIOps指标采集数据表';


CREATE TABLE IF NOT EXISTS aiops_alert_rule (
                                                id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                rule_code VARCHAR(64) NOT NULL COMMENT '规则编码',
    rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
    resource_type VARCHAR(32) NOT NULL COMMENT '资源类型：SERVER/DATABASE/MIDDLEWARE/NETWORK',
    metric_type VARCHAR(32) NOT NULL COMMENT '指标类型：CPU/MEMORY/DISK/NETWORK/MYSQL/REDIS',
    compare_operator VARCHAR(16) NOT NULL COMMENT '比较符：GT/GE/LT/LE/EQ',
    threshold_value DECIMAL(18,4) NOT NULL COMMENT '阈值',
    alert_level TINYINT NOT NULL COMMENT '告警级别：1一般，2重要，3严重',
    alert_title VARCHAR(200) NOT NULL COMMENT '告警标题',
    alert_content VARCHAR(500) DEFAULT NULL COMMENT '告警内容',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0启用，1停用',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_rule_code (rule_code),
    KEY idx_resource_type (resource_type),
    KEY idx_metric_type (metric_type),
    KEY idx_alert_level (alert_level),
    KEY idx_status (status)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AIOps告警规则表';

CREATE TABLE IF NOT EXISTS aiops_alert_event (
                                                 id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                 event_code VARCHAR(64) NOT NULL COMMENT '事件编码',
    rule_id BIGINT NOT NULL COMMENT '规则ID',
    rule_code VARCHAR(64) NOT NULL COMMENT '规则编码',
    rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
    metric_data_id BIGINT NOT NULL COMMENT '指标数据ID',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    resource_code VARCHAR(64) NOT NULL COMMENT '资源编码',
    resource_name VARCHAR(100) NOT NULL COMMENT '资源名称',
    resource_type VARCHAR(32) NOT NULL COMMENT '资源类型',
    ip_addr VARCHAR(64) DEFAULT NULL COMMENT 'IP地址',
    metric_code VARCHAR(64) NOT NULL COMMENT '指标编码',
    metric_name VARCHAR(100) NOT NULL COMMENT '指标名称',
    metric_type VARCHAR(32) NOT NULL COMMENT '指标类型',
    metric_value DECIMAL(18,4) NOT NULL COMMENT '指标值',
    threshold_value DECIMAL(18,4) NOT NULL COMMENT '阈值',
    compare_operator VARCHAR(16) NOT NULL COMMENT '比较符',
    alert_level TINYINT NOT NULL COMMENT '告警级别：1一般，2重要，3严重',
    alert_title VARCHAR(200) NOT NULL COMMENT '告警标题',
    alert_content VARCHAR(500) DEFAULT NULL COMMENT '告警内容',
    alert_time DATETIME NOT NULL COMMENT '告警时间',
    handle_status TINYINT NOT NULL DEFAULT 0 COMMENT '处理状态：0未处理，1已派单，2已关闭',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0有效，1无效',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_event_code (event_code),
    UNIQUE KEY uk_rule_metric_data (rule_id, metric_data_id),
    KEY idx_rule_id (rule_id),
    KEY idx_metric_data_id (metric_data_id),
    KEY idx_resource_id (resource_id),
    KEY idx_resource_code (resource_code),
    KEY idx_resource_type (resource_type),
    KEY idx_metric_type (metric_type),
    KEY idx_alert_level (alert_level),
    KEY idx_alert_time (alert_time),
    KEY idx_handle_status (handle_status),
    KEY idx_status (status)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AIOps告警事件表';

CREATE TABLE IF NOT EXISTS aiops_work_order (
                                                id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                work_order_code VARCHAR(64) NOT NULL COMMENT '工单编码',
    alert_event_id BIGINT NOT NULL COMMENT '告警事件ID',
    event_code VARCHAR(64) NOT NULL COMMENT '告警事件编码',
    alert_level TINYINT NOT NULL COMMENT '告警级别：1一般，2重要，3严重',
    work_order_title VARCHAR(200) NOT NULL COMMENT '工单标题',
    work_order_content VARCHAR(500) DEFAULT NULL COMMENT '工单内容',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    resource_code VARCHAR(64) NOT NULL COMMENT '资源编码',
    resource_name VARCHAR(100) NOT NULL COMMENT '资源名称',
    resource_type VARCHAR(32) NOT NULL COMMENT '资源类型',
    ip_addr VARCHAR(64) DEFAULT NULL COMMENT 'IP地址',
    metric_code VARCHAR(64) NOT NULL COMMENT '指标编码',
    metric_name VARCHAR(100) NOT NULL COMMENT '指标名称',
    metric_type VARCHAR(32) NOT NULL COMMENT '指标类型',
    order_status TINYINT NOT NULL DEFAULT 0 COMMENT '工单状态：0待处理，1处理中，2已处理，3已关闭',
    handler_user_id BIGINT DEFAULT NULL COMMENT '处理人用户ID',
    handler_username VARCHAR(64) DEFAULT NULL COMMENT '处理人用户名',
    handle_time DATETIME DEFAULT NULL COMMENT '处理时间',
    handle_result VARCHAR(1000) DEFAULT NULL COMMENT '处理结果',
    close_user_id BIGINT DEFAULT NULL COMMENT '关闭人用户ID',
    close_username VARCHAR(64) DEFAULT NULL COMMENT '关闭人用户名',
    close_time DATETIME DEFAULT NULL COMMENT '关闭时间',
    close_result VARCHAR(1000) DEFAULT NULL COMMENT '关闭结果',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0有效，1无效',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_work_order_code (work_order_code),
    UNIQUE KEY uk_alert_event_id (alert_event_id),
    KEY idx_event_code (event_code),
    KEY idx_resource_id (resource_id),
    KEY idx_resource_code (resource_code),
    KEY idx_resource_type (resource_type),
    KEY idx_metric_type (metric_type),
    KEY idx_alert_level (alert_level),
    KEY idx_order_status (order_status),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AIOps运维工单表';

INSERT INTO aiops_alert_rule (rule_code, rule_name, resource_type, metric_type, compare_operator, threshold_value, alert_level, alert_title, alert_content, status, create_by, create_time, deleted, remark)
SELECT 'AIOPS-RULE-CPU-001', '服务器CPU使用率过高', 'SERVER', 'CPU', 'GE', 80.0000, 2, '服务器CPU使用率过高', '服务器CPU使用率超过阈值，请检查进程负载和系统资源。', 0, 'system', NOW(), 0, 'A2-04 初始化规则'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM aiops_alert_rule WHERE rule_code = 'AIOPS-RULE-CPU-001');
INSERT INTO aiops_alert_rule (rule_code, rule_name, resource_type, metric_type, compare_operator, threshold_value, alert_level, alert_title, alert_content, status, create_by, create_time, deleted, remark)
SELECT 'AIOPS-RULE-MEM-001', '服务器内存使用率过高', 'SERVER', 'MEMORY', 'GE', 85.0000, 2, '服务器内存使用率过高', '服务器内存使用率超过阈值，请检查内存占用和缓存情况。', 0, 'system', NOW(), 0, 'A2-04 初始化规则'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM aiops_alert_rule WHERE rule_code = 'AIOPS-RULE-MEM-001');
INSERT INTO aiops_alert_rule (rule_code, rule_name, resource_type, metric_type, compare_operator, threshold_value, alert_level, alert_title, alert_content, status, create_by, create_time, deleted, remark)
SELECT 'AIOPS-RULE-DISK-001', '服务器磁盘使用率过高', 'SERVER', 'DISK', 'GE', 90.0000, 3, '服务器磁盘使用率过高', '服务器磁盘使用率超过严重阈值，请尽快清理磁盘或扩容。', 0, 'system', NOW(), 0, 'A2-04 初始化规则'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM aiops_alert_rule WHERE rule_code = 'AIOPS-RULE-DISK-001');
INSERT INTO aiops_alert_rule (rule_code, rule_name, resource_type, metric_type, compare_operator, threshold_value, alert_level, alert_title, alert_content, status, create_by, create_time, deleted, remark)
SELECT 'AIOPS-RULE-MYSQL-001', 'MySQL响应时间过高', 'DATABASE', 'MYSQL', 'GE', 200.0000, 3, 'MySQL响应时间过高', '数据库响应时间超过阈值，请检查慢 SQL、连接数和磁盘 IO。', 0, 'system', NOW(), 0, 'A2-04 初始化规则'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM aiops_alert_rule WHERE rule_code = 'AIOPS-RULE-MYSQL-001');
INSERT INTO aiops_alert_rule (rule_code, rule_name, resource_type, metric_type, compare_operator, threshold_value, alert_level, alert_title, alert_content, status, create_by, create_time, deleted, remark)
SELECT 'AIOPS-RULE-REDIS-001', 'Redis内存使用率过高', 'MIDDLEWARE', 'REDIS', 'GE', 80.0000, 2, 'Redis内存使用率过高', 'Redis 内存使用率超过阈值，请检查缓存热点和淘汰策略。', 0, 'system', NOW(), 0, 'A2-04 初始化规则'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM aiops_alert_rule WHERE rule_code = 'AIOPS-RULE-REDIS-001');
INSERT INTO aiops_alert_rule (rule_code, rule_name, resource_type, metric_type, compare_operator, threshold_value, alert_level, alert_title, alert_content, status, create_by, create_time, deleted, remark)
SELECT 'AIOPS-RULE-NET-001', '网络设备流量异常', 'NETWORK', 'NETWORK', 'GE', 80.0000, 2, '网络设备流量异常', '网络设备流量指标超过阈值，请检查链路带宽和设备状态。', 0, 'system', NOW(), 0, 'A2-04 初始化规则'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM aiops_alert_rule WHERE rule_code = 'AIOPS-RULE-NET-001');

CREATE TABLE IF NOT EXISTS aiops_root_cause_record (
                                                       id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                       analysis_code VARCHAR(64) NOT NULL COMMENT '分析编码',
    alert_event_id BIGINT NOT NULL COMMENT '告警事件ID',
    event_code VARCHAR(64) NOT NULL COMMENT '告警事件编码',
    resource_id BIGINT DEFAULT NULL COMMENT '资源ID',
    resource_code VARCHAR(64) DEFAULT NULL COMMENT '资源编码',
    resource_name VARCHAR(100) DEFAULT NULL COMMENT '资源名称',
    resource_type VARCHAR(32) DEFAULT NULL COMMENT '资源类型',
    ip_addr VARCHAR(64) DEFAULT NULL COMMENT 'IP地址',
    root_cause_type VARCHAR(64) NOT NULL COMMENT '根因类型',
    root_cause_desc VARCHAR(500) DEFAULT NULL COMMENT '根因描述',
    evidence TEXT COMMENT '分析证据',
    suggestion TEXT COMMENT '处理建议',
    confidence_score INT NOT NULL DEFAULT 0 COMMENT '置信度，0到100',
    analysis_time DATETIME NOT NULL COMMENT '分析时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态，0有效，1无效',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除，0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_analysis_code (analysis_code),
    UNIQUE KEY uk_alert_event_id (alert_event_id),
    KEY idx_event_code (event_code),
    KEY idx_resource_id (resource_id),
    KEY idx_resource_code (resource_code),
    KEY idx_resource_type (resource_type),
    KEY idx_ip_addr (ip_addr),
    KEY idx_root_cause_type (root_cause_type),
    KEY idx_analysis_time (analysis_time),
    KEY idx_status (status)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AIOps根因分析记录表';

CREATE TABLE IF NOT EXISTS risk_transaction (
                                                id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                transaction_no VARCHAR(64) NOT NULL COMMENT '交易流水号',
    account_no VARCHAR(64) NOT NULL COMMENT '账户号',
    customer_id BIGINT DEFAULT NULL COMMENT '客户ID',
    customer_name VARCHAR(100) DEFAULT NULL COMMENT '客户姓名',
    merchant_id VARCHAR(64) DEFAULT NULL COMMENT '商户ID',
    merchant_name VARCHAR(100) DEFAULT NULL COMMENT '商户名称',
    transaction_type VARCHAR(32) NOT NULL COMMENT '交易类型：PAYMENT支付，TRANSFER转账，WITHDRAW取现，CONSUME消费',
    channel VARCHAR(32) NOT NULL COMMENT '交易渠道：APP，WEB，ATM，POS',
    amount DECIMAL(18,2) NOT NULL COMMENT '交易金额',
    currency VARCHAR(16) NOT NULL DEFAULT 'CNY' COMMENT '币种',
    ip_addr VARCHAR(64) DEFAULT NULL COMMENT '交易IP',
    device_id VARCHAR(128) DEFAULT NULL COMMENT '设备ID',
    location VARCHAR(100) DEFAULT NULL COMMENT '交易地点',
    transaction_time DATETIME NOT NULL COMMENT '交易时间',
    transaction_status INT NOT NULL DEFAULT 0 COMMENT '交易状态：0成功，1失败，2处理中',
    risk_flag INT NOT NULL DEFAULT 0 COMMENT '风险标记：0未命中风险，1命中风险',
    status INT NOT NULL DEFAULT 0 COMMENT '状态：0正常，1异常',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_transaction_no (transaction_no),
    KEY idx_account_no (account_no),
    KEY idx_customer_id (customer_id),
    KEY idx_customer_name (customer_name),
    KEY idx_merchant_id (merchant_id),
    KEY idx_transaction_type (transaction_type),
    KEY idx_channel (channel),
    KEY idx_transaction_time (transaction_time),
    KEY idx_transaction_status (transaction_status),
    KEY idx_risk_flag (risk_flag),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='银行风控交易流水表';

CREATE TABLE IF NOT EXISTS risk_rule (
                                         id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                         rule_code VARCHAR(64) NOT NULL COMMENT '规则编码',
    rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
    rule_type VARCHAR(32) NOT NULL COMMENT '规则类型：AMOUNT/FREQUENCY/LOCATION/DEVICE/TIME/BLACKLIST',
    condition_type VARCHAR(32) NOT NULL COMMENT '条件类型',
    compare_operator VARCHAR(32) NOT NULL COMMENT '比较操作符：GT/GTE/LT/LTE/EQ/CONTAINS/NIGHT',
    threshold_value DECIMAL(18, 2) DEFAULT NULL COMMENT '阈值',
    risk_level TINYINT NOT NULL DEFAULT 1 COMMENT '风险等级：1低风险，2中风险，3高风险',
    risk_score INT NOT NULL DEFAULT 0 COMMENT '风险分',
    rule_content VARCHAR(500) DEFAULT NULL COMMENT '规则内容',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0启用，1停用',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_rule_code (rule_code),
    KEY idx_rule_type (rule_type),
    KEY idx_risk_level (risk_level),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='风控规则表';

CREATE TABLE IF NOT EXISTS risk_rule_hit (
                                             id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                             hit_code VARCHAR(64) NOT NULL COMMENT '命中编码',
    transaction_id BIGINT NOT NULL COMMENT '交易ID',
    transaction_no VARCHAR(64) NOT NULL COMMENT '交易流水号',
    account_no VARCHAR(64) NOT NULL COMMENT '账号',
    customer_id VARCHAR(64) NOT NULL COMMENT '客户ID',
    customer_name VARCHAR(100) NOT NULL COMMENT '客户姓名',
    rule_id BIGINT NOT NULL COMMENT '规则ID',
    rule_code VARCHAR(64) NOT NULL COMMENT '规则编码',
    rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
    rule_type VARCHAR(32) NOT NULL COMMENT '规则类型',
    hit_value VARCHAR(200) DEFAULT NULL COMMENT '命中值',
    threshold_value DECIMAL(18, 2) DEFAULT NULL COMMENT '命中阈值',
    risk_level TINYINT NOT NULL DEFAULT 1 COMMENT '风险等级：1低风险，2中风险，3高风险',
    risk_score INT NOT NULL DEFAULT 0 COMMENT '风险分',
    hit_time DATETIME NOT NULL COMMENT '命中时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0未处理，1已处理',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_hit_code (hit_code),
    UNIQUE KEY uk_transaction_rule (transaction_id, rule_id),
    KEY idx_transaction_id (transaction_id),
    KEY idx_transaction_no (transaction_no),
    KEY idx_account_no (account_no),
    KEY idx_customer_id (customer_id),
    KEY idx_rule_id (rule_id),
    KEY idx_rule_code (rule_code),
    KEY idx_rule_type (rule_type),
    KEY idx_risk_level (risk_level),
    KEY idx_status (status),
    KEY idx_hit_time (hit_time),
    KEY idx_create_time (create_time)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='风控规则命中表';

INSERT INTO risk_rule (
    rule_code,
    rule_name,
    rule_type,
    condition_type,
    compare_operator,
    threshold_value,
    risk_level,
    risk_score,
    rule_content,
    status,
    create_by,
    remark
) VALUES
      ('RISK_AMOUNT_10000', '大额交易规则', 'AMOUNT', 'amount', 'GTE', 10000.00, 2, 30, '交易金额大于等于10000元', 0, 'system', 'R3-03 默认规则：大额交易'),
      ('RISK_FREQ_3_10MIN', '十分钟高频交易规则', 'FREQUENCY', 'account_no', 'GTE', 3.00, 3, 40, '同一账号10分钟内交易次数大于等于3次', 0, 'system', 'R3-03 默认规则：高频交易'),
      ('RISK_LOCATION_OVERSEA', '异地交易规则', 'LOCATION', 'location', 'CONTAINS', NULL, 3, 35, '境外,香港,澳门,港澳', 0, 'system', 'R3-03 默认规则：异地交易'),
      ('RISK_DEVICE_ABNORMAL', '异常设备规则', 'DEVICE', 'device_id', 'CONTAINS', NULL, 2, 25, 'UNKNOWN,ROOT,ROOTED,EMULATOR,VIRTUAL', 0, 'system', 'R3-03 默认规则：异常设备'),
      ('RISK_TIME_NIGHT', '夜间交易规则', 'TIME', 'transaction_time', 'NIGHT', 23.00, 1, 15, '23:00-06:00 夜间交易', 0, 'system', 'R3-03 默认规则：夜间交易'),
      ('RISK_BLACKLIST_CUSTOMER', '黑名单客户规则', 'BLACKLIST', 'customer_id/account_no', 'CONTAINS', NULL, 3, 60, 'CUST_BLACK_001,ACC_BLACK_001', 0, 'system', 'R3-03 默认规则：黑名单')
    ON DUPLICATE KEY UPDATE
                         rule_name = VALUES(rule_name),
                         rule_type = VALUES(rule_type),
                         condition_type = VALUES(condition_type),
                         compare_operator = VALUES(compare_operator),
                         threshold_value = VALUES(threshold_value),
                         risk_level = VALUES(risk_level),
                         risk_score = VALUES(risk_score),
                         rule_content = VALUES(rule_content),
                         status = VALUES(status),
                         update_by = 'system',
                         update_time = CURRENT_TIMESTAMP,
                         remark = VALUES(remark);
CREATE TABLE IF NOT EXISTS risk_review_order (
                                                 id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                 review_order_code VARCHAR(64) NOT NULL COMMENT '审核单编号',
    transaction_id BIGINT NOT NULL COMMENT '交易ID',
    transaction_no VARCHAR(64) NOT NULL COMMENT '交易流水号',
    account_no VARCHAR(64) DEFAULT NULL COMMENT '账户号',
    customer_id VARCHAR(64) DEFAULT NULL COMMENT '客户ID',
    customer_name VARCHAR(100) DEFAULT NULL COMMENT '客户姓名',
    merchant_id VARCHAR(64) DEFAULT NULL COMMENT '商户ID',
    merchant_name VARCHAR(128) DEFAULT NULL COMMENT '商户名称',
    transaction_type VARCHAR(32) DEFAULT NULL COMMENT '交易类型',
    channel VARCHAR(32) DEFAULT NULL COMMENT '交易渠道',
    amount DECIMAL(18,2) DEFAULT NULL COMMENT '交易金额',
    currency VARCHAR(16) DEFAULT 'CNY' COMMENT '币种',
    total_score INT NOT NULL DEFAULT 0 COMMENT '风险总分',
    risk_level TINYINT NOT NULL DEFAULT 1 COMMENT '风险等级：1低风险，2中风险，3高风险',
    risk_result VARCHAR(32) NOT NULL DEFAULT 'REVIEW' COMMENT '风险结果：PASS放行，REVIEW人工审核，REJECT拒绝',
    review_status TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态：0待审核，1审核通过，2审核拒绝',
    reviewer_user_id BIGINT DEFAULT NULL COMMENT '审核人用户ID',
    reviewer_username VARCHAR(64) DEFAULT NULL COMMENT '审核人用户名',
    review_time DATETIME DEFAULT NULL COMMENT '审核时间',
    review_result VARCHAR(500) DEFAULT NULL COMMENT '审核意见',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常，1异常',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_review_order_code (review_order_code),
    UNIQUE KEY uk_transaction_id (transaction_id),
    KEY idx_transaction_no (transaction_no),
    KEY idx_account_no (account_no),
    KEY idx_customer_id (customer_id),
    KEY idx_risk_level (risk_level),
    KEY idx_risk_result (risk_result),
    KEY idx_review_status (review_status),
    KEY idx_create_time (create_time)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='风险人工审核单表';
CREATE TABLE IF NOT EXISTS datahub_datasource (
                                                  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                  datasource_code VARCHAR(64) NOT NULL COMMENT '数据源编码',
    datasource_name VARCHAR(100) NOT NULL COMMENT '数据源名称',
    datasource_type VARCHAR(32) NOT NULL COMMENT '数据源类型：MYSQL、POSTGRESQL、ORACLE、SQLSERVER、API',
    jdbc_url VARCHAR(500) NOT NULL COMMENT 'JDBC连接地址',
    host VARCHAR(100) DEFAULT NULL COMMENT '主机地址',
    port INT DEFAULT NULL COMMENT '端口号',
    database_name VARCHAR(100) DEFAULT NULL COMMENT '数据库名或服务名',
    username VARCHAR(100) DEFAULT NULL COMMENT '用户名',
    password VARCHAR(255) DEFAULT NULL COMMENT '密码，学习阶段暂明文保存，生产环境应加密',
    owner_name VARCHAR(50) DEFAULT NULL COMMENT '负责人',
    env_type VARCHAR(32) DEFAULT 'DEV' COMMENT '环境类型：DEV、TEST、PROD',
    test_status TINYINT DEFAULT 0 COMMENT '最近连接测试状态：0未测试，1成功，2失败',
    last_test_time DATETIME DEFAULT NULL COMMENT '最近连接测试时间',
    status TINYINT DEFAULT 0 COMMENT '状态：0正常，1停用',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_datasource_code (datasource_code),
    KEY idx_datasource_type (datasource_type),
    KEY idx_host (host),
    KEY idx_database_name (database_name),
    KEY idx_env_type (env_type),
    KEY idx_test_status (test_status),
    KEY idx_status (status)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据治理-数据源管理表';

INSERT INTO datahub_datasource (
    datasource_code,
    datasource_name,
    datasource_type,
    jdbc_url,
    host,
    port,
    database_name,
    username,
    password,
    owner_name,
    env_type,
    test_status,
    status,
    create_by,
    remark
) VALUES
      (
          'DATAHUB-MYSQL-001',
          '企业主数据 MySQL 数据源',
          'MYSQL',
          'jdbc:mysql://localhost:3306/enterprise_scaffold?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true',
          'localhost',
          3306,
          'enterprise_scaffold',
          'root',
          '',
          '数据治理管理员',
          'DEV',
          0,
          0,
          'admin',
          'D4-02 初始化数据源：本地开发 MySQL'
      ),
      (
          'DATAHUB-MYSQL-002',
          '政务共享交换 MySQL 数据源',
          'MYSQL',
          'jdbc:mysql://10.10.20.21:3306/gov_exchange?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true',
          '10.10.20.21',
          3306,
          'gov_exchange',
          'gov_reader',
          '',
          '政务数据负责人',
          'TEST',
          0,
          0,
          'admin',
          'D4-02 初始化数据源：政务共享交换测试库'
      ),
      (
          'DATAHUB-MYSQL-003',
          '能源企业生产数据 MySQL 数据源',
          'MYSQL',
          'jdbc:mysql://10.10.30.31:3306/energy_prod?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true',
          '10.10.30.31',
          3306,
          'energy_prod',
          'energy_reader',
          '',
          '能源数据负责人',
          'PROD',
          0,
          0,
          'admin',
          'D4-02 初始化数据源：能源企业生产数据源'
      )
    ON DUPLICATE KEY UPDATE
                         datasource_name = VALUES(datasource_name),
                         datasource_type = VALUES(datasource_type),
                         jdbc_url = VALUES(jdbc_url),
                         host = VALUES(host),
                         port = VALUES(port),
                         database_name = VALUES(database_name),
                         username = VALUES(username),
                         owner_name = VALUES(owner_name),
                         env_type = VALUES(env_type),
                         status = VALUES(status),
                         update_time = CURRENT_TIMESTAMP,
                         remark = VALUES(remark);

CREATE TABLE IF NOT EXISTS datahub_metadata_table (
                                                      id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                      data_source_id BIGINT NOT NULL COMMENT '数据源ID',
                                                      data_source_name VARCHAR(100) NOT NULL COMMENT '数据源名称',
    schema_name VARCHAR(100) NOT NULL COMMENT '数据库名或Schema名',
    table_name VARCHAR(100) NOT NULL COMMENT '表名',
    table_comment VARCHAR(500) DEFAULT NULL COMMENT '表注释',
    table_type VARCHAR(50) NOT NULL COMMENT '表类型：TABLE表，VIEW视图',
    row_count BIGINT DEFAULT NULL COMMENT '表行数',
    collect_batch_no VARCHAR(64) NOT NULL COMMENT '采集批次号',
    collect_time DATETIME NOT NULL COMMENT '采集时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常，1异常',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_data_source_id (data_source_id),
    KEY idx_schema_table (schema_name, table_name),
    KEY idx_collect_batch_no (collect_batch_no),
    KEY idx_status (status)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据治理-元数据表信息表';

CREATE TABLE IF NOT EXISTS datahub_metadata_column (
                                                       id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                       table_id BIGINT NOT NULL COMMENT '元数据表ID',
                                                       data_source_id BIGINT NOT NULL COMMENT '数据源ID',
                                                       data_source_name VARCHAR(100) NOT NULL COMMENT '数据源名称',
    schema_name VARCHAR(100) NOT NULL COMMENT '数据库名或Schema名',
    table_name VARCHAR(100) NOT NULL COMMENT '表名',
    column_name VARCHAR(100) NOT NULL COMMENT '字段名',
    column_comment VARCHAR(500) DEFAULT NULL COMMENT '字段注释',
    column_type VARCHAR(100) DEFAULT NULL COMMENT '完整字段类型',
    data_type VARCHAR(100) DEFAULT NULL COMMENT '基础数据类型',
    column_length INT DEFAULT NULL COMMENT '字段长度',
    numeric_precision INT DEFAULT NULL COMMENT '数字精度',
    numeric_scale INT DEFAULT NULL COMMENT '小数位数',
    nullable_flag TINYINT NOT NULL DEFAULT 1 COMMENT '是否允许为空：0否，1是',
    primary_key_flag TINYINT NOT NULL DEFAULT 0 COMMENT '是否主键：0否，1是',
    column_default VARCHAR(500) DEFAULT NULL COMMENT '默认值',
    ordinal_position INT DEFAULT NULL COMMENT '字段顺序',
    collect_batch_no VARCHAR(64) NOT NULL COMMENT '采集批次号',
    collect_time DATETIME NOT NULL COMMENT '采集时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常，1异常',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_table_id (table_id),
    KEY idx_data_source_id (data_source_id),
    KEY idx_schema_table (schema_name, table_name),
    KEY idx_column_name (column_name),
    KEY idx_collect_batch_no (collect_batch_no),
    KEY idx_primary_key_flag (primary_key_flag),
    KEY idx_status (status)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据治理-元数据字段信息表';

CREATE TABLE IF NOT EXISTS datahub_metadata_collect_log (
                                                            id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                            collect_batch_no VARCHAR(64) NOT NULL COMMENT '采集批次号',
    data_source_id BIGINT NOT NULL COMMENT '数据源ID',
    data_source_name VARCHAR(100) NOT NULL COMMENT '数据源名称',
    collect_status TINYINT NOT NULL COMMENT '采集状态：0成功，1失败',
    table_total INT NOT NULL DEFAULT 0 COMMENT '采集表数量',
    column_total INT NOT NULL DEFAULT 0 COMMENT '采集字段数量',
    error_msg TEXT DEFAULT NULL COMMENT '失败原因',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME DEFAULT NULL COMMENT '结束时间',
    cost_time BIGINT DEFAULT NULL COMMENT '耗时毫秒',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常，1异常',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_collect_batch_no (collect_batch_no),
    KEY idx_data_source_id (data_source_id),
    KEY idx_collect_status (collect_status),
    KEY idx_start_time (start_time),
    KEY idx_status (status)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据治理-元数据采集日志表';

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

CREATE TABLE IF NOT EXISTS datahub_api_publish (
                                                   id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                   api_code VARCHAR(64) NOT NULL COMMENT 'API编码',
    api_name VARCHAR(100) NOT NULL COMMENT 'API名称',
    datasource_id BIGINT NULL COMMENT '数据源ID',
    datasource_code VARCHAR(64) NULL COMMENT '数据源编码',
    datasource_name VARCHAR(100) NULL COMMENT '数据源名称',
    table_id BIGINT NOT NULL COMMENT '元数据表ID',
    table_code VARCHAR(100) NULL COMMENT '表编码',
    table_name VARCHAR(100) NOT NULL COMMENT '表名',
    table_comment VARCHAR(255) NULL COMMENT '表说明',
    api_path VARCHAR(200) NOT NULL COMMENT 'API访问路径',
    request_method VARCHAR(20) NOT NULL DEFAULT 'GET' COMMENT '请求方式',
    publish_type VARCHAR(30) NOT NULL DEFAULT 'TABLE_PAGE' COMMENT '发布类型：TABLE_PAGE表分页API',
    online_status TINYINT NOT NULL DEFAULT 0 COMMENT '上线状态：0下线，1上线',
    auth_required TINYINT NOT NULL DEFAULT 1 COMMENT '是否需要认证：0否，1是',
    owner_name VARCHAR(64) NULL COMMENT '负责人',
    publish_time DATETIME NULL COMMENT '发布时间',
    last_online_time DATETIME NULL COMMENT '最近上线时间',
    last_offline_time DATETIME NULL COMMENT '最近下线时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0正常，1停用',
    create_by VARCHAR(64) NULL COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NULL COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_api_code (api_code),
    UNIQUE KEY uk_api_path (api_path),
    KEY idx_datasource_id (datasource_id),
    KEY idx_table_id (table_id),
    KEY idx_api_name (api_name),
    KEY idx_online_status (online_status),
    KEY idx_status (status),
    KEY idx_deleted (deleted)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据治理-API共享发布表';

CREATE TABLE IF NOT EXISTS datahub_api_call_log (
                                                    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                    call_code VARCHAR(64) NOT NULL COMMENT '调用流水号',
    api_id BIGINT NOT NULL COMMENT 'API发布ID',
    api_code VARCHAR(64) NOT NULL COMMENT 'API编码',
    api_name VARCHAR(100) NOT NULL COMMENT 'API名称',
    request_path VARCHAR(200) NOT NULL COMMENT '请求路径',
    request_method VARCHAR(20) NOT NULL COMMENT '请求方式',
    caller_ip VARCHAR(64) NULL COMMENT '调用方IP',
    call_status TINYINT NOT NULL DEFAULT 0 COMMENT '调用状态：0成功，1失败',
    error_msg VARCHAR(500) NULL COMMENT '错误信息',
    cost_time BIGINT NULL COMMENT '耗时毫秒',
    call_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '调用时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0有效，1无效',
    create_by VARCHAR(64) NULL COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NULL COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_call_code (call_code),
    KEY idx_api_id (api_id),
    KEY idx_api_code (api_code),
    KEY idx_call_status (call_status),
    KEY idx_call_time (call_time),
    KEY idx_status (status),
    KEY idx_deleted (deleted)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据治理-API调用日志表';
CREATE TABLE IF NOT EXISTS iam_access_log (
                                              id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                              trace_id VARCHAR(64) NULL COMMENT '链路追踪ID',
    request_uri VARCHAR(255) NOT NULL COMMENT '接口路径',
    request_method VARCHAR(20) NOT NULL COMMENT '请求方法',
    module_name VARCHAR(100) NULL DEFAULT 'IAM' COMMENT '模块名称',
    operation_name VARCHAR(100) NULL COMMENT '操作名称',
    user_id BIGINT NULL COMMENT '访问用户ID',
    username VARCHAR(64) NULL COMMENT '访问用户名',
    client_ip VARCHAR(64) NULL COMMENT '客户端IP',
    user_agent VARCHAR(512) NULL COMMENT '浏览器或客户端标识',
    request_params VARCHAR(1000) NULL COMMENT '请求参数摘要',
    response_code INT NULL COMMENT '响应业务码或HTTP码',
    response_msg VARCHAR(255) NULL COMMENT '响应消息摘要',
    access_status TINYINT NOT NULL DEFAULT 0 COMMENT '访问状态：0成功，1失败',
    cost_ms BIGINT NOT NULL DEFAULT 0 COMMENT '接口耗时毫秒',
    access_time DATETIME NOT NULL COMMENT '访问时间',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '业务状态：0有效，1无效',
    create_by VARCHAR(64) NULL COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NULL COMMENT '更新人',
    update_time DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) NULL COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_trace_id (trace_id),
    KEY idx_request_uri (request_uri),
    KEY idx_request_method (request_method),
    KEY idx_username (username),
    KEY idx_client_ip (client_ip),
    KEY idx_access_status (access_status),
    KEY idx_access_time (access_time),
    KEY idx_deleted (deleted)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='IAM接口访问日志表';

INSERT INTO iam_access_log (
    trace_id,
    request_uri,
    request_method,
    module_name,
    operation_name,
    user_id,
    username,
    client_ip,
    user_agent,
    request_params,
    response_code,
    response_msg,
    access_status,
    cost_ms,
    access_time,
    status,
    create_by,
    create_time,
    deleted,
    remark
)
SELECT
    'I5-02-DEMO-0001',
    '/api/iam/health',
    'GET',
    'IAM',
    '模块健康检查',
    1,
    'admin',
    '127.0.0.1',
    'PostmanRuntime',
    '{}',
    0,
    'success',
    0,
    18,
    NOW(),
    0,
    'system',
    NOW(),
    0,
    'I5-02 初始化演示数据'
    WHERE NOT EXISTS (
    SELECT 1 FROM iam_access_log WHERE trace_id = 'I5-02-DEMO-0001'
);

INSERT INTO iam_access_log (
    trace_id,
    request_uri,
    request_method,
    module_name,
    operation_name,
    user_id,
    username,
    client_ip,
    user_agent,
    request_params,
    response_code,
    response_msg,
    access_status,
    cost_ms,
    access_time,
    status,
    create_by,
    create_time,
    deleted,
    remark
)
SELECT
    'I5-02-DEMO-0002',
    '/api/iam/access-logs/page',
    'GET',
    'IAM',
    '接口访问日志分页查询',
    1,
    'admin',
    '127.0.0.1',
    'Mozilla/5.0',
    '{"pageNo":1,"pageSize":10}',
    0,
    'success',
    0,
    32,
    NOW(),
    0,
    'system',
    NOW(),
    0,
    'I5-02 初始化演示数据'
    WHERE NOT EXISTS (
    SELECT 1 FROM iam_access_log WHERE trace_id = 'I5-02-DEMO-0002'
);

CREATE TABLE IF NOT EXISTS iam_login_risk (
                                              id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                              risk_code VARCHAR(64) NOT NULL COMMENT '风险编码',
    username VARCHAR(64) DEFAULT NULL COMMENT '登录用户名',
    client_ip VARCHAR(64) DEFAULT NULL COMMENT '客户端IP',
    risk_type VARCHAR(64) NOT NULL COMMENT '风险类型：LOGIN_FAILED登录失败，SHORT_TIME_FAILED短时间多次失败，ABNORMAL_IP异常IP',
    risk_level TINYINT NOT NULL DEFAULT 1 COMMENT '风险等级：1低风险，2中风险，3高风险',
    fail_count INT NOT NULL DEFAULT 0 COMMENT '失败次数',
    first_fail_time DATETIME DEFAULT NULL COMMENT '首次失败时间',
    last_fail_time DATETIME DEFAULT NULL COMMENT '最近失败时间',
    detect_time DATETIME NOT NULL COMMENT '检测时间',
    handle_status TINYINT NOT NULL DEFAULT 0 COMMENT '处理状态：0未处理，1已确认，2已忽略',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '业务状态：0有效，1无效',
    create_by VARCHAR(64) DEFAULT 'system' COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_risk_code (risk_code),
    KEY idx_username (username),
    KEY idx_client_ip (client_ip),
    KEY idx_risk_type (risk_type),
    KEY idx_risk_level (risk_level),
    KEY idx_handle_status (handle_status),
    KEY idx_detect_time (detect_time),
    KEY idx_deleted (deleted)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='IAM异常登录风险表';

INSERT IGNORE INTO iam_login_risk
(risk_code, username, client_ip, risk_type, risk_level, fail_count, first_fail_time, last_fail_time, detect_time, handle_status, status, create_by, create_time, deleted, remark)
VALUES
('LR-DEMO-20260709-0001', 'admin', '127.0.0.1', 'LOGIN_FAILED', 1, 1, NOW() - INTERVAL 2 HOUR, NOW() - INTERVAL 2 HOUR, NOW(), 0, 0, 'system', NOW(), 0, '演示数据：单次登录失败'),
('LR-DEMO-20260709-0002', 'admin', '192.168.1.20', 'SHORT_TIME_FAILED', 3, 5, NOW() - INTERVAL 8 MINUTE, NOW() - INTERVAL 1 MINUTE, NOW(), 0, 0, 'system', NOW(), 0, '演示数据：短时间多次登录失败'),
('LR-DEMO-20260709-0003', 'test', '10.0.0.8', 'ABNORMAL_IP', 2, 4, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 30 MINUTE, NOW(), 0, 0, 'system', NOW(), 0, '演示数据：异常IP登录失败次数较多');

SELECT
    id,
    risk_code,
    username,
    client_ip,
    risk_type,
    risk_level,
    fail_count,
    detect_time
FROM iam_login_risk
ORDER BY id DESC
    LIMIT 10;
CREATE TABLE IF NOT EXISTS iam_rate_limit_rule (
                                                   id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                   rule_code VARCHAR(64) NOT NULL COMMENT '规则编码',
    rule_name VARCHAR(128) NOT NULL COMMENT '规则名称',
    request_uri VARCHAR(255) NOT NULL COMMENT '接口路径',
    request_method VARCHAR(16) NOT NULL COMMENT '请求方法：GET、POST、PUT、DELETE',
    limit_dimension VARCHAR(32) NOT NULL DEFAULT 'IP' COMMENT '限流维度：GLOBAL 全局，USER 用户，IP 客户端IP',
    limit_window_seconds INT NOT NULL DEFAULT 60 COMMENT '限流时间窗口，单位秒',
    max_requests INT NOT NULL DEFAULT 60 COMMENT '窗口内最大请求次数',
    limit_action VARCHAR(32) NOT NULL DEFAULT 'WARN' COMMENT '命中动作：WARN 仅告警，REJECT 拒绝',
    enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用：0 停用，1 启用',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '业务状态：0 正常，1 禁用',
    create_by VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删除，1 已删除',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_rate_limit_rule_code (rule_code),
    KEY idx_rate_limit_request_uri (request_uri),
    KEY idx_rate_limit_request_method (request_method),
    KEY idx_rate_limit_dimension (limit_dimension),
    KEY idx_rate_limit_enabled (enabled),
    KEY idx_rate_limit_deleted (deleted),
    KEY idx_rate_limit_create_time (create_time)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='IAM接口限流规则表';

INSERT INTO iam_rate_limit_rule (
    rule_code,
    rule_name,
    request_uri,
    request_method,
    limit_dimension,
    limit_window_seconds,
    max_requests,
    limit_action,
    enabled,
    status,
    create_by,
    create_time,
    update_by,
    update_time,
    deleted,
    remark
) VALUES
      ('RL-IAM-LOGIN-RISK-DETECT', '异常登录检测接口限流', '/api/iam/login-risks/detect', 'POST', 'USER', 60, 5, 'WARN', 1, 0, 'admin', NOW(), 'admin', NOW(), 0, '演示规则：同一用户 60 秒内最多模拟 5 次异常登录检测请求'),
      ('RL-IAM-ACCESS-LOG-PAGE', '接口访问日志分页限流', '/api/iam/access-logs/page', 'GET', 'IP', 60, 30, 'WARN', 1, 0, 'admin', NOW(), 'admin', NOW(), 0, '演示规则：同一 IP 60 秒内最多模拟 30 次访问日志分页查询'),
      ('RL-IAM-RATE-LIMIT-PAGE', '限流规则分页接口限流', '/api/iam/rate-limit-rules/page', 'GET', 'IP', 60, 60, 'WARN', 1, 0, 'admin', NOW(), 'admin', NOW(), 0, '演示规则：同一 IP 60 秒内最多模拟 60 次限流规则分页查询')
    ON DUPLICATE KEY UPDATE
                         rule_name = VALUES(rule_name),
                         request_uri = VALUES(request_uri),
                         request_method = VALUES(request_method),
                         limit_dimension = VALUES(limit_dimension),
                         limit_window_seconds = VALUES(limit_window_seconds),
                         max_requests = VALUES(max_requests),
                         limit_action = VALUES(limit_action),
                         enabled = VALUES(enabled),
                         status = VALUES(status),
                         update_by = VALUES(update_by),
                         update_time = NOW(),
                         deleted = 0,
                         remark = VALUES(remark);

CREATE TABLE IF NOT EXISTS `iam_security_policy` (
                                                     `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                     `policy_code` VARCHAR(64) NOT NULL COMMENT '策略编码',
    `policy_name` VARCHAR(100) NOT NULL COMMENT '策略名称',
    `policy_type` VARCHAR(32) NOT NULL COMMENT '策略类型：LOGIN 登录安全，RATE_LIMIT 接口限流，RISK 风险处理，AUDIT 审计保留',
    `policy_level` TINYINT NOT NULL DEFAULT 1 COMMENT '策略等级：1 低，2 中，3 高',
    `policy_value` VARCHAR(128) NOT NULL COMMENT '策略值',
    `policy_unit` VARCHAR(32) DEFAULT NULL COMMENT '策略单位：COUNT 次，SECOND 秒，DAY 天，LEVEL 等级',
    `effective_scope` VARCHAR(32) NOT NULL DEFAULT 'GLOBAL' COMMENT '生效范围：GLOBAL 全局，USER 用户，IP 客户端IP，API 接口',
    `enabled` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用：0 停用，1 启用',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '业务状态：0 正常，1 禁用',
    `create_by` VARCHAR(64) DEFAULT 'system' COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT 'system' COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删除，1 已删除',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_iam_security_policy_code` (`policy_code`),
    KEY `idx_iam_security_policy_type` (`policy_type`),
    KEY `idx_iam_security_policy_level` (`policy_level`),
    KEY `idx_iam_security_policy_enabled` (`enabled`),
    KEY `idx_iam_security_policy_status` (`status`),
    KEY `idx_iam_security_policy_deleted` (`deleted`),
    KEY `idx_iam_security_policy_create_time` (`create_time`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='IAM安全策略配置表';

INSERT INTO `iam_security_policy` (
    `policy_code`,
    `policy_name`,
    `policy_type`,
    `policy_level`,
    `policy_value`,
    `policy_unit`,
    `effective_scope`,
    `enabled`,
    `status`,
    `create_by`,
    `update_by`,
    `remark`
) VALUES
      ('SP-LOGIN-FAIL-THRESHOLD', '登录失败次数阈值', 'LOGIN', 3, '5', 'COUNT', 'USER', 1, 0, 'system', 'system', '同一用户在统计窗口内登录失败达到该次数后应进入异常登录风险检测。'),
      ('SP-LOGIN-FAIL-WINDOW', '登录失败统计窗口', 'LOGIN', 2, '600', 'SECOND', 'USER', 1, 0, 'system', 'system', '异常登录检测使用的登录失败统计时间窗口，单位为秒。'),
      ('SP-RATE-LIMIT-DEFAULT-MAX', '默认接口限流次数', 'RATE_LIMIT', 2, '60', 'COUNT', 'API', 1, 0, 'system', 'system', '接口未配置独立规则时可参考的默认最大请求次数，本阶段只配置策略，不做真实拦截。'),
      ('SP-RATE-LIMIT-DEFAULT-WINDOW', '默认接口限流窗口', 'RATE_LIMIT', 2, '60', 'SECOND', 'API', 1, 0, 'system', 'system', '接口限流默认时间窗口，单位为秒，本阶段只作为策略配置展示。'),
      ('SP-HIGH-RISK-HANDLE-SLA', '高风险处理时限', 'RISK', 3, '1', 'DAY', 'GLOBAL', 1, 0, 'system', 'system', '高风险登录、异常访问或关键安全事件建议在该时限内处理。'),
      ('SP-AUDIT-LOG-RETENTION', '审计日志保留天数', 'AUDIT', 1, '180', 'DAY', 'GLOBAL', 1, 0, 'system', 'system', 'IAM 接口访问日志、登录风险和安全策略操作记录建议保留天数。')
    ON DUPLICATE KEY UPDATE
                         `policy_name` = VALUES(`policy_name`),
                         `policy_type` = VALUES(`policy_type`),
                         `policy_level` = VALUES(`policy_level`),
                         `policy_value` = VALUES(`policy_value`),
                         `policy_unit` = VALUES(`policy_unit`),
                         `effective_scope` = VALUES(`effective_scope`),
                         `enabled` = VALUES(`enabled`),
                         `status` = VALUES(`status`),
                         `update_by` = VALUES(`update_by`),
                         `remark` = VALUES(`remark`),
                         `deleted` = 0;
CREATE TABLE IF NOT EXISTS iam_permission_audit (
                                                    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                    audit_code VARCHAR(64) NOT NULL COMMENT '审计编码',
    audit_type VARCHAR(64) NOT NULL COMMENT '审计类型：USER_ROLE用户角色变更，ROLE_MENU角色菜单变更，USER_STATUS用户状态变更，MENU_PERMISSION菜单权限变更，DATA_SCOPE数据范围变更',
    target_type VARCHAR(64) NOT NULL COMMENT '审计对象类型：USER用户，ROLE角色，MENU菜单，API接口',
    target_id BIGINT NULL COMMENT '审计对象ID',
    target_name VARCHAR(128) NULL COMMENT '审计对象名称',
    change_action VARCHAR(64) NOT NULL COMMENT '变更动作：GRANT授权，REVOKE撤销，UPDATE修改，ENABLE启用，DISABLE停用',
    before_value TEXT NULL COMMENT '变更前内容',
    after_value TEXT NULL COMMENT '变更后内容',
    risk_level VARCHAR(32) NOT NULL DEFAULT 'LOW' COMMENT '风险等级：LOW低，MEDIUM中，HIGH高',
    review_status VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '复核状态：PENDING待复核，REVIEWED已复核，IGNORED已忽略',
    review_by VARCHAR(64) NULL COMMENT '复核人',
    review_time DATETIME NULL COMMENT '复核时间',
    request_ip VARCHAR(64) NULL COMMENT '请求IP',
    operator_name VARCHAR(64) NULL COMMENT '操作人',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '业务状态：0正常，1禁用',
    create_by VARCHAR(64) NULL DEFAULT 'system' COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) NULL COMMENT '更新人',
    update_time DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
    remark VARCHAR(500) NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_iam_permission_audit_code (audit_code),
    KEY idx_iam_permission_audit_type (audit_type),
    KEY idx_iam_permission_target_type (target_type),
    KEY idx_iam_permission_action (change_action),
    KEY idx_iam_permission_risk_level (risk_level),
    KEY idx_iam_permission_review_status (review_status),
    KEY idx_iam_permission_operator (operator_name),
    KEY idx_iam_permission_deleted (deleted),
    KEY idx_iam_permission_create_time (create_time)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='IAM权限审计记录表';

INSERT IGNORE INTO iam_permission_audit
(audit_code, audit_type, target_type, target_id, target_name, change_action, before_value, after_value, risk_level, review_status, request_ip, operator_name, status, create_by, create_time, remark)
VALUES
('PA-USER-ROLE-001', 'USER_ROLE', 'USER', 1, 'admin', 'GRANT', 'roles=[普通用户]', 'roles=[普通用户,系统管理员]', 'HIGH', 'PENDING', '127.0.0.1', 'admin', 0, 'system', NOW(), '初始化权限审计样例：用户被授予管理员角色'),
('PA-USER-ROLE-002', 'USER_ROLE', 'USER', 2, 'demo', 'REVOKE', 'roles=[系统管理员,审计员]', 'roles=[审计员]', 'HIGH', 'PENDING', '127.0.0.1', 'admin', 0, 'system', NOW(), '初始化权限审计样例：用户管理员角色被撤销'),
('PA-ROLE-MENU-001', 'ROLE_MENU', 'ROLE', 1, '系统管理员', 'GRANT', 'menus=[首页]', 'menus=[首页,用户管理,角色管理,菜单管理]', 'MEDIUM', 'REVIEWED', '127.0.0.1', 'admin', 0, 'system', NOW(), '初始化权限审计样例：角色新增菜单权限'),
('PA-MENU-PERM-001', 'MENU_PERMISSION', 'MENU', 100, '权限审计', 'UPDATE', 'perms=iam:audit:view', 'perms=iam:audit:view,iam:audit:review', 'MEDIUM', 'PENDING', '127.0.0.1', 'admin', 0, 'system', NOW(), '初始化权限审计样例：菜单权限标识变化'),
('PA-USER-STATUS-001', 'USER_STATUS', 'USER', 3, 'risk_user', 'DISABLE', 'status=0', 'status=1', 'HIGH', 'PENDING', '127.0.0.1', 'admin', 0, 'system', NOW(), '初始化权限审计样例：高风险用户被停用'),
('PA-DATA-SCOPE-001', 'DATA_SCOPE', 'ROLE', 2, '数据审计员', 'UPDATE', 'scope=全部数据', 'scope=本部门数据', 'LOW', 'IGNORED', '127.0.0.1', 'admin', 0, 'system', NOW(), '初始化权限审计样例：数据范围变更');

ALTER TABLE iam_login_risk
    MODIFY COLUMN handle_status tinyint NOT NULL DEFAULT 0 COMMENT '处理状态：0未处理，1已确认，2已忽略，3已关闭';

ALTER TABLE iam_login_risk
    ADD COLUMN handle_by varchar(64) DEFAULT NULL COMMENT '处理人' AFTER handle_status,
    ADD COLUMN handle_time datetime DEFAULT NULL COMMENT '处理时间' AFTER handle_by,
    ADD COLUMN handle_remark varchar(500) DEFAULT NULL COMMENT '处理备注' AFTER handle_time;

ALTER TABLE iam_login_risk
    ADD INDEX idx_iam_login_risk_handle_status_time (handle_status, handle_time);