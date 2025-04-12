package by.romanenko.web_project.controller.concrete.impl;

import by.romanenko.web_project.controller.concrete.Command;
import by.romanenko.web_project.controller.utils.UrlFormatterUtil;
import by.romanenko.web_project.model.Auth;
import by.romanenko.web_project.model.ProfileDataField;
import by.romanenko.web_project.model.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static by.romanenko.web_project.controller.utils.AuthPresenceUtil.checkAuthPresence;
import static by.romanenko.web_project.controller.utils.UpdateUtil.isProfileFieldCheckedAndUpdated;

import java.io.IOException;

//отдельно, на одну форму по одной команде контроллера + эта форма доступна для всех ролей, в отличие от changeBio

public class ChangeAccount implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Auth auth = (Auth) request.getSession().getAttribute("auth");

        //проверяем, жива ли сессия
        checkAuthPresence(request, response, auth);

        boolean checkUpdateResult = isUpdated(request, auth);
        // проверяем, было ли изменено хотя бы одно поле
        request.getSession().setAttribute(
                checkUpdateResult ?
                        "changeAccountSuccess" : "changeAccountError",
                checkUpdateResult ?
                        "Профиль успешно обновлен!" : "Не было внесено ни одного изменения");

        // Перенаправление на страницу профиля
        UserRole role = UserRole.valueOf(((String) request.getSession().getAttribute("role")).toUpperCase());
        response.sendRedirect("Controller?command=" + UrlFormatterUtil.formatRedirectUrl(role));
    }

    private boolean isUpdated(HttpServletRequest request, Auth auth) {

        boolean updatedName = isProfileFieldCheckedAndUpdated(auth, request.getParameter("newName"), auth.getName(), ProfileDataField.NAME);
        boolean updatedEmail = isProfileFieldCheckedAndUpdated(request, auth, request.getParameter("oldEmail"), request.getParameter("newEmail"), ProfileDataField.EMAIL);
        boolean updatedPassword = isProfileFieldCheckedAndUpdated(request, auth, request.getParameter("oldPassword"), request.getParameter("newPassword"), ProfileDataField.PASSWORD);

        return (updatedName || updatedEmail || updatedPassword);
    }
}