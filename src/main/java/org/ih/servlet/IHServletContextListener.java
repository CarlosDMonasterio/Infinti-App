package org.ih.servlet;

import org.ih.ApplicationInitializer;
import org.ih.common.logging.Logger;
import org.ih.dao.hibernate.HibernateConfiguration;
import org.ih.task.TaskRunner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.nio.file.Path;

/**
 * Servlet context listener for running initializing
 * and pre-shutdown instructions
 *
 * @author Hector Plahar
 */
public class IHServletContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        Path path = ApplicationInitializer.configure();
        if (path == null)
            return;

        try {
            HibernateConfiguration.beginTransaction();
            ApplicationInitializer.loadAuthentication();
            ApplicationInitializer.start(path);
            HibernateConfiguration.commitTransaction();
        } catch (Throwable e) {
            Logger.logErrorOnly(e);
            HibernateConfiguration.rollbackTransaction();
        }
    }

    public void contextDestroyed(ServletContextEvent event) {
        // shutdown executor service (when created)
        TaskRunner.getInstance().stopService();
        HibernateConfiguration.close();
    }
}
