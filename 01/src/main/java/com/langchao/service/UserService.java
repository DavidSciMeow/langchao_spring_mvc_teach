package com.langchao.service;

public interface UserService {
    // 兼容旧方法
    public void save();

    // transfer money from one account to another; if simulateException=true, throw after debit to test rollback
    public void transfer(String fromName, String toName, int amount, boolean simulateException);
}
