package by.romanenko.web_project.controller.concrete.impl;

import by.romanenko.web_project.controller.concrete.Command;
import by.romanenko.web_project.utils.EmailSending;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;

public class WriteAdmin implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email = request.getParameter("email");
        String message = request.getParameter("message");
        // именно Part, потому что содержимое НЕ строка
        Part inputFile = request.getPart("inputFile");

        try {
            EmailSending.sendEmail(email, message, inputFile);
            // устанавливаем оповещение об успехе
            // добавляем сессию, потому что при редиректе атрибуты запроса не сохраняются,
            // т.к. происходит новый HTTP-запрос от клиента серверу
            request.getSession().setAttribute("successMessage", "Сообщение успешно отправлено.");
        } catch (IOException | ServletException e) {
            // устанавливаем оповещение об ошибке
            request.getSession().setAttribute("errorMessage", "Ошибка: " + e.getMessage());
            // поля не обнуляются при ошибке -> их не нужно заново заполнять
            request.getSession().setAttribute("email", email);
            request.getSession().setAttribute("message", message);
        } finally {
            // тут нужно не на index, а на команду контроллера, тогда сессии не нужны
            // исправляем форвард на редирект
            response.sendRedirect("Controller?command=GO_TO_INDEX_PAGE");
        }
    }

}