package by.romanenko.web_project.dao;

import java.util.List;

import by.romanenko.web_project.model.News;
import by.romanenko.web_project.model.NewsImportance;
import by.romanenko.web_project.model.User;

public interface INewsDAO {
    int addNews(News news) throws DAOException;

    boolean addPrimaryAuthor(int newsId, int authId) throws DAOException;

    boolean deleteNews(int newsId) throws DAOException;

    List<News> getAllNews() throws DAOException;

    List<User> getNewsAuthorsByNewsId(int newsId) throws DAOException;

    News getNewsById(int id) throws DAOException;

    List<News> getAllNewsByAuthor(int authorId) throws DAOException;

    boolean updateNewsArticle(int newsId, News news) throws DAOException;

    boolean addCoauthor(int coauthorId, int newsId) throws DAOException;

    List <News> findNewsByType(NewsImportance newsImportance) throws DAOException;

}
