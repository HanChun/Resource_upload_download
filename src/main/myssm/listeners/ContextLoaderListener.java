package myssm.listeners;

import myssm.ioc.BeanFactory;
import myssm.ioc.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextLoaderListener implements ServletContextListener {
    /**
     *  监听 上下文 启动，在 上下文启动 的 时候去创建Ioc容器
     *  然后将其保存到application的作用域
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext();
        ServletContext application = servletContextEvent.getServletContext();
        application.setAttribute("beanFactory",beanFactory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}











