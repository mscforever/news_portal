package by.romanenko.web_project.controller.concrete.impl;

import by.romanenko.web_project.controller.concrete.Command;
import by.romanenko.web_project.model.Auth;
import by.romanenko.web_project.model.News;
import by.romanenko.web_project.model.UserRole;
import by.romanenko.web_project.service.INewsService;
import by.romanenko.web_project.service.ServiceException;
import by.romanenko.web_project.service.ServiceFactory;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.romanenko.web_project.controller.utils.AuthPresenceUtil.checkAuthPresence;
import static by.romanenko.web_project.controller.utils.RolePresenceUtil.isAuthRoleValid;

import java.io.IOException;

public class GoToChangeForm implements Command {

    private final ServiceFactory serviceFactory = ServiceFactory.getInstance();
    private INewsService newsService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Auth auth = (Auth) request.getSession(false).getAttribute("auth");
        // если не в сессии
        checkAuthPresence(request, response, auth);

        System.out.println("В сети находится: " + auth.toString());

        String formType = request.getParameter("formType");
        if (formType == null) {
            request.getSession().setAttribute("authError", "Не указан тип формы");
            response.sendRedirect("Controller?command=NO_SUCH_COMMAND");
            return;
        }

        // Проверка роли перед переходом на страницу
        if ("bio".equals(formType)) {
            if (!isAuthRoleValid(request, response, UserRole.AUTHOR)) {
                return;
            }
        }

        //newsId передавалось в URL, его нужно передать далее в ChangeNewsArticle.java
        String newsId = request.getParameter("newsId");
        try {
            if (newsId != null) {
                request.getSession().setAttribute("newsId", newsId);
                newsService = serviceFactory.getNewsService();
                News newsToEdit = newsService.getNewsFromDatabaseById(Integer.parseInt(newsId));
                if (newsToEdit != null) {
                    request.getSession().setAttribute("news", newsToEdit);
                }
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при обработке запроса", e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(specifyPageAccordingToFormType(formType));
        dispatcher.forward(request, response);
    }

    /**
     * Метод для определения страницы для перехода в зависимости от типа формы
     *
     * @param formType - берется из скрытого поля формы на странице джсп (<input type="hidden" name="formType" value="account">)
     * @return стринговое значение адреса страницы, которое будет подставляться в request.getRequestDispatcher
     */
    private String specifyPageAccordingToFormType(String formType) {

        return switch (formType) {
            case "account" -> "/WEB-INF/jsp/change-user-data-pages/change-account.jsp";
            case "bio" -> "/WEB-INF/jsp/change-user-data-pages/change-bio-form.jsp";
            case "newsArticle" -> "/WEB-INF/jsp/change-news-article-form.jsp";
            default -> "";
        };
    }
}
