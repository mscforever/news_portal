package by.romanenko.web_project.controller.concrete.impl;

import by.romanenko.web_project.controller.concrete.Command;
import by.romanenko.web_project.model.Auth;
import by.romanenko.web_project.model.News;
import by.romanenko.web_project.model.UserRole;
import by.romanenko.web_project.service.INewsService;
import by.romanenko.web_project.service.ServiceException;
import by.romanenko.web_project.service.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.romanenko.web_project.controller.utils.AuthPresenceUtil.checkAuthPresence;
import static by.romanenko.web_project.controller.utils.NewsUtil.addCoauthorIfNeeded;
import static by.romanenko.web_project.controller.utils.NewsUtil.checkNewsExists;
import static by.romanenko.web_project.controller.utils.RolePresenceUtil.isAuthRoleValid;

import java.io.IOException;

public class ChangeNewsArticle implements Command {
    private final ServiceFactory serviceFactory = ServiceFactory.getInstance();
    private INewsService newsService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Auth auth = (Auth) request.getSession().getAttribute("auth");
        checkAuthPresence(request, response, auth);
        if (!isAuthRoleValid(request, response, UserRole.AUTHOR)) {
            return;
        }

        //пришло из GoToChangeForm.java
        int newsId = Integer.parseInt((String) request.getSession().getAttribute("newsId"));
        try {
            newsService = serviceFactory.getNewsService();
            News news = newsService.getNewsFromDatabaseById(newsId);
            checkNewsExists(request, response, news);

            news.updateFields(request.getParameter("newNewsTitle"), request.getParameter("newNewsBrief"), request.getParameter("newNewsContent"), request.getParameter("newNewsCategory"));


            if (newsService.changeFieldData(newsId, news)) {
                addCoauthorIfNeeded(auth.getId(), newsId, newsService);

                request.getSession().setAttribute("changeArticleSuccess", "Статья успешно обновлена");
            }
        } catch (ServiceException e) {
            request.getSession().setAttribute("changeArticleError", "Ошибка при обновлении новости: " + e.getMessage());
        }

        response.sendRedirect("Controller?command=SHOW_ALL_AUTHOR_NEWS");
    }


}
