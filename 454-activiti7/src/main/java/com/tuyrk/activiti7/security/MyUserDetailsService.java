package com.tuyrk.activiti7.security;


import com.tuyrk.activiti7.mapper.UserInfoBeanMapper;
import com.tuyrk.activiti7.pojo.UserInfoBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserInfoBeanMapper userInfoBeanMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /*-------------------------读取数据库判断登录-----------------------*/
        /*SysUser sysUser = sysUserService.queryByUsername(username);

        if (Objects.nonNull(sysUser)) {
            return User.withUsername(username).password(sysUser.getEncodePassword())
                    .authorities(AuthorityUtils.NO_AUTHORITIES)
                    .build();
        }
        throw new UsernameNotFoundException("username: " + username + " notfound");*/



        /*------------------------根据code写死用户登录------------------------------*/

        //-----------------------正常的代码-----------------
        /*log.info("登录用户名：" + username);
        String password = passwordEncoder.encode("111");
        log.info("密码：" + password);
        // 没有做任何数据库校验
        return new User(username, password,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ACTIVITI_USER"));*/

        //-----------------------正常的代码-----------------
        // 页面默认会对密码加密，数据库里如果在用户注册时，用的是加密过的密码，则直接读取比较即可
        // return new User(username, "$2a$10$YFZDTqyBqwHkV/vTxKrhtuyIQCMD/joeIylCs8wbvXnhOYRgD/kDq", AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

        /*-------------------根据自定义用户属性登录-----------------------------*/
        // 读取数据库判断用户。如果用户是null抛出异常，返回用户信息
        UserInfoBean userInfoBean = userInfoBeanMapper.selectByUsername(username);
        if (userInfoBean == null) {
            throw new UsernameNotFoundException("数据库中无此用户！");
        }
        return userInfoBean;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
