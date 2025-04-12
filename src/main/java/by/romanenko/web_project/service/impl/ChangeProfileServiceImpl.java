package by.romanenko.web_project.service.impl;

import by.romanenko.web_project.dao.DAOException;
import by.romanenko.web_project.dao.DAOFactory;
import by.romanenko.web_project.dao.IUserDAO;
import by.romanenko.web_project.model.ProfileDataField;
import by.romanenko.web_project.service.IChangeProfileService;
import by.romanenko.web_project.service.ServiceException;

import java.util.Map;


public class ChangeProfileServiceImpl implements IChangeProfileService {

    private final IUserDAO userDAO;

    //ранняя инициализация в конструкторе
    public ChangeProfileServiceImpl() throws ServiceException {
        try {
            this.userDAO = DAOFactory.getInstance().getUserDAO();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public String getFieldData(int id, ProfileDataField profileDataField) throws ServiceException {
        try {
            Map<String, String> fields = userDAO.getUserProfileById(id);
            String fieldKey = (profileDataField.name()).toLowerCase();
            String fieldValue = fields.get(fieldKey);
            if (fieldValue == null) {
                throw new IllegalArgumentException("Такого поля не существует");
            }

            return fieldValue;
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (IllegalArgumentException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void updateProfile(int id, ProfileDataField fieldToUpdate, String newValue) throws ServiceException {
        try {
            switch (fieldToUpdate) {
                case BIO:
                    userDAO.addOrUpdateBio(id, newValue);
                    break;
                case NAME:
                    userDAO.updateName(id, newValue);
                    break;
                case EMAIL:
                    userDAO.updateEmail(id, newValue);
                    break;
                case PASSWORD:
                    userDAO.updatePassword(id, newValue);
                    break;
                default:
                    throw new ServiceException("Неизвестное поле профиля: " + fieldToUpdate);
            }
        } catch (DAOException e) {
            throw new ServiceException("Ошибка при обновлении поля " + fieldToUpdate, e);
        }

    }
}