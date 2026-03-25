package com.langchao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
public class UserController {
    /*@RequestMapping(value = "/quick",method = RequestMethod.GET,params = {"username"})*/
    @RequestMapping(value = "/quick",method = RequestMethod.GET,params = {"username"})
    public String save(){

        System.out.println("Controller save is running..........");

        return "/success.jsp";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void list(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws java.io.IOException {
        javax.servlet.ServletContext servletContext = req.getServletContext();
        org.springframework.context.ApplicationContext app = org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(servletContext);
        com.langchao.dao.UserDao userDao = app.getBean(com.langchao.dao.UserDao.class);
        String respType = req.getParameter("type");
        boolean wantHtml = "HTML".equalsIgnoreCase(respType);
        java.io.PrintWriter out = resp.getWriter();
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
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
