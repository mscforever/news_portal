package by.romanenko.web_project.service;


import by.romanenko.web_project.model.UserRole;
import jakarta.servlet.http.HttpServletRequest;

public interface IRegistrationService {
    UserRole specifyRoleKeyBelongsTo(HttpServletRequest request, String inputRegKey) throws ServiceException;

    int checkUserReg(String name, String email, String password) throws ServiceException;

    int checkExclusiveUserReg(String name, String email, String password, String regKey, UserRole userRole) throws ServiceException;

    boolean addInitialBioToExclusiveUser(int userId) throws ServiceException;

    boolean checkEmailExistsInDB(HttpServletRequest request, String email) throws ServiceException;
}