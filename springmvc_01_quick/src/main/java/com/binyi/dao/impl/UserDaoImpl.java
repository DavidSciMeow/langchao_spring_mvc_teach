package com.binyi.dao.impl;

import com.binyi.domain.SysUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

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

    public List<SysUser> findAll(String keyword, int offset, int limit) {
        String sql;
        Object[] params;
        if (keyword == null || keyword.trim().isEmpty()) {
            sql = "SELECT id, username, email, password, phoneNum FROM sys_user LIMIT ? OFFSET ?";
            params = new Object[]{limit, offset};
        } else {
            sql = "SELECT id, username, email, password, phoneNum FROM sys_user WHERE username LIKE ? OR email LIKE ? LIMIT ? OFFSET ?";
            String like = "%" + keyword + "%";
            params = new Object[]{like, like, limit, offset};
        }
        return jdbcTemplate.query(sql, params, new UserRowMapper());
    }

    public int count(String keyword) {
        String sql;
        Object[] params;
        if (keyword == null || keyword.trim().isEmpty()) {
            sql = "SELECT COUNT(*) FROM sys_user";
            params = new Object[]{};
        } else {
            sql = "SELECT COUNT(*) FROM sys_user WHERE username LIKE ? OR email LIKE ?";
            String like = "%" + keyword + "%";
            params = new Object[]{like, like};
        }
        Integer n = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return n == null ? 0 : n;
    }

    public SysUser findById(Long id) {
        String sql = "SELECT id, username, email, password, phoneNum FROM sys_user WHERE id = ?";
        List<SysUser> list = jdbcTemplate.query(sql, new Object[]{id}, new UserRowMapper());
        return list.isEmpty() ? null : list.get(0);
    }

    public int save(SysUser u) {
        final String sql = "INSERT INTO sys_user(username, email, password, phoneNum) VALUES(?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws java.sql.SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, u.getUsername());
                ps.setString(2, u.getEmail());
                ps.setString(3, u.getPassword());
                ps.setString(4, u.getPhoneNum());
                return ps;
            }
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key != null) {
            u.setId(key.longValue());
        }
        // return 1 to indicate one row inserted (jdbcTemplate.update returned via PreparedStatementCreator doesn't provide count here)
        return (key == null) ? 0 : 1;
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

    public int deleteByIds(Long[] ids) {
        if (ids == null || ids.length == 0) return 0;
        StringBuilder ph = new StringBuilder();
        for (int i = 0; i < ids.length; i++) {
            if (i > 0) ph.append(',');
            ph.append('?');
        }
        Object[] params = new Object[ids.length];
        for (int i = 0; i < ids.length; i++) params[i] = ids[i];
        String sqlRel = "DELETE FROM sys_user_role WHERE userId IN (" + ph.toString() + ")";
        jdbcTemplate.update(sqlRel, params);
        String sql = "DELETE FROM sys_user WHERE id IN (" + ph.toString() + ")";
        return jdbcTemplate.update(sql, params);
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
