package by.romanenko.web_project.controller.concrete.impl;

import by.romanenko.web_project.controller.concrete.Command;
import by.romanenko.web_project.model.News;
import by.romanenko.web_project.service.INewsService;
import by.romanenko.web_project.service.ServiceException;
import by.romanenko.web_project.service.ServiceFactory;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class GoToNewsPage implements Command {
    private final ServiceFactory serviceFactory = ServiceFactory.getInstance();
    private INewsService newsService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // получаем новость по ID
        try {
            newsService = serviceFactory.getNewsService();
            News news = newsService.getNewsFromDatabaseById(Integer.parseInt(request.getParameter("newsId")));

            if (news != null) {
                request.setAttribute("news", news);
            } else {
                request.setAttribute("errorMessage", "Новость не найдена.");
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при обработке запроса", e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/news-page.jsp");
        dispatcher.forward(request, response);
    }
}
