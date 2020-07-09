package com.tuyrk.blog.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * User 实体。
 *
 * @Author : TYK tuyk涂元坤
 * @Mail : 766564616@qq.com
 * @Create : 2019/8/19 15:54 星期一
 * @Update : 2019/8/19 15:54 by tuyk涂元坤
 */
@Data
@Entity // 实体
public class User {
    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增策略
    private Long id;// 实体一个唯一标识
    private String name;
    private String email;

    protected User() { // 无参构造函数，设为 protected 防止直接使用
    }

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
