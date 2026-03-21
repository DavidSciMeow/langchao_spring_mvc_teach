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

        String respType = req.getParameter("type"); // if type=HTML return HTML table, default JSON
        boolean wantHtml = "HTML".equalsIgnoreCase(respType);
        java.io.PrintWriter out = resp.getWriter();

        if (from != null && to != null && amountStr != null) {
            int amount = Integer.parseInt(amountStr);
            boolean sim = "true".equalsIgnoreCase(simulate);
            try {
                userService.transfer(from, to, amount, sim);
                if (wantHtml) {
                    resp.setContentType("text/html;charset=utf-8");
                    out.println("<html><body>");
                    out.println("<h3>Transfer completed</h3>");
                    out.println("<p>from=" + from + ", to=" + to + ", amount=" + amount + "</p>");
                    out.println("</body></html>");
                } else {
                    resp.setContentType("application/json;charset=utf-8");
                    out.println("{\"success\":true,\"message\":\"Transfer completed\",\"from\":\"" + from + "\",\"to\":\"" + to + "\",\"amount\":" + amount + "}");
                }
            } catch (Exception e) {
                if (wantHtml) {
                    resp.setContentType("text/html;charset=utf-8");
                    out.println("<html><body>");
                    out.println("<h3>Transfer failed</h3>");
                    out.println("<pre>" + e.getMessage() + "</pre>");
                    out.println("</body></html>");
                } else {
                    resp.setContentType("application/json;charset=utf-8");
                    out.println("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}");
                }
            }
        } else {
            String action = req.getParameter("action");
            if ("list".equalsIgnoreCase(action)) {
                // list accounts
                try {
                    java.util.List<com.langchao.domain.Account> all = userDao.findAll();
                    if (wantHtml) {
                        resp.setContentType("text/html;charset=utf-8");
                        out.println("<html><body>");
                        out.println("<table border=1><thead><tr><th>id</th><th>name</th><th>money</th><th>accountNo</th></tr></thead><tbody>");
                        for (com.langchao.domain.Account a : all) {
                            out.println("<tr><td>" + a.getId() + "</td><td>" + a.getName() + "</td><td>" + a.getMoney() + "</td><td>" + a.getAccountNo() + "</td></tr>");
                        }
                        out.println("</tbody></table>");
                        out.println("</body></html>");
                    } else {
                        resp.setContentType("application/json;charset=utf-8");
                        StringBuilder sb = new StringBuilder();
                        sb.append('[');
                        for (int i = 0; i < all.size(); i++) {
                            com.langchao.domain.Account a = all.get(i);
                            sb.append('{');
                            sb.append("\"id\":").append(a.getId()).append(',');
                            sb.append("\"name\":\"").append(escapeJson(a.getName())).append("\",");
                            sb.append("\"money\":").append(a.getMoney()).append(',');
                            sb.append("\"accountNo\":\"").append(escapeJson(a.getAccountNo())).append("\"");
                            sb.append('}');
                            if (i < all.size() - 1) sb.append(',');
                        }
                        sb.append(']');
                        out.println(sb.toString());
                    }
                } catch (Exception e) {
                    if (wantHtml) {
                        resp.setContentType("text/html;charset=utf-8");
                        out.println("<html><body><pre>Failed to list accounts: " + e.getMessage() + "</pre></body></html>");
                    } else {
                        resp.setContentType("application/json;charset=utf-8");
                        out.println("{\"success\":false,\"message\":\"Failed to list accounts: " + e.getMessage() + "\"}");
                    }
                }
            } else {
                if (wantHtml) {
                    resp.setContentType("text/html;charset=utf-8");
                    out.println("<html><body>");
                    out.println("<p>No transfer parameters provided. Example: /userServlet?from=lucy&to=tom&amount=500&simulate=false</p>");
                    out.println("</body></html>");
                } else {
                    resp.setContentType("application/json;charset=utf-8");
                    out.println("{\"success\":false,\"message\":\"No transfer parameters provided. Example: /userServlet?from=lucy&to=tom&amount=500&simulate=false\"}");
                }

            }
        }

    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

}
