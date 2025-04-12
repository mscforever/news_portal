package by.romanenko.web_project.controller.concrete.impl;

import by.romanenko.web_project.controller.concrete.Command;
import by.romanenko.web_project.model.Auth;
import by.romanenko.web_project.model.UserRole;
import by.romanenko.web_project.service.INewsService;
import by.romanenko.web_project.service.ServiceException;
import by.romanenko.web_project.service.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.romanenko.web_project.controller.utils.AuthPresenceUtil.checkAuthPresence;
import static by.romanenko.web_project.controller.utils.RolePresenceUtil.isAuthRoleValid;

import java.io.IOException;

public class DeleteFromDatabase implements Command {
    private final ServiceFactory serviceFactory = ServiceFactory.getInstance();
    private INewsService newsService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Auth auth = (Auth) request.getSession(false).getAttribute("auth");
        // если не в сессии
        checkAuthPresence(request, response, auth);
        // если от другой роли
        if (!isAuthRoleValid(request, response, UserRole.ADMIN)) {
            return;
        }

        int newsId = Integer.parseInt(request.getParameter("newsId"));
        try {
            newsService = serviceFactory.getNewsService();
            if (newsService.deleteNewsFromDatabase(newsId)) {
                request.getSession().setAttribute("deleteSuccessMessage", "Новость удалена");
            } else {
                request.getSession().setAttribute("deleteFailMessage", "При удалении новости произошла ошибка");
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при обработке запроса", e);
        }

        response.sendRedirect("Controller?command=SHOW_ALL_NEWS");
    }
}