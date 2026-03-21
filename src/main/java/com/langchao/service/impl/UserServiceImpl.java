package com.langchao.service.impl;

import com.langchao.dao.UserDao;
import com.langchao.domain.Account;
import com.langchao.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void save() {
        System.out.println("service save is running.........");
        userDao.save();
    }

    @Override
    @Transactional
    public void transfer(String fromName, String toName, int amount, boolean simulateException) {
        System.out.println("Begin transfer " + amount + " from " + fromName + " to " + toName);
        Account from = userDao.findByName(fromName);
        Account to = userDao.findByName(toName);
        if (from == null) {
            throw new RuntimeException("From account not found: " + fromName);
        }
        if (to == null) {
            throw new RuntimeException("To account not found: " + toName);
        }
        if (Objects.isNull(from.getMoney()) || from.getMoney() < amount) {
            throw new RuntimeException("Insufficient funds in " + fromName);
        }

        int newFrom = from.getMoney() - amount;
        userDao.updateBalanceByName(fromName, newFrom);
        System.out.println("Debited " + amount + " from " + fromName + ", new balance=" + newFrom);

        if (simulateException) {
            System.out.println("Simulating exception after debit to test rollback");
            throw new RuntimeException("Simulated failure after debit");
        }

        int newTo = to.getMoney() + amount;
        userDao.updateBalanceByName(toName, newTo);
        System.out.println("Credited " + amount + " to " + toName + ", new balance=" + newTo);
    }
}
