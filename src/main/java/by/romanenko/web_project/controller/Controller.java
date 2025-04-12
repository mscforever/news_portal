package by.romanenko.web_project.controller;

import by.romanenko.web_project.controller.concrete.Command;
import by.romanenko.web_project.controller.concrete.CommandProvider;
import by.romanenko.web_project.service.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/Controller")
// сервлет может обрабатывать запросы с типом контента multipart/form-data (используется для загрузки файлов через форму)
// такие данные могут быть разделены на части (текст и файлы)
@MultipartConfig
public class Controller extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final CommandProvider provider = new CommandProvider();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        doRequest(request, response);

    }

    private void doRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userCommand = request.getParameter("command");
        System.out.println("Received command: " + userCommand);
        Command command = provider.takeCommand(userCommand);
        command.execute(request, response);
    }
}
