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
