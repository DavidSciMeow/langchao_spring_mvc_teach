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
    public String list(Model model) {
        List<SysUser> users = userService.findAll();
        model.addAttribute("userList", users);
        return "user-list";
    }

    @RequestMapping("/list")
    public String listAlias() {
        return "redirect:/user/findAll.do";
    }

    @RequestMapping("/saveUI")
    public String saveUI(Model model) {
        List<SysRole> roles = roleService.findAll();
        model.addAttribute("roleList", roles);
        return "user-add";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@RequestParam String username,
                       @RequestParam(required = false) String password,
                       @RequestParam(required = false) String email,
                       @RequestParam(required = false) String phoneNum,
                       @RequestParam(required = false) Long[] roleIds) {
        SysUser u = new SysUser();
        u.setUsername(username);
        u.setPassword(password);
        u.setEmail(email);
        u.setPhoneNum(phoneNum);
        userService.save(u, roleIds);
        return "redirect:/user/findAll.do";
    }

    @RequestMapping("/del/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/user/findAll.do";
    }

}
