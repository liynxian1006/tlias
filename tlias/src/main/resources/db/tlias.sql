-- ============================================================
-- Tlias 智能学习辅助系统 - 数据库初始化脚本
-- 执行方式（需 MySQL 8.0）：
--   mysql -u root -p < tlias.sql
--   或在 MySQL 客户端中执行本文件
-- ============================================================

CREATE DATABASE IF NOT EXISTS tlias
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE tlias;

-- 部门表
CREATE TABLE IF NOT EXISTS dept (
    id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID, 主键',
    name        VARCHAR(10) NOT NULL UNIQUE COMMENT '部门名称',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    update_time DATETIME DEFAULT NULL COMMENT '修改时间'
) COMMENT = '部门表';

INSERT INTO dept (id, name, create_time, update_time) VALUES
    (1, '学工部',   '2023-09-25 09:47:40', '2024-07-25 09:47:40'),
    (2, '教研部',   '2023-09-25 09:47:40', '2024-08-09 15:17:04'),
    (3, '咨询部',   '2023-09-25 09:47:40', '2024-07-30 21:26:24'),
    (4, '就业部',   '2023-09-25 09:47:40', '2024-07-25 09:47:40'),
    (5, '人事部',   '2023-09-25 09:47:40', '2024-07-25 09:47:40'),
    (6, '行政部',   '2023-11-30 20:56:37', '2024-07-30 20:56:37')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 员工表
CREATE TABLE IF NOT EXISTS emp (
    id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID, 主键',
    username    VARCHAR(20) NOT NULL UNIQUE COMMENT '用户名',
    password    VARCHAR(20) DEFAULT '123456' COMMENT '密码',
    name        VARCHAR(10) NOT NULL COMMENT '姓名',
    gender      TINYINT UNSIGNED NOT NULL COMMENT '性别, 1 男, 2 女',
    phone       VARCHAR(11) DEFAULT NULL COMMENT '手机号',
    image       VARCHAR(255) DEFAULT NULL COMMENT '图像',
    job         TINYINT UNSIGNED DEFAULT NULL COMMENT '职位, 1 班主任,2 讲师,3 学工主管,4 教研主管,5 咨询师',
    salary      INT UNSIGNED DEFAULT NULL COMMENT '薪资',
    entry_date  DATE DEFAULT NULL COMMENT '入职日期',
    dept_id     INT UNSIGNED DEFAULT NULL COMMENT '部门ID',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    update_time DATETIME DEFAULT NULL COMMENT '修改时间',
    UNIQUE KEY uk_emp_phone (phone),
    CONSTRAINT fk_emp_dept FOREIGN KEY (dept_id) REFERENCES dept (id)
) COMMENT = '员工表';

INSERT INTO emp (id, username, password, name, gender, image, job, salary, entry_date, dept_id, create_time, update_time) VALUES
    (1, 'songjiang',   '123456', '宋江',   1, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/2022-09-02-00-27-53B.jpg', 3, 9000, '2010-01-01', 1, '2022-09-01 23:06:30', '2024-07-25 09:47:40'),
    (2, 'jinyong',     '123456', '金庸',   1, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/2022-09-02-00-27-53B.jpg', 2, 8000, '2015-01-01', 2, '2022-09-01 23:06:30', '2024-07-25 09:47:40'),
    (3, 'zhangwuji',   '123456', '张无忌', 1, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/2022-09-02-00-27-53B.jpg', 2, 6000, '2015-01-01', 2, '2022-09-01 23:06:30', '2024-07-25 09:47:40'),
    (4, 'linpingzhi',  '123456', '林平之', 1, 'https://web-framework.oss-cn-hangzhou.aliyuncs.com/2022-09-03-07-37-38222.jpg', 1, 8000, '2022-09-18', 1, '2022-09-18 10:00:00', '2024-07-25 09:47:40'),
    (5, 'xiaozhao',    '123456', '小昭',   2, 'https://web-65.oss-cn-beijing.aliyuncs.com/da94dc38-f165-480c-b8b7-0b3f4bcd1602.jpg', 3, 8000, '2013-09-05', 1, '2023-04-07 11:16:00', '2024-07-25 09:47:40'),
    (6, 'zcc',         '123456', '周星驰', 1, 'https://web-65.oss-cn-beijing.aliyuncs.com/99c143e9-0241-41f3-bc55-dd5e4e0824f4.jpg', 1, 8000, '2023-04-23', 2, '2023-05-26 17:25:01', '2024-07-25 09:47:40'),
    (7, 'jixiaofu',    '123456', '纪晓芙', 2, NULL, 4, 9000, '2018-03-15', 2, '2023-06-01 17:51:21', '2024-07-25 09:47:40'),
    (8, 'chenyouliang','123456', '陈友谅', 1, NULL, 5, 7000, '2019-07-01', 4, '2023-06-01 17:46:10', '2024-07-25 09:47:40')
ON DUPLICATE KEY UPDATE username = VALUES(username);

-- 员工工作经历表
CREATE TABLE IF NOT EXISTS emp_expr (
    id      INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID, 主键',
    emp_id  INT UNSIGNED NOT NULL COMMENT '员工ID',
    company VARCHAR(50) DEFAULT NULL COMMENT '所在公司',
    job     VARCHAR(50) DEFAULT NULL COMMENT '职位',
    begin   DATE DEFAULT NULL COMMENT '开始时间',
    end     DATE DEFAULT NULL COMMENT '结束时间',
    CONSTRAINT fk_expr_emp FOREIGN KEY (emp_id) REFERENCES emp (id)
) COMMENT = '员工工作经历表';

INSERT INTO emp_expr (emp_id, company, job, begin, end) VALUES
    (2, '中软国际股份有限公司', 'java开发', '2008-06-01', '2014-05-20'),
    (2, '百度科技股份有限公司', '架构师',   '2014-06-01', '2022-08-30'),
    (3, '百度科技股份有限公司', 'java开发', '2012-07-01', '2019-03-03'),
    (3, '阿里巴巴科技股份有限公司', '架构师', '2019-03-15', '2023-03-01');

-- 班级表
CREATE TABLE IF NOT EXISTS clazz (
    id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID, 主键',
    name        VARCHAR(20) NOT NULL COMMENT '班级名称',
    room        VARCHAR(10) DEFAULT NULL COMMENT '班级教室',
    begin_date  DATE DEFAULT NULL COMMENT '开课时间',
    end_date    DATE DEFAULT NULL COMMENT '结课时间',
    master_id   INT UNSIGNED DEFAULT NULL COMMENT '班主任(员工ID)',
    subject     TINYINT UNSIGNED DEFAULT NULL COMMENT '学科, 1:java,2:前端,3:大数据,4:Python,5:Go,6:嵌入式',
    status      VARCHAR(10) DEFAULT NULL COMMENT '未开班、已开班、已结课',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    update_time DATETIME DEFAULT NULL COMMENT '修改时间'
) COMMENT = '班级表';

INSERT INTO clazz (id, name, room, begin_date, end_date, master_id, subject, status, create_time, update_time) VALUES
    (1, '黄埔一期',       '101', '2023-01-01', '2023-07-01', 4, 1, '已结课', '2023-06-01 17:51:21', '2023-06-01 17:51:21'),
    (2, 'JavaEE就业166期','105', '2023-07-20', '2024-02-20', 7, 1, '已开班', '2023-06-01 17:46:10', '2023-06-01 17:46:10'),
    (3, '黄埔四期',       '209', '2023-08-01', '2024-02-15', 7, 1, '已开班', '2023-06-01 17:51:21', '2023-06-01 17:51:21')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 学员表
CREATE TABLE IF NOT EXISTS student (
    id              INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID, 主键',
    name            VARCHAR(10) NOT NULL COMMENT '姓名',
    no              VARCHAR(20) NOT NULL UNIQUE COMMENT '学号',
    gender          TINYINT UNSIGNED DEFAULT NULL COMMENT '性别, 1 男, 2 女',
    phone           VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    degree          TINYINT UNSIGNED DEFAULT NULL COMMENT '学历, 1 初中,2 高中,3 大专,4 本科,5 硕士,6 博士',
    id_card         CHAR(18) DEFAULT NULL COMMENT '身份证号',
    is_college      TINYINT UNSIGNED DEFAULT NULL COMMENT '是否是院校学生, 1 是, 0 否',
    address         VARCHAR(100) DEFAULT NULL COMMENT '联系地址',
    graduation_date DATE DEFAULT NULL COMMENT '毕业时间',
    violation_count INT UNSIGNED DEFAULT 0 COMMENT '违纪次数',
    violation_score INT UNSIGNED DEFAULT 0 COMMENT '违纪扣分',
    clazz_id        INT UNSIGNED DEFAULT NULL COMMENT '班级ID',
    create_time     DATETIME DEFAULT NULL COMMENT '创建时间',
    update_time     DATETIME DEFAULT NULL COMMENT '修改时间',
    CONSTRAINT fk_student_clazz FOREIGN KEY (clazz_id) REFERENCES clazz (id)
) COMMENT = '学员表';

INSERT INTO student (id, name, no, gender, phone, degree, id_card, is_college, address, graduation_date, violation_count, violation_score, clazz_id, create_time, update_time) VALUES
    (1, 'Lily',  '2023001003', 2, '13309230912', 4, '110090110090110090', 0, '回龙观东大街110号', '2020-07-01', 2, 5, 1, '2023-06-01 18:35:23', '2023-06-01 19:37:42'),
    (2, 'Jerry', '2023001004', 1, '15309232323', 4, '110090110090110090', 0, '回龙观东大街110号', '2020-07-01', 1, 2, 1, '2023-06-01 18:35:48', '2023-06-01 19:37:35'),
    (3, 'Locos', '2023001010', 1, '13712345678', 5, '110090110090110090', 0, '回龙观东大街110号', '2020-07-01', 0, 0, 2, '2023-06-04 18:27:27', '2023-06-04 18:27:27'),
    (4, '张三',  '2023001001', 1, '13800001111', 1, '110090110090110091', 1, '昌平回龙观',       '2024-07-01', 0, 0, 3, '2023-06-04 18:27:27', '2023-06-04 18:27:27')
ON DUPLICATE KEY UPDATE no = VALUES(no);

-- 操作日志表
CREATE TABLE IF NOT EXISTS log (
    id              INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID, 主键',
    operate_emp_id  INT UNSIGNED DEFAULT NULL COMMENT '操作人ID',
    operate_time    DATETIME DEFAULT NULL COMMENT '操作时间',
    class_name      VARCHAR(100) DEFAULT NULL COMMENT '类名',
    method_name     VARCHAR(50) DEFAULT NULL COMMENT '方法名',
    method_params   VARCHAR(2000) DEFAULT NULL COMMENT '方法请求参数',
    return_value    VARCHAR(2000) DEFAULT NULL COMMENT '返回值',
    cost_time       BIGINT DEFAULT NULL COMMENT '执行耗时(ms)',
    operate_emp_name VARCHAR(20) DEFAULT NULL COMMENT '操作人姓名'
) COMMENT = '操作日志表';

-- 员工操作日志表（演示事务传播 REQUIRES_NEW：新增员工无论成败都落库）
CREATE TABLE IF NOT EXISTS emp_log (
    id               INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID, 主键',
    operate_emp_name VARCHAR(30) DEFAULT NULL COMMENT '操作人',
    operate_time     DATETIME   DEFAULT NULL COMMENT '操作时间',
    info             VARCHAR(255) DEFAULT NULL COMMENT '操作信息'
) COMMENT = '员工操作日志表(事务传播演示)';
