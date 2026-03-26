package com.binyi.service.impl;

import com.binyi.dao.impl.UserDaoImpl;
import com.binyi.dao.impl.RoleDaoImpl;
import com.binyi.domain.SysRole;
import com.binyi.domain.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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

    public java.util.List<SysUser> findAllPaged(int page, int size, String keyword) {
        if (page < 1) page = 1;
        int offset = (page - 1) * size;
        java.util.List<SysUser> list = userDao.findAll(keyword, offset, size);
        for (SysUser u : list) {
            u.setRoles(roleDao.findRolesByUserId(u.getId()));
        }
        return list;
    }

    public int count(String keyword) {
        return userDao.count(keyword);
    }

    public SysUser findById(Long id) {
        SysUser u = userDao.findById(id);
        if (u != null) {
            u.setRoles(roleDao.findRolesByUserId(u.getId()));
        }
        return u;
    }

    public void save(SysUser u, Long[] roleIds) {
        logger.debug("Saving user: {}", u.getUsername());
        userDao.save(u);
        Long userId = u.getId();
        if (userId != null && userId > 0 && roleIds != null) {
            for (Long r : roleIds) {
                userDao.addUserRole(userId, r);
            }
        }
    }

    public void deleteById(Long id) {
        logger.debug("Deleting user id={}", id);
        userDao.deleteById(id);
    }

    public int deleteByIds(Long[] ids) {
        logger.debug("Batch deleting users count={}", ids == null ? 0 : ids.length);
        return userDao.deleteByIds(ids);
    }

    public void update(SysUser u, Long[] roleIds) {
        logger.debug("Updating user id={}", u.getId());
        userDao.update(u);
        userDao.deleteUserRoles(u.getId());
        if (roleIds != null) {
            for (Long r : roleIds) userDao.addUserRole(u.getId(), r);
        }
    }
}
