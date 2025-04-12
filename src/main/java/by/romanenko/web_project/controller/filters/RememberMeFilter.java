package by.romanenko.web_project.controller.filters;

import by.romanenko.web_project.model.Auth;
import by.romanenko.web_project.model.ProfileDataField;
import by.romanenko.web_project.model.User;
import by.romanenko.web_project.service.IAuthorizationService;
import by.romanenko.web_project.service.IChangeProfileService;
import by.romanenko.web_project.service.ServiceException;
import by.romanenko.web_project.service.ServiceFactory;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "rememberMeFilter", urlPatterns = "/*")

//делать фильтр "проверка на null в сессии" нет смысла, потому что часть страниц д.б. доступна без авторизации, проще вынести проверку в утилиты

public class RememberMeFilter implements Filter {
    private final ServiceFactory serviceFactory;
    private final IAuthorizationService authorizationLogic;
    private final IChangeProfileService changeProfileService;

    public RememberMeFilter() throws ServiceException {
        serviceFactory = ServiceFactory.getInstance(); // Инициализация фабрики
        authorizationLogic = serviceFactory.getAuthorizationService();
        changeProfileService = serviceFactory.getChangeProfileService();
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            Auth auth = (Auth) session.getAttribute("auth");
            if (auth == null) {
                Cookie[] cookies = httpRequest.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if ("rememberMe".equals(cookie.getName())) {
                            String token = cookie.getValue();
                            if (token != null && !token.isEmpty()) {
                                User userFromCookies = null;
                                try {
                                    userFromCookies = authorizationLogic.findUserByToken(token);
                                } catch (ServiceException e) {
                                    throw new RuntimeException(e);
                                }

                                if (userFromCookies != null) {
                                    Auth authFromCookies = new Auth(userFromCookies.getId(), userFromCookies.getName(), userFromCookies.getRole());
                                    System.out.println(authFromCookies);
                                    httpRequest.getSession().setAttribute("auth", authFromCookies);
                                    httpRequest.getSession().setAttribute("id", authFromCookies.getId());
                                    httpRequest.getSession().setAttribute("role", authFromCookies.getRole().name().toLowerCase());
                                    httpRequest.getSession().setAttribute("name", authFromCookies.getName());

                                    String bio = null;
                                    try {
                                        // Получаем поле BIO для пользователя
                                        bio = changeProfileService.getFieldData(authFromCookies.getId(), ProfileDataField.BIO);
                                        // Если поле не существует или равно null, устанавливаем его как null в сессии
                                        if (bio == null) {
                                            session.setAttribute("bio", null);
                                        } else {
                                            session.setAttribute("bio", bio);
                                        }
                                    } catch (ServiceException e) {
                                        // Если возникла ошибка при получении BIO, установим в null
                                        session.setAttribute("bio", null);
                                        System.out.println("Ошибка при получении BIO: " + e.getMessage());
                                    }

                                    break; // Завершаем цикл после того, как нашли нужного пользователя
                                }
                            }
                        }
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
