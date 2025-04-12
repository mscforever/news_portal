package by.romanenko.web_project.dao;

import by.romanenko.web_project.model.Favorites;

//НЕ БУДЕТ РЕАЛИЗОВАН
public interface IFavoritesDAO {
    void addFavorites(Favorites favorites) throws DAOException;
    boolean deleteFavorites(String loggedVisitorEmail, int newsId) throws DAOException;
    boolean deleteAllFavorites(String loggedVisitorEmail) throws DAOException;
}
