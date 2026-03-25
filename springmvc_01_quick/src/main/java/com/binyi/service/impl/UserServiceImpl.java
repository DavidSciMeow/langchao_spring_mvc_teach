package com.binyi.service.impl;

import com.binyi.dao.impl.UserDaoImpl;
import com.binyi.dao.impl.RoleDaoImpl;
import com.binyi.domain.SysRole;
import com.binyi.domain.SysUser;

import java.util.List;

public class UserServiceImpl {

    private UserDaoImpl userDao;
    private RoleDaoImpl roleDao;

    public void setUserDao(UserDaoImpl userDao) {
        this.userDao = userDao;
    }

    public void setRoleDao(RoleDaoImpl roleDao) {
        this.roleDao = roleDao;
    }

    public List<SysUser> findAll() {
        List<SysUser> list = userDao.findAllBasic();
        for (SysUser u : list) {
            List<SysRole> roles = roleDao.findRolesByUserId(u.getId());
            u.setRoles(roles);
        }
        return list;
    }

    public SysUser findById(Long id) {
        SysUser u = userDao.findById(id);
        if (u != null) {
            u.setRoles(roleDao.findRolesByUserId(u.getId()));
        }
        return u;
    }

    public void save(SysUser u, Long[] roleIds) {
        userDao.save(u);
        Long userId = userDao.getLastInsertId();
        if (roleIds != null) {
            for (Long r : roleIds) {
                userDao.addUserRole(userId, r);
            }
        }
    }

    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

    public void update(SysUser u, Long[] roleIds) {
        userDao.update(u);
        userDao.deleteUserRoles(u.getId());
        if (roleIds != null) {
            for (Long r : roleIds) userDao.addUserRole(u.getId(), r);
        }
    }
}
