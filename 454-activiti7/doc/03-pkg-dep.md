# 第3章 项目从git下载与打包部署

> 很多小伙伴在别的实战课程中的反馈：不知道如何下载源码，在本地或者部署到云等环境中，本章特别采用“倒叙”方式，方便小伙伴正确在仓库中下载源码，并在本地或者云环境部署，感受一下项目再进一步深入学习。

### 3-1 源码下载与运行

> 下载Git源码
>
> 修改数据库连接并运行项目（首次运行会报错）
>
> 运行user.sql创建用户表并补充Activiti7缺失字段

**注**：中央仓库可能没有Activiti7的依赖，所以需添加阿里云的maven仓库配置

- 创建用户表

  ```mysql
  CREATE TABLE IF NOT EXISTS `user`
  (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) DEFAULT NULL COMMENT '姓名',
    `address` VARCHAR(64) DEFAULT NULL COMMENT '联系地址',
    `username` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '账号',
    `password` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '密码',
    `authorities` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '角色',
    `account_non_expired` TINYINT(1) DEFAULT 0 COMMENT '账户是否过期',
    `account_non_locked` TINYINT(1) DEFAULT 0 COMMENT '账户是否被锁定',
    `credentials_non_expired` TINYINT(1) DEFAULT 0 COMMENT '权限是否过期',
    `enabled` TINYINT(1) DEFAULT 0 COMMENT '用户是否被禁用',
    PRIMARY KEY (`id`)
  ) ENGINE = InnoDB COMMENT '用户表';
  ```

  ```mysql
  INSERT INTO `user` VALUES ('1', 'admincn', 'beijing', 'admin', '$2a$10$gw46pmsOVYO.smHYQ2jH.OoXoe.lGP8OStDkHNs/E74GqZDL5K7ki', 'ROLE_ACTIVITI_ADMIN', 1, 1, 1, 1);
  INSERT INTO `user` VALUES ('2', 'bajiecn', 'shanghang', 'bajie', '$2a$10$gw46pmsOVYO.smHYQ2jH.OoXoe.lGP8OStDkHNs/E74GqZDL5K7ki', 'ROLE_ACTIVITI_USER,GROUP_activitiTeam,g_bajiewukong', 1, 1, 1, 1);
  INSERT INTO `user` VALUES ('3', 'wukongcn', 'beijing', 'wukong', '$2a$10$gw46pmsOVYO.smHYQ2jH.OoXoe.lGP8OStDkHNs/E74GqZDL5K7ki', 'ROLE_ACTIVITI_USER,GROUP_activitiTeam', 1, 1, 1, 1);
  INSERT INTO `user` VALUES ('4', 'salaboycn', 'beijing', 'salaboy', '$2a$10$gw46pmsOVYO.smHYQ2jH.OoXoe.lGP8OStDkHNs/E74GqZDL5K7ki', 'ROLE_ACTIVITI_USER,GROUP_activitiTeam', 1, 1, 1, 1);
  ```

- 修复Activiti7的M5版本缺失字段Bug

  ```mysql
  alter table ACT_RE_DEPLOYMENT add column PROJECT_RELEASE_VERSION_ varchar(255) DEFAULT NULL;
  alter table ACT_RE_DEPLOYMENT add column VERSION_ varchar(255) DEFAULT NULL;
  ```

项目首页：http://127.0.0.1:8080/layuimini/page/login-1.html

测试账号：bajie/1、wukong/1

