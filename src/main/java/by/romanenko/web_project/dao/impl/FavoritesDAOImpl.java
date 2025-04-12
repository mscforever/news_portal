package by.romanenko.web_project.dao.impl;

import by.romanenko.web_project.dao.DAOException;
import by.romanenko.web_project.dao.IFavoritesDAO;
import by.romanenko.web_project.model.Favorites;

//НЕ БУДЕТ РЕАЛИЗОВАН
public class FavoritesDAOImpl implements IFavoritesDAO {
    public void addFavorites(Favorites favorites) throws DAOException {
        System.out.println(favorites.toString());
    }

    public boolean deleteFavorites(String loggedVisitorEmail, int newsId) throws DAOException {
        return true;
    }

    public boolean deleteAllFavorites(String loggedVisitorEmail) throws DAOException {
        return true;
    }

}
