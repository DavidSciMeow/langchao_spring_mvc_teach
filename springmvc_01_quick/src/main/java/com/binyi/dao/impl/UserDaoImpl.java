package com.binyi.dao.impl;

import com.binyi.domain.SysUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl {

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SysUser> findAllBasic() {
        String sql = "SELECT id, username, email, password, phoneNum FROM sys_user";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    public SysUser findById(Long id) {
        String sql = "SELECT id, username, email, password, phoneNum FROM sys_user WHERE id = ?";
        List<SysUser> list = jdbcTemplate.query(sql, new Object[]{id}, new UserRowMapper());
        return list.isEmpty() ? null : list.get(0);
    }

    public int save(SysUser u) {
        String sql = "INSERT INTO sys_user(username, email, password, phoneNum) VALUES(?,?,?,?)";
        return jdbcTemplate.update(sql, u.getUsername(), u.getEmail(), u.getPassword(), u.getPhoneNum());
    }

    public Long getLastInsertId() {
        try {
            // sqlite specific
            Number n = jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Number.class);
            return n == null ? 0L : n.longValue();
        } catch (Exception e) {
            return 0L;
        }
    }

    public int update(SysUser u) {
        String sql = "UPDATE sys_user SET username=?, email=?, password=?, phoneNum=? WHERE id=?";
        return jdbcTemplate.update(sql, u.getUsername(), u.getEmail(), u.getPassword(), u.getPhoneNum(), u.getId());
    }

    public int deleteById(Long id) {
        // delete user-role links first
        jdbcTemplate.update("DELETE FROM sys_user_role WHERE userId = ?", id);
        String sql = "DELETE FROM sys_user WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public void addUserRole(Long userId, Long roleId) {
        String sql = "INSERT INTO sys_user_role(userId, roleId) VALUES(?,?)";
        jdbcTemplate.update(sql, userId, roleId);
    }

    public void deleteUserRoles(Long userId) {
        jdbcTemplate.update("DELETE FROM sys_user_role WHERE userId = ?", userId);
    }

    private static class UserRowMapper implements RowMapper<SysUser> {
        @Override
        public SysUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            SysUser u = new SysUser();
            u.setId(rs.getLong("id"));
            u.setUsername(rs.getString("username"));
            u.setEmail(rs.getString("email"));
            u.setPassword(rs.getString("password"));
            u.setPhoneNum(rs.getString("phoneNum"));
            return u;
        }
    }
}
