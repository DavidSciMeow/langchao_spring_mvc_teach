package com.langchao.dao;

import com.langchao.domain.Account;
import java.util.List;

public interface UserDao {
    // 兼容旧方法
    public void save();

    // CRUD related methods for Account
    public void createTableIfNotExists();
    public void insert(Account account);
    public Account findByName(String name);
    public int updateBalanceByName(String name, int newMoney);
    public List<Account> findAll();
}
