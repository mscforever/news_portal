package by.romanenko.web_project.dao;

import by.romanenko.web_project.model.NewsComment;

//НЕ БУДЕТ РЕАЛИЗОВАН
public interface INewsCommentDAO {
    void addComment(NewsComment comment) throws DAOException;
    boolean deleteComment(int newsCommentId) throws DAOException;
    boolean deleteAllComments(String loggedVisitorEmail) throws DAOException;
}
