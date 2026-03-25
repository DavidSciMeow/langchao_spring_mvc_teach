package com.binyi.service.impl;

import com.binyi.dao.impl.RoleDaoImpl;
import com.binyi.domain.SysRole;

import java.util.List;

public class RoleServiceImpl {

    private RoleDaoImpl roleDao;

    public void setRoleDao(RoleDaoImpl roleDao) {
        this.roleDao = roleDao;
    }

    public List<SysRole> findAll() {
        return roleDao.findAll();
    }

    public List<SysRole> findRolesByUserId(Long userId) {
        return roleDao.findRolesByUserId(userId);
    }
}
