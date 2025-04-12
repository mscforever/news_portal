package by.romanenko.web_project.controller.concrete.impl;

import by.romanenko.web_project.controller.concrete.Command;
import by.romanenko.web_project.model.Auth;
import by.romanenko.web_project.model.ProfileDataField;
import by.romanenko.web_project.model.UserRole;
import by.romanenko.web_project.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.romanenko.web_project.controller.utils.UrlFormatterUtil.formatRedirectUrl;

import java.io.IOException;

public class DoAuth implements Command {
    private final ServiceFactory serviceFactory = ServiceFactory.getInstance();
    private IAuthorizationService authorizationService;
    private IChangeProfileService changeProfileService;
    private ICookiesService cookiesService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //эти данные приходят методом пост
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            setAuthErrorAndRedirect(request, response, "Email и пароль не могут быть пустыми.");
            return;
        }

        try {
            authorizationService = serviceFactory.getAuthorizationService();
            changeProfileService = serviceFactory.getChangeProfileService();
            cookiesService = serviceFactory.getCookiesService();

            //проверяем формат email + авторизован ли посетитель
            Auth auth = authorizationService.checkAuth(email, password);
            if (auth == null) {
                setAuthErrorAndRedirect(request, response, "Неверный email или пароль.");
                return;
            }

            //передаем в атрибуты поля объекта auth
            setAttributes(request, auth);

            //проверяем, был ли заполнен чекбокс "запомни меня", передаем в респонс куки в случае поставленной галочки
            try {
                checkCookieField(request, response, auth);
            } catch (ServiceException e) {
                e.printStackTrace();
                throw new RuntimeException("Ошибка при обработке запроса", e);
            }

            response.sendRedirect("Controller?command=" + formatRedirectUrl(auth.getRole()));

        } catch (ServiceException e) {
            setAuthErrorAndRedirect(request, response, "Произошла ошибка при авторизации.");
        }
    }

    /**
     * Метод для передачи в атрибуты сессии аутентифицированного пользователя
     */
    private void setAttributes(HttpServletRequest request, Auth auth) {
        request.getSession().setAttribute("auth", auth);
        request.getSession().setAttribute("id", auth.getId());
        request.getSession().setAttribute("role", auth.getRole().name().toLowerCase());
        request.getSession().setAttribute("name", auth.getName());
        if (auth.getRole() == UserRole.AUTHOR) {
            try {
                // Пытаемся получить поле BIO из базы данных
                String bio = changeProfileService.getFieldData(auth.getId(), ProfileDataField.BIO);

                // Если поле BIO не null, сохраняем его, иначе сохраняем null
                if (bio == null) {
                    request.getSession().setAttribute("bio", null);
                } else {
                    request.getSession().setAttribute("bio", bio);
                }
            } catch (ServiceException e) {
                // В случае ошибки сохраняем null
                request.getSession().setAttribute("bio", null);
                System.out.println("Ошибка при получении BIO: " + e.getMessage());
            }
        } else {
            // Если не автор, устанавливаем bio в null
            request.getSession().setAttribute("bio", null);
        }
    }

    /**
     * Метод для проверки передавалось ли что-то в чекбоксе "запомни меня".
     * Также добавляем в респонс куки, если в чекбоксе была поставлена галочка
     * Куки сохраняются в браузере и будут передаваться с каждым реквестом
     */
    private void checkCookieField(HttpServletRequest request, HttpServletResponse response, Auth auth) throws ServiceException {
        //параметр из формы на странице авторизации
        String rememberMe = request.getParameter("rememberMe");

        //чекбокс "запомни меня" установлен
        if (rememberMe != null && rememberMe.equals("on")) {
            Cookie rememberMeCookie = cookiesService.createOrUpdateRememberMeCookie(request, auth);
            response.addCookie(rememberMeCookie);
            System.out.println("чекбокс 'Remember Me' установлен");

        }
        //чекбокс "запомни меня" не установлен
        else {
            System.out.println("Чекбокс 'Remember Me' не установлен.");
        }
    }

    /**
     * Метод для редиректа на страницу аутентификации в случае ошибки
     */
    private void setAuthErrorAndRedirect(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
        request.getSession().setAttribute("authError", message);
        response.sendRedirect("Controller?command=GO_TO_AUTHENTICATION_PAGE");
    }
}
