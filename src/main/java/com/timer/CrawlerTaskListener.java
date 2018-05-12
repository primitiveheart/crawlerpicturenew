package com.timer;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by admin on 2018/5/6.
 */
public class CrawlerTaskListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //执行的入口
        new CrawlerTimer(servletContextEvent);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
