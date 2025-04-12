package by.romanenko.web_project.controller.concrete.impl;

import by.romanenko.web_project.controller.concrete.Command;
import by.romanenko.web_project.controller.utils.NewsUtil;
import by.romanenko.web_project.model.Auth;
import by.romanenko.web_project.model.News;
import by.romanenko.web_project.model.UserRole;
import by.romanenko.web_project.service.INewsService;
import by.romanenko.web_project.service.ServiceException;
import by.romanenko.web_project.service.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.romanenko.web_project.controller.utils.AuthPresenceUtil.checkAuthPresence;
import static by.romanenko.web_project.controller.utils.RolePresenceUtil.isAuthRoleValid;

import java.io.IOException;
import java.util.List;

public class AddNews implements Command {

    private final ServiceFactory serviceFactory = ServiceFactory.getInstance();
    private INewsService newsService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {

        Auth auth = (Auth) request.getSession(false).getAttribute("auth");

        // если не в сессии
        checkAuthPresence(request, response, auth);
        // если от другой роли
        if (!isAuthRoleValid(request, response, UserRole.AUTHOR)) {
            return;
        }

        News newNews = null;
        try {
            newsService = serviceFactory.getNewsService();
            //формируем новостной объект данными из формы
            newNews = NewsUtil.createNewsFromForm(request);

            //добавляем в БД новость и автора
            int newNewsId = newsService.addNewsToDatabase(newNews);
            newsService.addAuthorToNews(newNewsId, auth.getId());

            //получаем из бд весь список новостей
            List<News> newsList = newsService.getNewsList();
            //добавляем в этот список новую новость
            newsList.add(newNews);

            //добавляем в атрибуты сессии нужную информацию
            NewsUtil.addNewsToSession(request, newNews, newsList);

            request.getSession().setAttribute("addNewsSuccess", "Новость успешно добавлена");

            // Перенаправляем на страницу с новостями автора
            response.sendRedirect("Controller?command=SHOW_ALL_AUTHOR_NEWS");

        } catch (IllegalArgumentException | IOException e) {
            errorHandling(request, newNews, "addNewsError", "Неверный формат данных", response);

        } catch (ServiceException e) {
            errorHandling(request, newNews, "errorMessage", "Ошибка при добавлении новости", response);
        }
    }

    private static void errorHandling(HttpServletRequest request, News newNews, String errorClass, String errorMessage, HttpServletResponse response) {
        request.getSession().setAttribute("newNews", newNews);
        request.getSession().setAttribute(errorClass, errorMessage);
        try {
            request.getRequestDispatcher("/WEB-INF/jsp/add-news-form-page.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при обработке запроса", e);
        }
    }
}
