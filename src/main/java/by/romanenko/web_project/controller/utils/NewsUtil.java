package by.romanenko.web_project.controller.utils;

import by.romanenko.web_project.model.News;
import by.romanenko.web_project.model.NewsImportance;
import by.romanenko.web_project.model.User;
import by.romanenko.web_project.service.INewsService;
import by.romanenko.web_project.service.ServiceException;
import by.romanenko.web_project.utils.ImageUtils.ImagePathReadable;
import by.romanenko.web_project.utils.ImageUtils.ReadImagePathFromStorage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class NewsUtil {

    public static void addNewsToSession(HttpServletRequest request, News newNews, List<News> newsList) {

        //добавляем обновленный список новостей (с учетом добавленной только что новости) в атрибуты
        request.getSession().setAttribute("newsList", newsList);

        //в зависимости от степени важности, добавляем еще и в соответствующий список
        switch (newNews.getImportance()) {
            case NewsImportance.BREAKING:
                request.getSession().setAttribute("breakingNews", newNews);
                break;
            case NewsImportance.TOP:
                request.getSession().setAttribute("topNews", newNews);
                break;
        }
    }

    public static News createNewsFromForm(HttpServletRequest request) {
        News newNews = new News();

        // Формируем объект News параметрами из запроса, которые передавались через форму
        newNews.setImportance(NewsImportance.valueOf(request.getParameter("newsImportance").toUpperCase()));
        newNews.setTitle(request.getParameter("newsTitle"));
        newNews.setBrief(request.getParameter("newsBrief"));
        newNews.setContent(request.getParameter("newsContent"));
        newNews.setCategory(request.getParameter("newsCategory"));

        ImagePathReadable tmp = new ReadImagePathFromStorage();

        try {

            // Сохраняем во внешнюю папку
//            newNews.setImageUrl(tmp.getImagePathFromExternalStorage(request));
//            System.out.println("Сохраняем во внешнюю папку");

            // Сохраняем в проект
            newNews.setImageUrl(tmp.getImagePathFromProjectStorage(request));
            System.out.println("Сохраняем в проект");


        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }

        return newNews;
    }

    public static void checkNewsExists(HttpServletRequest request, HttpServletResponse response, News news) throws IOException {
        if (news == null) {
            request.getSession().setAttribute("changeArticleError", "Новость с указанным ID не найдена.");
            response.sendRedirect("Controller?command=SHOW_ALL_AUTHOR_NEWS");
        }
    }

    public static void addCoauthorIfNeeded(int authorId, int newsId, INewsService newsService) throws ServiceException {
        List<User> newsAuthors = newsService.getAuthorByNewsId(newsId);
        for (User newsAuthor : newsAuthors) {
            if (newsAuthor.getId() != authorId) {
                newsService.addCoauthorToNews(authorId, newsId);
            }
        }
    }

}