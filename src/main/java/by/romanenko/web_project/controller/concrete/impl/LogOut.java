package by.romanenko.web_project.controller.concrete.impl;

import by.romanenko.web_project.controller.concrete.Command;
import by.romanenko.web_project.model.Auth;
import by.romanenko.web_project.service.IAuthorizationService;
import by.romanenko.web_project.service.ServiceException;
import by.romanenko.web_project.service.ServiceFactory;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LogOut implements Command {
    private final ServiceFactory serviceFactory = ServiceFactory.getInstance();
    private IAuthorizationService authorizationService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        deleteCookie(response);

        Auth auth = (Auth) request.getSession(false).getAttribute("auth");
        // Проверяем, существует ли сессия
        try {
            authorizationService = serviceFactory.getAuthorizationService();
            if (auth != null) {
                if (authorizationService.deleteToken(auth.getId())) {
                    System.out.println("Токен был успешно удален из базы данных.");
                } else {
                    System.out.println("Не удалось удалить токен из базы данных.");
                }

                request.getSession().invalidate();
                request.getSession().setAttribute("logoutSuccess", "Вы успешно вышли из системы");
            } else {
                request.getSession().setAttribute("logoutFail", "Вы не были зарегистрированы в системе");
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при обработке запроса", e);
        }
        response.sendRedirect("Controller?command=GO_TO_AUTHENTICATION_PAGE");
    }

    private void deleteCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("rememberMe", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
