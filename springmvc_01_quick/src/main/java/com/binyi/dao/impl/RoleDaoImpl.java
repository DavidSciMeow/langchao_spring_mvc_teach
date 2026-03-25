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
