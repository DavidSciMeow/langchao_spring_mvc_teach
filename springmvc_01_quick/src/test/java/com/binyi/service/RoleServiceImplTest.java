package com.binyi.service;

import com.binyi.domain.SysRole;
import com.binyi.service.impl.RoleServiceImpl;
import com.binyi.dao.impl.RoleDaoImpl;
import org.junit.Assert;
import org.junit.Test;

public class RoleServiceImplTest {

    @Test
    public void testSaveAndUpdateAndFindById() {
        RoleServiceImpl svc = new RoleServiceImpl();
        // stub RoleDaoImpl
        svc.setRoleDao(new RoleDaoImpl() {
            @Override
            public int save(com.binyi.domain.SysRole role) {
                return 1;
            }

            @Override
            public com.binyi.domain.SysRole findById(Long id) {
                SysRole r = new SysRole(); r.setId(id); r.setRoleName("r"); return r;
            }

            @Override
            public int update(com.binyi.domain.SysRole role) {
                return 1;
            }
        });

        SysRole r = new SysRole(); r.setRoleName("admin"); r.setRoleDesc("desc");
        int inserted = svc.save(r);
        Assert.assertEquals(1, inserted);

        SysRole found = svc.findById(42L);
        Assert.assertNotNull(found);
        Assert.assertEquals(Long.valueOf(42L), found.getId());

        found.setRoleDesc("new");
        int updated = svc.update(found);
        Assert.assertEquals(1, updated);
    }
}
