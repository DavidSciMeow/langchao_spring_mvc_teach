package com.langchao.dao.impl;

import com.langchao.dao.UserDao;
import com.langchao.domain.Account;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void init() {
        createTableIfNotExists();
        // insert sample data if not exists
        if (findByName("lucy") == null) {
            insert(new Account() {{ setName("zhangsan"); setMoney(2000); setAccountNo("111111"); }});
            insert(new Account() {{ setName("lucy"); setMoney(5000); setAccountNo("123456"); }});
            insert(new Account() {{ setName("tom"); setMoney(5000); setAccountNo("123457"); }});
            insert(new Account() {{ setName("lisi"); setMoney(1000); setAccountNo("22222"); }});
            insert(new Account() {{ setName("wangwu"); setMoney(2000); setAccountNo("333333"); }});
        }
    }

    @Override
    public void save() {
        System.out.println("dao save is running..........");
    }

    @Override
    public void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS account (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, money INTEGER, accountNo TEXT)";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void insert(Account account) {
        String sql = "INSERT INTO account(name,money,accountNo) VALUES(?,?,?)";
        jdbcTemplate.update(sql, account.getName(), account.getMoney(), account.getAccountNo());
    }

    @Override
    public Account findByName(String name) {
        String sql = "SELECT id,name,money,accountNo FROM account WHERE name=?";
        List<Account> list = jdbcTemplate.query(sql, new Object[]{name}, new BeanPropertyRowMapper<>(Account.class));
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public int updateBalanceByName(String name, int newMoney) {
        String sql = "UPDATE account SET money=? WHERE name=?";
        return jdbcTemplate.update(sql, newMoney, name);
    }

    @Override
    public List<Account> findAll() {
        String sql = "SELECT id,name,money,accountNo FROM account";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Account.class));
    }
}
