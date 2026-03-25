package com.binyi.controller;

import com.binyi.domain.SysRole;
import com.binyi.service.impl.RoleServiceImpl;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;
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
    public String findAll(Model model,
                          @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "1") Integer page,
                          @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "10") Integer size,
                          @org.springframework.web.bind.annotation.RequestParam(required = false) String q) {
        int p = page == null ? 1 : page;
        int s = size == null ? 10 : size;
        java.util.List<SysRole> roles = roleService.findAllPaged(p, s, q);
        int total = roleService.count(q);
        int totalPages = (int) Math.ceil(total / (double) s);

        model.addAttribute("roleList", roles);
        model.addAttribute("page", p);
        model.addAttribute("size", s);
        model.addAttribute("total", total);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("q", q);
        return "role-list";
    }

    @RequestMapping("/saveUI")
    public String saveUI(@org.springframework.web.bind.annotation.RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            SysRole role = roleService.findById(id);
            model.addAttribute("role", role);
        }
        return "role-add";
    }

    @RequestMapping("/del/{id}")
    public String delete(@PathVariable("id") Long id) {
        // delegate deletion to service layer
        try {
            roleService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/role/findAll.do";
    }

    @RequestMapping(value = "/batchDel", method = org.springframework.web.bind.annotation.RequestMethod.POST)
    public String batchDelete(@org.springframework.web.bind.annotation.RequestParam(required = false) Long[] ids) {
        if (ids != null && ids.length > 0) {
            roleService.deleteByIds(ids);
        }
        return "redirect:/role/findAll.do";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@org.springframework.web.bind.annotation.RequestParam(required = false) Long id,
                       @org.springframework.web.bind.annotation.RequestParam String roleName,
                       @org.springframework.web.bind.annotation.RequestParam(required = false) String roleDesc,
                       Model model) {
        Map<String, String> errors = new HashMap<>();
        if (roleName == null || roleName.trim().isEmpty()) {
            errors.put("roleName", "角色名称为必填项");
        } else if (roleName.length() > 100) {
            errors.put("roleName", "角色名称不能超过100个字符");
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            // keep submitted values available via request parameters in JSP
            return "role-add";
        }

        SysRole r = new SysRole();
        if (id != null) r.setId(id);
        r.setRoleName(roleName.trim());
        r.setRoleDesc(roleDesc == null ? null : roleDesc.trim());
        if (id == null) {
            roleService.save(r);
        } else {
            roleService.update(r);
        }
        return "redirect:/role/findAll.do";
    }
}
