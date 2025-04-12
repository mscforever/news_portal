package by.romanenko.web_project.controller.concrete.impl;

import by.romanenko.web_project.controller.concrete.Command;
import by.romanenko.web_project.model.News;
import by.romanenko.web_project.model.NewsImportance;
import by.romanenko.web_project.service.INewsService;
import by.romanenko.web_project.service.ServiceException;
import by.romanenko.web_project.service.ServiceFactory;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class GoToIndexPage implements Command {
    private final ServiceFactory serviceFactory = ServiceFactory.getInstance();
    private INewsService newsService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            newsService = serviceFactory.getNewsService();
            List<News> breakingNewsList = newsService.getNewsByType(NewsImportance.BREAKING);
            News breakingNews = breakingNewsList.get(breakingNewsList.size() - 1);
            List<News> topNewsList = newsService.getNewsByType(NewsImportance.TOP);
            News topNews = topNewsList.get(topNewsList.size() - 1);
            List<News> regularNewsList = newsService.getNewsByType(NewsImportance.REGULAR);

            if (breakingNews != null) {
                request.setAttribute("breakingNews", breakingNews); // выводим последнюю добавленную срочную новость
            }
            if (topNews != null) {
                request.setAttribute("topNews", topNews); // выводим последнюю добавленную топовую новость
            }

            request.setAttribute("regularNews", regularNewsList);

        } catch (ServiceException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Ошибка при получении новостей.");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/main-index.jsp");
        dispatcher.forward(request, response);
    }
}