package com.tuyrk.activiti7.controller;

import com.tuyrk.activiti7.mapper.ActivitiMapper;
import com.tuyrk.activiti7.util.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ActivitiMapper activitiMapper;

    /**
     * 获取用户列表
     *
     * @return 用户列表<姓名, 账号>
     */
    @GetMapping(value = "/getUsers")
    public AjaxResponse<?> getUsers() {
        List<HashMap<String, Object>> userList = activitiMapper.selectUser();
        return AjaxResponse.success(userList);
    }
}
