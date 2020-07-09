package com.tuyrk.blog.controller;

import com.tuyrk.blog.domain.User;
import com.tuyrk.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * User 控制器
 *
 * @Author : TYK tuyk涂元坤
 * @Mail : 766564616@qq.com
 * @Create : 2019/8/19 17:13 星期一
 * @Update : 2019/8/19 17:13 by tuyk涂元坤
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    /**
     * 查询所有用户
     *
     * @param model MVC的Model对象
     * @return ModelAndView
     */
    @GetMapping
    public ModelAndView list(Model model) {
        model.addAttribute("userList", userRepository.findAll());
        model.addAttribute("title", "用户管理");
        return new ModelAndView("users/list", "userModel", model);
    }

    /**
     * 根据用户ID查询用户
     *
     * @param id    用户ID
     * @param model MVC的Model对象
     * @return ModelAndView
     */
    @GetMapping("/{id}")
    public ModelAndView view(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("user", user);
        model.addAttribute("title", "查看用户");
        return new ModelAndView("users/view", "userModel", model);
    }

    /**
     * 获取创建表单页面
     *
     * @param model MVC的Model对象
     * @return ModelAndView
     */
    @GetMapping("/form")
    public ModelAndView form(Model model) {
        model.addAttribute("user", new User(null, null, null));
        model.addAttribute("title", "创建用户");
        return new ModelAndView("users/form", "userModel", model);
    }

    /**
     * 新建用户
     *
     * @param user user用户对象
     * @return 重定向ModelAndView
     */
    @PostMapping
    public ModelAndView save(User user) {
        userRepository.save(user);
        return new ModelAndView("redirect:/users");
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return ModelAndView
     */
    @GetMapping(value = "/delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return new ModelAndView("redirect:/users");
    }

    /**
     * 修改用户
     *
     * @param id    用户ID
     * @param model MVC的Model对象
     * @return ModelAndView
     */
    @GetMapping(value = "/modify/{id}")
    public ModelAndView modify(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("user", user);
        model.addAttribute("title", "修改用户");
        return new ModelAndView("users/form", "userModel", model);
    }
}
