package com.binyi.utils;

import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;

public class DBInit {

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        init();
    }

    public void init() {
        try {
            // ensure directory exists
            File dir = new File("database");
            if (!dir.exists()) dir.mkdirs();

            // create tables if not exists (sqlite compatible)
            String sqlUser = "CREATE TABLE IF NOT EXISTS sys_user ( id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, email TEXT, password TEXT, phoneNum TEXT );";
            String sqlRole = "CREATE TABLE IF NOT EXISTS sys_role ( id INTEGER PRIMARY KEY AUTOINCREMENT, roleName TEXT, roleDesc TEXT );";
            String sqlUserRole = "CREATE TABLE IF NOT EXISTS sys_user_role ( userId INTEGER NOT NULL, roleId INTEGER NOT NULL );";

            jdbcTemplate.execute(sqlUser);
            jdbcTemplate.execute(sqlRole);
            jdbcTemplate.execute(sqlUserRole);

            // insert sample data if tables empty (matches provided screenshots)
            Integer roleCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sys_role", Integer.class);
            if (roleCount == null || roleCount == 0) {
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_role(id, roleName, roleDesc) VALUES (1, '院长', '负责全面工作')");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_role(id, roleName, roleDesc) VALUES (2, '研究员', '课程研发工作')");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_role(id, roleName, roleDesc) VALUES (3, '讲师', '授课工作')");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_role(id, roleName, roleDesc) VALUES (4, '助教', '协助解决学生的问题')");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_role(id, roleName, roleDesc) VALUES (5, '辅导员', '负责学生的学习生活')");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_role(id, roleName, roleDesc) VALUES (15, '后勤', '保障日常工作')");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_role(id, roleName, roleDesc) VALUES (17, '维修人员', '维修校内设施')");
            }

            Integer userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sys_user", Integer.class);
            if (userCount == null || userCount == 0) {
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user(id, username, email, password, phoneNum) VALUES (1, 'zhangsan', 'zhangsan@itcast.cn', '123', '13888888888')");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user(id, username, email, password, phoneNum) VALUES (2, 'lisi', 'lisi@itcast.cn', '123', '13999999999')");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user(id, username, email, password, phoneNum) VALUES (6, '李四', 'mattgodzxx@163.cc', '111111', '1861234567')");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user(id, username, email, password, phoneNum) VALUES (7, '王五', '123@163.com', '112233', '1111')");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user(id, username, email, password, phoneNum) VALUES (11, '中医1', 'zhangyi1@163.com', '123456', '1861234567')");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user(id, username, email, password, phoneNum) VALUES (12, '网工1', 'wanggong@163.com', '123456', '18612369874')");
            }

            Integer urCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sys_user_role", Integer.class);
            if (urCount == null || urCount == 0) {
                // user-role mappings from screenshot
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user_role(userId, roleId) VALUES (1,1)");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user_role(userId, roleId) VALUES (11,1)");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user_role(userId, roleId) VALUES (1,2)");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user_role(userId, roleId) VALUES (2,2)");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user_role(userId, roleId) VALUES (11,2)");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user_role(userId, roleId) VALUES (12,2)");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user_role(userId, roleId) VALUES (2,3)");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user_role(userId, roleId) VALUES (7,3)");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user_role(userId, roleId) VALUES (11,3)");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user_role(userId, roleId) VALUES (12,3)");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user_role(userId, roleId) VALUES (6,4)");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user_role(userId, roleId) VALUES (7,4)");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user_role(userId, roleId) VALUES (11,4)");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user_role(userId, roleId) VALUES (6,5)");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user_role(userId, roleId) VALUES (7,5)");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user_role(userId, roleId) VALUES (6,15)");
                jdbcTemplate.execute("INSERT OR IGNORE INTO sys_user_role(userId, roleId) VALUES (12,17)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
