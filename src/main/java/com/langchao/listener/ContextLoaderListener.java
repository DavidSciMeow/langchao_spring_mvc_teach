package com.langchao.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        //将spring应用上下文对象存储到servletContext中
        ServletContext servletContext = servletContextEvent.getServletContext();
        //读取web.xml中的全局变量
        String contextConfigLocation = servletContext.getInitParameter("contextConfigLocation");
        System.out.println("配置文件路径：" + contextConfigLocation);
        ApplicationContext app = new ClassPathXmlApplicationContext(contextConfigLocation);
        servletContext.setAttribute("app",app);
        System.out.println("spring容器创建完毕");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
