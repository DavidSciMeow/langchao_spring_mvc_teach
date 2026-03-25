package com.binyi.dao.impl;

import com.binyi.domain.SysRole;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RoleDaoImpl {

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SysRole> findAll() {
        String sql = "SELECT id, roleName, roleDesc FROM sys_role";
        return jdbcTemplate.query(sql, new RoleRowMapper());
    }

    public List<SysRole> findAll(String keyword, int offset, int limit) {
        String sql;
        Object[] params;
        if (keyword == null || keyword.trim().isEmpty()) {
            sql = "SELECT id, roleName, roleDesc FROM sys_role LIMIT ? OFFSET ?";
            params = new Object[]{limit, offset};
        } else {
            sql = "SELECT id, roleName, roleDesc FROM sys_role WHERE roleName LIKE ? OR roleDesc LIKE ? LIMIT ? OFFSET ?";
            String like = "%" + keyword + "%";
            params = new Object[]{like, like, limit, offset};
        }
        return jdbcTemplate.query(sql, params, new RoleRowMapper());
    }

    public int count(String keyword) {
        String sql;
        Object[] params;
        if (keyword == null || keyword.trim().isEmpty()) {
            sql = "SELECT COUNT(*) FROM sys_role";
            params = new Object[]{};
        } else {
            sql = "SELECT COUNT(*) FROM sys_role WHERE roleName LIKE ? OR roleDesc LIKE ?";
            String like = "%" + keyword + "%";
            params = new Object[]{like, like};
        }
        Integer n = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return n == null ? 0 : n;
    }

    public List<SysRole> findRolesByUserId(Long userId) {
        String sql = "SELECT r.id, r.roleName, r.roleDesc FROM sys_role r JOIN sys_user_role ur ON r.id = ur.roleId WHERE ur.userId = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, new RoleRowMapper());
    }

    public int deleteById(Long id) {
        // remove user-role links first
        jdbcTemplate.update("DELETE FROM sys_user_role WHERE roleId = ?", id);
        String sql = "DELETE FROM sys_role WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int deleteByIds(Long[] ids) {
        if (ids == null || ids.length == 0) return 0;
        // build placeholder list
        StringBuilder ph = new StringBuilder();
        for (int i = 0; i < ids.length; i++) {
            if (i > 0) ph.append(',');
            ph.append('?');
        }
        // delete relations
        String sqlRel = "DELETE FROM sys_user_role WHERE roleId IN (" + ph.toString() + ")";
        Object[] params = new Object[ids.length];
        for (int i = 0; i < ids.length; i++) params[i] = ids[i];
        jdbcTemplate.update(sqlRel, params);
        String sql = "DELETE FROM sys_role WHERE id IN (" + ph.toString() + ")";
        return jdbcTemplate.update(sql, params);
    }

    public int save(com.binyi.domain.SysRole role) {
        String sql = "INSERT INTO sys_role(roleName, roleDesc) VALUES(?,?)";
        return jdbcTemplate.update(sql, role.getRoleName(), role.getRoleDesc());
    }

    public SysRole findById(Long id) {
        String sql = "SELECT id, roleName, roleDesc FROM sys_role WHERE id = ?";
        List<SysRole> list = jdbcTemplate.query(sql, new Object[]{id}, new RoleRowMapper());
        return list.isEmpty() ? null : list.get(0);
    }

    public int update(com.binyi.domain.SysRole role) {
        String sql = "UPDATE sys_role SET roleName = ?, roleDesc = ? WHERE id = ?";
        return jdbcTemplate.update(sql, role.getRoleName(), role.getRoleDesc(), role.getId());
    }

    private static class RoleRowMapper implements RowMapper<SysRole> {
        @Override
        public SysRole mapRow(ResultSet rs, int rowNum) throws SQLException {
            SysRole role = new SysRole();
            role.setId(rs.getLong("id"));
            role.setRoleName(rs.getString("roleName"));
            role.setRoleDesc(rs.getString("roleDesc"));
            return role;
        }
    }
}
