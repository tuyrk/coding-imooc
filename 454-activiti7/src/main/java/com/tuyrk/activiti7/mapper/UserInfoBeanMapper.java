package com.tuyrk.activiti7.mapper;


import com.tuyrk.activiti7.pojo.UserInfoBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * @author tuyrk@qq.com
 */
@Mapper
public interface UserInfoBeanMapper {
    /**
     * 从数据库中查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM user WHERE username = #{username}")
    UserInfoBean selectByUsername(@Param("username") String username);
}
