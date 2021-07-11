package com.tuyrk.activiti7.pojo;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


@Data
public class UserInfoBean implements UserDetails {
    private static final long serialVersionUID = -8308266372513594348L;
    private Long id;
    public String name;
    private String address;
    private String username;
    private String password;
    private String authorities;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    /**
     * 从数据库中取出authorities字符串后，进行分解，构成一个GrantedAuthority的List返回
     *
     * @return 授予的权限列表
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(authorities.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
