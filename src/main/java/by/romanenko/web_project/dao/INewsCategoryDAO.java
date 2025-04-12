package by.romanenko.web_project.dao;

import by.romanenko.web_project.model.NewsCategory;

//НЕ БУДЕТ РЕАЛИЗОВАН
public interface INewsCategoryDAO {
    void addCategory(NewsCategory category) throws DAOException;
    void changeCategory(NewsCategory category) throws DAOException;
    boolean deleteCategory(int newsCategoryId) throws DAOException;
}
