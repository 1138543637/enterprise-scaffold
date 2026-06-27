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