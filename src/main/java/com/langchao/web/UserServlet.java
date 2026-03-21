package com.langchao.web;

import com.langchao.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*//ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        ServletContext servletContext = this.getServletContext();
        //ApplicationContext  app = (ApplicationContext) servletContext.getAttribute("app");
        */
        //利用工具类进行读取
        ServletContext servletContext = this.getServletContext();
        ApplicationContext app = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        UserService userService = app.getBean(UserService.class);

        // also access UserDao for listing current accounts when requested
        com.langchao.dao.UserDao userDao = app.getBean(com.langchao.dao.UserDao.class);

        // support transfer testing via query params: from, to, amount, simulate=true|false
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amountStr = req.getParameter("amount");
        String simulate = req.getParameter("simulate");

        resp.setContentType("text/plain;charset=utf-8");
        java.io.PrintWriter out = resp.getWriter();

        if (from != null && to != null && amountStr != null) {
            int amount = Integer.parseInt(amountStr);
            boolean sim = "true".equalsIgnoreCase(simulate);
            try {
                userService.transfer(from, to, amount, sim);
                out.println("Transfer completed (from=" + from + ", to=" + to + ", amount=" + amount + ")");
            } catch (Exception e) {
                out.println("Transfer failed: " + e.getMessage());
            }
        } else {
            String action = req.getParameter("action");
            if ("list".equalsIgnoreCase(action)) {
                // list accounts
                try {
                    java.util.List<com.langchao.domain.Account> all = userDao.findAll();
                    out.println("id\tname\tmoney\taccountNo");
                    for (com.langchao.domain.Account a : all) {
                        out.println(a.getId() + "\t" + a.getName() + "\t" + a.getMoney() + "\t" + a.getAccountNo());
                    }
                } catch (Exception e) {
                    out.println("Failed to list accounts: " + e.getMessage());
                }
            } else {
                out.println("No transfer parameters provided. Example: /userServlet?from=lucy&to=tom&amount=500&simulate=false");
            }
        }


    }

}
