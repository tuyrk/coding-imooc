package com.tuyrk.blog.domain;

import lombok.Data;

/**
 * User 实体。
 *
 * @Author : TYK tuyk涂元坤
 * @Mail : 766564616@qq.com
 * @Create : 2019/8/19 15:54 星期一
 * @Update : 2019/8/19 15:54 by tuyk涂元坤
 */
@Data
public class User {
    private Long id;// 实体一个唯一标识
    private String name;
    private String email;

    public User() {
    }

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
