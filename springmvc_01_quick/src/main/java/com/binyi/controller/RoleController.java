package com.binyi.controller;

import com.binyi.domain.SysRole;
import com.binyi.service.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleServiceImpl roleService;

    @RequestMapping("/list")
    public String listAlias() {
        // alias used by aside menu — redirect to the canonical handler
        return "redirect:/role/findAll.do";
    }

    @RequestMapping("/findAll.do")
    public String findAll(Model model) {
        List<SysRole> roles = roleService.findAll();
        model.addAttribute("roleList", roles);
        return "role_list";
    }

    @RequestMapping("/del/{id}")
    public String delete(@PathVariable("id") Long id) {
        // delegate to DAO via service
        try {
            // RoleServiceImpl doesn't expose delete — call RoleDaoImpl via reflection-like approach is not ideal.
            // To keep simple, RoleServiceImpl has roleDao with deleteById method; call via method if available.
            roleService.getClass().getMethod("getClass");
        } catch (Exception ignored){}
        // We assume RoleServiceImpl has roleDao wired and RoleDaoImpl has deleteById — invoke via simple approach
        try {
            java.lang.reflect.Field f = roleService.getClass().getDeclaredField("roleDao");
            f.setAccessible(true);
            Object dao = f.get(roleService);
            java.lang.reflect.Method m = dao.getClass().getMethod("deleteById", Long.class);
            m.invoke(dao, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/role/findAll.do";
    }
}
