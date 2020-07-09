-- 数据库
create database imooc_356;

-- 用户信息表
create table if not exists `imooc_356`.`homepage_user` (
  `id` bigint(20) not null auto_increment comment '自增ID',
  `username` varchar(128) not null default '' comment '用户名',
  `email` varchar(128) not null default '' comment '用户邮箱',
  `create_time` datetime not null default '1970-01-01 08:00:00' comment '创建时间',
  `update_time` datetime not null default '1970-01-01 08:00:00' comment '更新时间',
  primary key (`id`),
  unique key `key_username` (`username`)
) engine=InnoDB auto_increment = 1 default charset=utf8 row_format=compact comment='用户信息表';

-- 用户课程表
create table if not exists `imooc_356`.`homepage_user_course` (
  `id` bigint(20) not null auto_increment comment '自增ID',
  `user_id` bigint(20) not null default 0 comment '用户ID',
  `course_id` bigint(20) not null default 0 comment '课程ID',
  `create_time` datetime not null default '1970-01-01 08:00:00' comment '创建时间',
  `update_time` datetime not null default '1970-01-01 08:00:00' comment '更新时间',
  primary key (`id`),
  unique key `key_user_course` (`user_id`, `course_id`)
) engine=InnoDB auto_increment = 1 default charset=utf8 row_format=compact comment='用户课程表';

-- 课程信息表
create table if not exists `imooc_356`.`homepage_course` (
  `id` bigint(20) not null auto_increment comment '自增ID',
  `course_name` varchar(128) not null default '' comment '课程名称',
  `course_type` varchar(128) not null default '' comment '课程类型',
  `course_icon` varchar(128) not null default '' comment '课程图片',
  `course_intro` varchar(128) not null default '' comment '课程介绍',
  `create_time` datetime not null default '1970-01-01 08:00:00' comment '创建时间',
  `update_time` datetime not null default '1970-01-01 08:00:00' comment '更新时间',
  primary key (`id`),
  unique key `key_course_name` (`course_name`)
) engine=InnoDB auto_increment = 1 default charset=utf8 row_format=compact comment='课程信息表';

INSERT INTO imooc_356.homepage_course (id, course_name, course_type, course_icon, course_intro, create_time, update_time) VALUES (1, 'JDK11&12 新特性解读', '0', 'https://www.imooc.com', '解读 JDK11 和 JDK12 的新版本特性', '2020-04-27 15:07:32', '2020-04-27 15:07:32');
INSERT INTO imooc_356.homepage_course (id, course_name, course_type, course_icon, course_intro, create_time, update_time) VALUES (2, '基于 SpringCloud 微服务架构 广告系统设计与实现', '1', 'https://www.imooc.com', '广告系统的设计与实现', '2020-04-27 15:07:32', '2020-04-27 15:07:32');

INSERT INTO imooc_356.homepage_user (id, username, email, create_time, update_time) VALUES (1, 'tuyrk', 'tuyrk@qq.com', '2020-04-28 14:51:39', '2020-04-28 14:51:39');

INSERT INTO imooc_356.homepage_user_course (id, user_id, course_id, create_time, update_time) VALUES (1, 1, 1, '2020-04-28 15:02:50', '2020-04-28 15:02:50');
INSERT INTO imooc_356.homepage_user_course (id, user_id, course_id, create_time, update_time) VALUES (2, 1, 2, '2020-04-28 15:02:50', '2020-04-28 15:02:50');
