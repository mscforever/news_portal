package by.romanenko.web_project.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import by.romanenko.web_project.service.ICheckService;
import by.romanenko.web_project.service.ServiceException;

public class CheckServiceImpl implements ICheckService {

    /**
     * Валидация email на бэке
     */
    @Override
    public boolean checkInvalidEmail(String email) throws ServiceException {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }
}