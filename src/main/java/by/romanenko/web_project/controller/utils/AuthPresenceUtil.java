package by.romanenko.web_project.controller.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import by.romanenko.web_project.model.Auth;

public class AuthPresenceUtil {
    public static void checkAuthPresence(HttpServletRequest request, HttpServletResponse response, Auth auth) {
        // если не в сессии
        try {
            if (auth == null) {
                System.out.println("Пользователь не залогинен и пытается открыть скрытую для неавторизованного пользователя страницу");
                request.getSession().setAttribute("authError", "У Вас недостаточно прав для посещения этой страницы");
                response.sendRedirect("Controller?command=GO_TO_AUTHENTICATION_PAGE");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
