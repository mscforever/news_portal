package by.romanenko.web_project.dao.impl;

import by.romanenko.web_project.dao.DAOException;
import by.romanenko.web_project.dao.INewsCategoryDAO;
import by.romanenko.web_project.model.NewsCategory;

//НЕ БУДЕТ РЕАЛИЗОВАН
public class NewsCategoryDAOImpl implements INewsCategoryDAO {
    public void addCategory(NewsCategory category) throws DAOException {
        System.out.println(category);
    }

    public void changeCategory(NewsCategory category) throws DAOException {
        System.out.println(category);
    }

    public boolean deleteCategory(int newsCategoryId) throws DAOException {
        return true;
    }
}
