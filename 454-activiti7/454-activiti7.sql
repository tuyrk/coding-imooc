CREATE DATABASE IF NOT EXISTS `454-activiti7` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `454-activiti7`;

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 创建用户表
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) DEFAULT NULL COMMENT '姓名',
    `address` VARCHAR(64) DEFAULT NULL COMMENT '联系地址',
    `username` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '账号',
    `password` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '密码',
    `authorities` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色',
    `account_non_expired` TINYINT(1) DEFAULT 1 COMMENT '账户是否过期',
    `account_non_locked` TINYINT(1) DEFAULT 1 COMMENT '账户是否被锁定',
    `credentials_non_expired` TINYINT(1) DEFAULT 1 COMMENT '权限是否过期',
    `enabled` TINYINT(1) DEFAULT 1 COMMENT '用户是否被禁用',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT '用户表';

-- ----------------------------
-- 填充用户表
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admincn', 'beijing', 'admin', '$2a$10$gw46pmsOVYO.smHYQ2jH.OoXoe.lGP8OStDkHNs/E74GqZDL5K7ki', 'ROLE_ACTIVITI_ADMIN', 1, 1, 1, 1);
INSERT INTO `user` VALUES ('2', 'bajiecn', 'shanghang', 'bajie', '$2a$10$gw46pmsOVYO.smHYQ2jH.OoXoe.lGP8OStDkHNs/E74GqZDL5K7ki', 'ROLE_ACTIVITI_USER,GROUP_activitiTeam,g_bajiewukong', 1, 1, 1, 1);
INSERT INTO `user` VALUES ('3', 'wukongcn', 'beijing', 'wukong', '$2a$10$gw46pmsOVYO.smHYQ2jH.OoXoe.lGP8OStDkHNs/E74GqZDL5K7ki', 'ROLE_ACTIVITI_USER,GROUP_activitiTeam', 1, 1, 1, 1);
INSERT INTO `user` VALUES ('4', 'salaboycn', 'beijing', 'salaboy', '$2a$10$gw46pmsOVYO.smHYQ2jH.OoXoe.lGP8OStDkHNs/E74GqZDL5K7ki', 'ROLE_ACTIVITI_USER,GROUP_activitiTeam', 1, 1, 1, 1);

-- ----------------------------
-- 修复Activiti7的M4版本缺失字段Bug
-- ----------------------------
ALTER TABLE ACT_RE_DEPLOYMENT ADD COLUMN PROJECT_RELEASE_VERSION_ VARCHAR(255) DEFAULT NULL;
ALTER TABLE ACT_RE_DEPLOYMENT ADD COLUMN VERSION_ VARCHAR(255) DEFAULT NULL;

-- ----------------------------
-- 动态表单数据存储
-- ----------------------------
DROP TABLE IF EXISTS `formdata`;
CREATE TABLE `formdata`
(
    `PROC_DEF_ID_`   VARCHAR(64)   DEFAULT NULL COMMENT '流程定义ID',
    `PROC_INST_ID_`  VARCHAR(64)   DEFAULT NULL COMMENT '流程实例ID',
    `FORM_KEY_`      VARCHAR(255)  DEFAULT NULL COMMENT '表单KEY',
    `CONTROL_ID_`    VARCHAR(100)  DEFAULT NULL COMMENT '控件ID',
    `CONTROL_VALUE_` VARCHAR(2000) DEFAULT NULL COMMENT '控件值'
) ENGINE = InnoDB COMMENT '表单业务数据';
