package by.romanenko.web_project.controller.concrete.impl;

import by.romanenko.web_project.controller.concrete.Command;
import by.romanenko.web_project.model.UserRole;
import by.romanenko.web_project.service.ICheckService;
import by.romanenko.web_project.service.IRegistrationService;
import by.romanenko.web_project.service.ServiceException;
import by.romanenko.web_project.service.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

public class DoRegistration implements Command {

    private final ServiceFactory serviceFactory = ServiceFactory.getInstance();
    private IRegistrationService logicForRegistration;
    private ICheckService check;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            logicForRegistration = serviceFactory.getRegistrationService();
            check = serviceFactory.getCheckService();
            // Параметры приходят из формы регистрации
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String regKey = request.getParameter("authorKey");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            // Прописывается в личном кабинете, а не при регистрации
            String bio = "";

            if (detectErrorsInRegistrationData(request, response, email, password, confirmPassword)) {
                return;
            }

            // Проверяем поле "ключ автора" при наличии данных внутри него
            if (regKey != null && !regKey.trim().isEmpty()) {
                switch (logicForRegistration.specifyRoleKeyBelongsTo(request, regKey)) {
                    case ADMIN:
                        redirectIfSuccess(request, response, name, email, password, regKey, UserRole.ADMIN);
                        break;
                    case AUTHOR:
                        redirectIfSuccess(request, response, name, email, password, regKey, UserRole.AUTHOR);
                        break;
                    case null, default:
                        setAttributeNameAuthErrorAndRedirect(request, response, "invalidAuthorKey", "Вы ввели неверный ключ");
                        break;
                }
            } else {
                if (logicForRegistration.checkUserReg(name, email, password) != -1) {
                    request.getSession().setAttribute("regSuccess", name + ", поздравляем Вас с завершением регистрации в качестве пользователя!");
                    response.sendRedirect("Controller?command=GO_TO_AUTHENTICATION_PAGE");
                } else {
                    redirectToRegistrationPage(response);
                }
            }
        } catch (ServiceException e) {
            setAttributeNameAuthErrorAndRedirect(request, response, "regError", "Произошла ошибка при регистрации. Попробуйте позже.");
        }
    }

    private void redirectIfSuccess(HttpServletRequest request, HttpServletResponse response, String name, String email, String password, String regKey, UserRole userRole) throws IOException, ServiceException {
        logicForRegistration.addInitialBioToExclusiveUser(logicForRegistration.checkExclusiveUserReg(name, email, password, regKey, userRole));
        request.getSession().setAttribute("regSuccess", name + ", поздравляем Вас с завершением регистрации, теперь Вы " + userRole.name());
        response.sendRedirect("Controller?command=GO_TO_AUTHENTICATION_PAGE");
    }

    private boolean detectErrorsInRegistrationData(HttpServletRequest request, HttpServletResponse response,
                                                   String email, String password, String confirmPassword) throws IOException, ServiceException {
        // Проводим валидацию введённого email для передачи информации по пути из формы к БД
        if (check.checkInvalidEmail(email)) {
            setAttributeNameAuthErrorAndRedirect(request, response, "regError", "Неверный формат email");
            return true;
        }

        // Проверяем, что в базе данных ещё нет пользователя с таким email
        if (logicForRegistration.checkEmailExistsInDB(request, email)) {
            setAttributeNameAuthErrorAndRedirect(request, response, "emailDuplicate", "Пользователь с таким e-mail уже существует");
            return true;
        }

        // Проверяем, что повторно введённый пароль верный
        if (!Objects.equals(password, confirmPassword)) {
            setAttributeNameAuthErrorAndRedirect(request, response, "regError", "Пароли не совпадают");
            return true;
        }

        return false;
    }

    private void setAttributeNameAuthErrorAndRedirect(HttpServletRequest request, HttpServletResponse response, String attributeName, String message) throws IOException {
        request.getSession().setAttribute(attributeName, message);
        redirectToRegistrationPage(response);
    }

    private void redirectToRegistrationPage(HttpServletResponse response) throws IOException {
        response.sendRedirect("Controller?command=GO_TO_REGISTRATION_PAGE");
    }
}
