package by.romanenko.web_project.service.impl;

import by.romanenko.web_project.dao.DAOException;
import by.romanenko.web_project.dao.DAOFactory;
import by.romanenko.web_project.dao.IUserDAO;
import by.romanenko.web_project.model.UserRole;
import by.romanenko.web_project.service.IRegistrationService;
import by.romanenko.web_project.service.ServiceException;
import jakarta.servlet.http.HttpServletRequest;

public class RegistrationServiceImpl implements IRegistrationService {
    private final DAOFactory daoFactory;
    private final IUserDAO userRegistrationLogic;

    public RegistrationServiceImpl() throws ServiceException {
        try {
            daoFactory = DAOFactory.getInstance();
            userRegistrationLogic = daoFactory.getUserDAO();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Уточняем, является ли введённый при регистрации ключ:
     * 1. аналогичным вытянутому из БД ключу в слое ДАО
     * 2. не использованным ранее
     */
    @Override
    public UserRole specifyRoleKeyBelongsTo(HttpServletRequest request, String inputRegKey) throws ServiceException {
        try {
            return userRegistrationLogic.specifyKeyTypeIfItIsNotReserved(inputRegKey);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Возвращает ID зарегистрированного пользователя при успешной регистрации пользователя в БД
     */
    @Override
    public int checkUserReg(String name, String email, String password) throws ServiceException {
        try {
            return userRegistrationLogic.registerUserInDatabase(name, email, password);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int checkExclusiveUserReg(String name, String email, String password, String regKey, UserRole userRole) throws ServiceException {
        try {
            return userRegistrationLogic.registerExclusiveUserInDatabase(name, email, password, regKey, userRole);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    public boolean addInitialBioToExclusiveUser(int userId) throws ServiceException {
        try {
            //return userRegistrationLogic.addInitialBioToExclusiveUser(userId);
            return userRegistrationLogic.addOrUpdateBio(userId, null);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Проверяем, вернулось ли true после поиска email в БД пользователей
     */
    @Override
    public boolean checkEmailExistsInDB(HttpServletRequest request, String email) throws ServiceException {
        try {
            return userRegistrationLogic.doesEmailExistInDB(email);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

}
