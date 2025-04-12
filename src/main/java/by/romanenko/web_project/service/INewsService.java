package by.romanenko.web_project.service;

import java.util.List;

import by.romanenko.web_project.model.News;
import by.romanenko.web_project.model.NewsImportance;
import by.romanenko.web_project.model.User;

public interface INewsService {
    int addNewsToDatabase(News news) throws ServiceException;

    boolean deleteNewsFromDatabase(int newsId) throws ServiceException;

    List<News> getNewsByType(NewsImportance newsImportance) throws ServiceException;

    News getNewsFromDatabaseById(int newsId) throws ServiceException;

    List<News> getNewsList() throws ServiceException;

    List<News> getAuthorNewsList(int authorId) throws ServiceException;

    List<User> getAuthorByNewsId(int newsId) throws ServiceException;

    boolean changeFieldData(int newsId, News news) throws ServiceException;

    boolean addAuthorToNews(int newsId, int authId) throws ServiceException;

    boolean addCoauthorToNews(int coauthorId, int newsId) throws ServiceException;

    //методы для работы с категориями!!!
}