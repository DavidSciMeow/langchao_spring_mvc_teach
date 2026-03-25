package com.binyi.controller;

import com.binyi.domain.SysRole;
import com.binyi.domain.SysUser;
import com.binyi.service.impl.RoleServiceImpl;
import com.binyi.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleServiceImpl roleService;

    @RequestMapping("/findAll.do")
    public String list(Model model,
                       @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "1") Integer page,
                       @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "10") Integer size,
                       @org.springframework.web.bind.annotation.RequestParam(required = false) String q) {
        int p = page == null ? 1 : page;
        int s = size == null ? 10 : size;
        java.util.List<SysUser> users = userService.findAllPaged(p, s, q);
        int total = userService.count(q);
        int totalPages = (int) Math.ceil(total / (double) s);

        model.addAttribute("userList", users);
        model.addAttribute("page", p);
        model.addAttribute("size", s);
        model.addAttribute("total", total);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("q", q);
        return "user-list";
    }

    @RequestMapping("/list")
    public String listAlias() {
        return "redirect:/user/findAll.do";
    }

    @RequestMapping("/saveUI")
    public String saveUI(@org.springframework.web.bind.annotation.RequestParam(required = false) Long id, Model model) {
        List<SysRole> roles = roleService.findAll();
        model.addAttribute("roleList", roles);
        if (id != null) {
            SysUser u = userService.findById(id);
            model.addAttribute("user", u);
        }
        return "user-add";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@RequestParam(required = false) Long id,
                       @RequestParam String username,
                       @RequestParam(required = false) String password,
                       @RequestParam(required = false) String email,
                       @RequestParam(required = false) String phoneNum,
                       @RequestParam(required = false) Long[] roleIds,
                       Model model) {
        java.util.Map<String, String> errors = new java.util.HashMap<>();
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "用户名为必填项");
        } else if (username.length() > 50) {
            errors.put("username", "用户名不能超过50个字符");
        }

        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "密码为必填项");
        } else if (password.length() < 6) {
            errors.put("password", "密码长度至少为6位");
        }

        if (email != null && !email.trim().isEmpty()) {
            if (!email.contains("@")) {
                errors.put("email", "请输入有效的电子邮件地址");
            }
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("roleList", roleService.findAll());
            return "user-add";
        }

        SysUser u = new SysUser();
        if (id != null) u.setId(id);
        u.setUsername(username.trim());
        u.setPassword(password);
        u.setEmail(email == null ? null : email.trim());
        u.setPhoneNum(phoneNum == null ? null : phoneNum.trim());
        if (id == null) {
            userService.save(u, roleIds);
        } else {
            userService.update(u, roleIds);
        }
        return "redirect:/user/findAll.do";
    }

    @RequestMapping("/del/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/user/findAll.do";
    }

    @RequestMapping(value = "/batchDel", method = RequestMethod.POST)
    public String batchDelete(@org.springframework.web.bind.annotation.RequestParam(required = false) Long[] ids) {
        if (ids != null && ids.length > 0) {
            userService.deleteByIds(ids);
        }
        return "redirect:/user/findAll.do";
    }

}
