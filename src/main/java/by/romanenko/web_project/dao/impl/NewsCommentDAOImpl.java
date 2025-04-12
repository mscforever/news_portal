package by.romanenko.web_project.dao.impl;

import by.romanenko.web_project.dao.DAOException;
import by.romanenko.web_project.dao.INewsCommentDAO;
import by.romanenko.web_project.model.NewsComment;

//НЕ БУДЕТ РЕАЛИЗОВАН
public class NewsCommentDAOImpl implements INewsCommentDAO {
    public void addComment(NewsComment comment) throws DAOException {
        System.out.println(comment);
    }

    public boolean deleteComment(int newsCommentId) throws DAOException {
        return true;
    }

    public boolean deleteAllComments(String loggedVisitorEmail) throws DAOException {
        return true;
    }

}
