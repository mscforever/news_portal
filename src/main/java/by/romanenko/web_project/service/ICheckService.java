package by.romanenko.web_project.service;

public interface ICheckService {
    boolean checkInvalidEmail(String email) throws ServiceException;
}
