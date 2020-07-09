package com.tuyrk.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户信息表homepage_user映射实体表
 *
 * @author tuyrk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "homepage_user")
public class HomepageUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 用户名
     */
    @Basic
    @Column(name = "username", nullable = false)
    private String username;

    /**
     * 用户邮箱
     */
    @Basic
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * 创建时间
     */
    @Basic
    @Column(name = "create_time", nullable = false)
    @CreatedDate
    private Date createTime;

    /**
     * 更新时间
     */
    @Basic
    @Column(name = "update_time", nullable = false)
    @LastModifiedDate
    private Date updateTime;

    public HomepageUser(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public static HomepageUser invalid() {
        HomepageUser homepageUser = new HomepageUser("", "");
        homepageUser.setId(-1L);
        return homepageUser;
    }
}
