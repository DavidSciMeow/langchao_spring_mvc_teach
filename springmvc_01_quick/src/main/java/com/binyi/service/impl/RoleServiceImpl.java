package com.binyi.service.impl;

import com.binyi.dao.impl.RoleDaoImpl;
import com.binyi.domain.SysRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

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

    public int save(SysRole role) {
        logger.debug("Saving role: {}", role.getRoleName());
        return roleDao.save(role);
    }

    public SysRole findById(Long id) {
        return roleDao.findById(id);
    }

    public int update(SysRole role) {
        logger.debug("Updating role id={}", role.getId());
        return roleDao.update(role);
    }

    public int deleteById(Long id) {
        logger.debug("Deleting role id={}", id);
        return roleDao.deleteById(id);
    }

    public int deleteByIds(Long[] ids) {
        logger.debug("Batch deleting roles count={}", ids == null ? 0 : ids.length);
        return roleDao.deleteByIds(ids);
    }

    public java.util.List<SysRole> findAllPaged(int page, int size, String keyword) {
        if (page < 1) page = 1;
        int offset = (page - 1) * size;
        return roleDao.findAll(keyword, offset, size);
    }

    public int count(String keyword) {
        return roleDao.count(keyword);
    }
}
