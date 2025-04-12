package by.romanenko.web_project.service;

import by.romanenko.web_project.model.ProfileDataField;

public interface IChangeProfileService {

    String getFieldData (int id, ProfileDataField profileDataField) throws ServiceException;

//    boolean updateBio(int id, String newBio) throws ServiceException;
//
//    void updateName(int id, String newName) throws ServiceException;
//
//    void updateEmail(int id, String newEmail) throws ServiceException;
//
//    void updatePassword(int id, String newPassword) throws ServiceException;

    void updateProfile(int id, ProfileDataField fieldToUpdate, String newValue) throws ServiceException;
}