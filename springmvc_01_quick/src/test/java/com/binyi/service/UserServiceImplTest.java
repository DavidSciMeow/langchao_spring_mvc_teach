package com.binyi.service;

import com.binyi.domain.SysUser;
import com.binyi.domain.SysRole;
import com.binyi.service.impl.UserServiceImpl;
import com.binyi.dao.impl.UserDaoImpl;
import com.binyi.dao.impl.RoleDaoImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class UserServiceImplTest {

    @Test
    public void testSaveAndUpdateAndFindById() {
        UserServiceImpl svc = new UserServiceImpl();
        // stub userDao
        svc.setUserDao(new UserDaoImpl() {
            @Override
            public int save(com.binyi.domain.SysUser u) {
                return 1;
            }

            @Override
            public Long getLastInsertId() { return 100L; }

            @Override
            public int update(com.binyi.domain.SysUser u) { return 1; }

            @Override
            public com.binyi.domain.SysUser findById(Long id) {
                com.binyi.domain.SysUser u = new com.binyi.domain.SysUser(); u.setId(id); u.setUsername("u"); return u;
            }
            @Override
            public void addUserRole(Long userId, Long roleId) {
                // no-op for unit test: avoid accessing jdbcTemplate
            }
            @Override
            public void deleteUserRoles(Long userId) {
                // no-op for unit test
            }
        });
        // stub roleDao for role population
        svc.setRoleDao(new RoleDaoImpl() {
            @Override
            public java.util.List<com.binyi.domain.SysRole> findRolesByUserId(Long userId) {
                SysRole r = new SysRole(); r.setId(1L); r.setRoleName("r"); return Arrays.asList(r);
            }
        });

        SysUser u = new SysUser(); u.setUsername("bob"); u.setPassword("secret");
        svc.save(u, new Long[]{1L});

        SysUser found = svc.findById(100L);
        Assert.assertNotNull(found);
        Assert.assertEquals(Long.valueOf(100L), found.getId());

        found.setUsername("bob2");
        svc.update(found, new Long[]{1L});
    }
}
