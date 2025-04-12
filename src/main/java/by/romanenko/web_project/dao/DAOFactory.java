package by.romanenko.web_project.dao;

import by.romanenko.web_project.dao.impl.*;

public class DAOFactory {
    private static DAOFactory instance;

    // Экземпляры DAO объектов, инициализируются по мере необходимости
    //private IDatabaseConnectionDAO sqlDatabaseConnection;
    private IUserDAO sqlUserImpl;
    private IFavoritesDAO sqlFavoritesImpl;
    private INewsCategoryDAO sqlNewsCategoryImpl;
    private INewsCommentDAO sqlNewsCommentImpl;
    private INewsDAO sqlNewsImpl;

    // Метод для получения синглтон-экземпляра DAOFactory
    public static DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

//    // Метод для получения экземпляра DatabaseConnectionDAO
//    public IDatabaseConnectionDAO getDbConnection() throws DAOException {
//        if (sqlDatabaseConnection == null) {
//            sqlDatabaseConnection = new DatabaseConnectionDAOImpl();
//        }
//        return sqlDatabaseConnection;
//    }

    // Методы для получения DAO объектов
    public IUserDAO getUserDAO() throws DAOException {
        if (sqlUserImpl == null) {
            sqlUserImpl = new UserDAOImpl();
        }
        return sqlUserImpl;
    }

    public IFavoritesDAO getFavoritesDAO() throws DAOException {
        if (sqlFavoritesImpl == null) {
            sqlFavoritesImpl = new FavoritesDAOImpl();
        }
        return sqlFavoritesImpl;
    }

    public INewsCategoryDAO getNewsCategoryDAO() throws DAOException {
        if (sqlNewsCategoryImpl == null) {
            sqlNewsCategoryImpl = new NewsCategoryDAOImpl();
        }
        return sqlNewsCategoryImpl;
    }

    public INewsCommentDAO getNewsCommentDAO() throws DAOException {
        if (sqlNewsCommentImpl == null) {
            sqlNewsCommentImpl = new NewsCommentDAOImpl();
        }
        return sqlNewsCommentImpl;
    }

    public INewsDAO getNewsDAO() throws DAOException {
        if (sqlNewsImpl == null) {
            sqlNewsImpl = new NewsDAOImpl();
        }
        return sqlNewsImpl;
    }

}
