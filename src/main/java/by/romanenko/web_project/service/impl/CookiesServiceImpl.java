package by.romanenko.web_project.service.impl;

import by.romanenko.web_project.model.Auth;
import by.romanenko.web_project.model.Token;
import by.romanenko.web_project.service.IAuthorizationService;
import by.romanenko.web_project.service.ICookiesService;
import by.romanenko.web_project.service.ServiceException;
import by.romanenko.web_project.service.ServiceFactory;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Duration;
import java.time.LocalDateTime;

public class CookiesServiceImpl implements ICookiesService {

    private final IAuthorizationService authorizationService;

    //ранняя инициализация в конструкторе
    public CookiesServiceImpl() throws ServiceException {
        this.authorizationService = ServiceFactory.getInstance().getAuthorizationService();
    }

    /**
     * Метод для создания или обновления cookie "rememberMe".
     * Проверяет наличие старого токена в cookies и его срок годности,
     * и в случае необходимости создает новый токен.
     *
     * @param request запрос HTTP, который может содержать cookies
     * @param auth    объект с данными авторизованного пользователя
     * @return cookie "rememberMe", который либо обновляется, либо создается новый
     */
    public Cookie createOrUpdateRememberMeCookie(HttpServletRequest request, Auth auth) throws ServiceException {
        String tokenFromCookies = null;
        // Ищем cookie "rememberMe" в запросе
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberMe".equals(cookie.getName())) {
                    // Если cookie с именем, как в чекбоксе, существует, запоминаем его значение
                    tokenFromCookies = cookie.getValue();
                    break;
                }
            }
        }
        Cookie rememberMeCookie = null;
        Token tokenGenerated = null;

        // 1 вариант - токен уже существует в cookies
        if (tokenFromCookies != null) {

            Token tokenExisted = authorizationService.returnTokenIfPresent(auth.getId());
            // 1.1 токен есть в бд
            if (tokenExisted != null) {
                // 1.1.1 токен корректный + актуальный
                if (tokenExisted.getTokenName().equals(tokenFromCookies) && tokenExisted.getTokenExpirationDate().isAfter(LocalDateTime.now())) {

                    rememberMeCookie = new Cookie("rememberMe", tokenFromCookies);
                    setCookieAttributes(rememberMeCookie, tokenExisted);
                    System.out.println("токен существует, не истек срок годности, совпадает имя ");
                }
                // 1.1.2 токен некорректный + неакутальный
                else {
                    if (!authorizationService.deleteToken(auth.getId())) {
                        System.out.println("Произошла ошибка при удалении старого токена");
                    } else {
                        System.out.println("Старый токен удален, поскольку он невалидный");
                    }

                    rememberMeCookie = createAndSetCookie(auth, tokenGenerated, rememberMeCookie);
                    System.out.println("Был создан новый токен");
                }

            }
            // 1.2 токена нет в бд
            else {
                rememberMeCookie = createAndSetCookie(auth, tokenGenerated, rememberMeCookie);
                System.out.println("Токен не найден в БД, был создан новый");
            }
        }
        // 2 вариант - токена не существует в cookies
        else {
            rememberMeCookie = createAndSetCookie(auth, tokenGenerated, rememberMeCookie);
            System.out.println("Токен не найден в кукис, создан новый.");

        }

        return rememberMeCookie;
    }

    /**
     * Устанавливает атрибуты для cookie, такие как максимальный возраст, возможность доступа к куки через джаваскрипт, путь.
     *
     * @param cookie cookie, которому нужно установить атрибуты
     * @param token  токен из БД, на основе которого рассчитываются атрибуты
     */
    private void setCookieAttributes(Cookie cookie, Token token) {
        Duration duration = Duration.between(token.getTokenRegDate(), token.getTokenExpirationDate());
        cookie.setMaxAge((int) duration.getSeconds());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
    }

    /**
     * Метод для создания и установки cookie "rememberMe".
     * Генерирует новый токен и сохраняет его в cookies.
     *
     * @param auth             объект с данными авторизованного пользователя
     * @param tokenGenerated   токен, который нужно установить в cookie
     * @param rememberMeCookie cookie, которую необходимо создать и установить
     */
    private Cookie createAndSetCookie(Auth auth, Token tokenGenerated, Cookie rememberMeCookie) throws RuntimeException {
        try {
            String token = authorizationService.generateToken();
            tokenGenerated = authorizationService.saveToken(auth.getId(), token);
            rememberMeCookie = new Cookie("rememberMe", tokenGenerated.getTokenName());
            setCookieAttributes(rememberMeCookie, tokenGenerated);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return rememberMeCookie;
    }
}
