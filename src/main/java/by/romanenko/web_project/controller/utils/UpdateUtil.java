package by.romanenko.web_project.controller.utils;

import by.romanenko.web_project.model.Auth;
import by.romanenko.web_project.model.ProfileDataField;
import by.romanenko.web_project.service.IChangeProfileService;
import by.romanenko.web_project.service.ServiceException;
import by.romanenko.web_project.service.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;


public class UpdateUtil {

    private UpdateUtil() {
        // Приватный конструктор, чтобы никто не мог создать объект этого класса
        // Т.о. мы делаем утилитарный класс
    }

    private static final ServiceFactory serviceFactory = ServiceFactory.getInstance();
    private static IChangeProfileService changeProfileService;

    /**
     * Метод для проверки, было ли поле профиля обновлено пользователем,
     * для обновления (в случае необходимости) поля в базе данных через сервисный слой
     *
     * @param auth             - объект Auth, представляющий зарегистрированного пользователя, для которого будет обновляться поле профиля
     * @param inputValue       - строка, представляющая новое значение поля профиля, введенное пользователем в форме
     * @param profileDataField - поле профиля, которое будет обновляться
     * @return true, если поле профиля было успешно обновлено в базе данных, и false в противном случае
     */
    private static boolean isProfileFieldUpdated(Auth auth, String inputValue, ProfileDataField profileDataField) {
        try {
            changeProfileService = serviceFactory.getChangeProfileService();
            changeProfileService.updateProfile(auth.getId(), profileDataField, inputValue);

            if (Objects.equals(profileDataField, ProfileDataField.NAME)) {
                auth.setName(inputValue);
            }
            return true;
        } catch (ServiceException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Метод для передачи обновленного значения поля в параметры сессии и для отображения сообщений о результате проведения операции по обновлению
     * Используется для одиночно стоящего поля типа БИО
     *
     * @param request               - объект HttpServletRequest, представляющий текущий запрос от клиента
     * @param isProfileFieldUpdated - результат выполнения метода isProfileFieldUpdated
     * @param auth                  - - объект Auth, представляющий зарегистрированного пользователя, для которого будет обновляться поле профиля
     * @param profileDataField      - поле профиля, которое будет обновляться
     */
    public static void updateSessionAndDisplayMessage(HttpServletRequest request, boolean isProfileFieldUpdated, Auth auth, ProfileDataField profileDataField) {
        if (isProfileFieldUpdated) {
            try {
                //результат выполнения метода для выгрузки обновленного значения из БД
                String updatedFieldValueFromDb = changeProfileService.getFieldData(auth.getId(), profileDataField);
                //метод передает в параметры сессии обновленное значение
                updateNotificationMessage(request, profileDataField, updatedFieldValueFromDb);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            addChangeSuccessMessage(request, profileDataField);
        } else {
            addChangeErrorMessage(request, profileDataField, "Не было внесено изменений в поле ");
        }
    }

    /**
     * Метод для проверки, было ли поле профиля обновлено пользователем,
     * корректно ли пользователь ввёл старое значение поля (которое хранится в БД),
     * для обновления (в случае необходимости) поля в базе данных через сервисный слой.
     * Метод используется для полей, которые не хранятся в объекте Auth по соображениям безопасности (почта, пароль)
     *
     * @param request          - объект HttpServletRequest, представляющий текущий запрос от клиента
     * @param auth             - объект Auth, представляющий зарегистрированного пользователя, для которого будет обновляться поле профиля
     * @param oldInputValue    - строка, введенная пользователем в форме (если значение этого поля не соответствует значению из БД, то пользователь не имеет право обновлять значение поля)
     * @param newInputValue    - строка, представляющая новое значение поля профиля, введенное пользователем в форме
     * @param profileDataField - поле профиля, которое будет обновляться
     * @return true, если поле профиля было успешно обновлено в базе данных, и false в противном случае
     */
    public static boolean isProfileFieldCheckedAndUpdated(HttpServletRequest request, Auth auth, String oldInputValue, String newInputValue, ProfileDataField profileDataField) {
        if (isInputEmpty(oldInputValue) || isInputEmpty(newInputValue)) {
            return false;
        }

        try {
            String valueStoredInDatabase = changeProfileService.getFieldData(auth.getId(), profileDataField);

            if (!Objects.equals(oldInputValue, valueStoredInDatabase)) {
                addChangeErrorMessage(request, profileDataField, "Введённое вами значение не совпадает с данными из личного кабинета. Перепроверьте поле ");
                return false;
            }

            return isProfileFieldUpdated(auth, newInputValue, profileDataField);

        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isProfileFieldCheckedAndUpdated(Auth auth, String newInputValue, String valueToCompare, ProfileDataField profileDataField) {
        if (isInputEmpty(newInputValue)) {
            return false;
        }

        if (Objects.equals(newInputValue, valueToCompare)) {
            return false;
        }

        return isProfileFieldUpdated(auth, newInputValue, profileDataField);
    }

    /**
     * Метод для проверки валидности введённых данных в поле
     *
     * @param inputValue - строковое значение, которое пользователь ввёл в форме
     * @return true, если в поле присутствуют данные
     */
    private static boolean isInputEmpty(String inputValue) {
        return inputValue == null || inputValue.trim().isEmpty();
    }

    private static void addChangeSuccessMessage(HttpServletRequest request, ProfileDataField profileDataField) {
        request.getSession().setAttribute("change" + engFieldName(profileDataField) + "Success", "Поле " + rusFieldName(profileDataField) + " успешно обновлено!");
    }

    private static void addChangeErrorMessage(HttpServletRequest request, ProfileDataField profileDataField, String
            message) {
        request.getSession().setAttribute("change" + engFieldName(profileDataField) + "Error", message + rusFieldName(profileDataField));
    }

    private static void updateNotificationMessage(HttpServletRequest request, ProfileDataField
            profileDataField, String value) {
        request.getSession().setAttribute("updated" + engFieldName(profileDataField), value);
    }

    private static String engFieldName(ProfileDataField profileDataField) {
        char[] array = profileDataField.name().toCharArray();
        StringBuilder sb = new StringBuilder();
        sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            sb.append(Character.toLowerCase(array[i]));
        }
        return sb.toString();
    }

    private static String rusFieldName(ProfileDataField profileDataField) {
        return profileDataField.getDescription();
    }

}