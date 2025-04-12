package by.romanenko.web_project.listener;

import by.romanenko.web_project.dao.dbmanager.ConnectionPool;
import by.romanenko.web_project.dao.dbmanager.ConnectionPoolException;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

//Инициализация пула соединений должна выполняться один раз при старте(!!!) приложения
@WebListener
public class ConnectionPoolInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            connectionPool.initPoolData();
            System.out.println("Connection pool initialized successfully.");
        } catch (ConnectionPoolException e) {
            System.err.println("Failed to initialize connection pool: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPool.getInstance().dispose();
        System.out.println("Connection pool disposed.");
    }
}