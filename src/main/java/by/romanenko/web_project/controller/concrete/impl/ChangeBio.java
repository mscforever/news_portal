package by.romanenko.web_project.controller.concrete.impl;

import by.romanenko.web_project.controller.concrete.Command;
import by.romanenko.web_project.controller.utils.UrlFormatterUtil;
import by.romanenko.web_project.model.Auth;
import by.romanenko.web_project.model.ProfileDataField;
import by.romanenko.web_project.model.UserRole;
import by.romanenko.web_project.service.IChangeProfileService;
import by.romanenko.web_project.service.ServiceException;
import by.romanenko.web_project.service.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.romanenko.web_project.controller.utils.AuthPresenceUtil.checkAuthPresence;
import static by.romanenko.web_project.controller.utils.UpdateUtil.isProfileFieldCheckedAndUpdated;
import static by.romanenko.web_project.controller.utils.UpdateUtil.updateSessionAndDisplayMessage;

import java.io.IOException;

public class ChangeBio implements Command {
    //ПЕРЕДЕЛАЙ ВЕЗДЕ IChangeProfileService СТАТИК МЕТОДЫ
    private final ServiceFactory serviceFactory = ServiceFactory.getInstance();
    private IChangeProfileService changeProfileService;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Auth auth = (Auth) request.getSession(false).getAttribute("auth");

        // если не в сессии
        // если от другой роли мы проверяем шагом ранее (в контроллере GoToChangeForm), иначе на страницу заходило, только не давало отправить
        checkAuthPresence(request, response, auth);
        try {
            changeProfileService = serviceFactory.getChangeProfileService();
            boolean isProfileUpdated = isProfileFieldCheckedAndUpdated(auth, request.getParameter("newBio"), changeProfileService.getFieldData(auth.getId(), ProfileDataField.BIO), ProfileDataField.BIO);
            updateSessionAndDisplayMessage(request, isProfileUpdated, auth, ProfileDataField.BIO);
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при обработке запроса", e);
        }
        // Перенаправление на страницу профиля
        response.sendRedirect("Controller?command=" + UrlFormatterUtil.formatRedirectUrl(UserRole.valueOf(((String) request.getSession().getAttribute("role")).toUpperCase())));
    }
}