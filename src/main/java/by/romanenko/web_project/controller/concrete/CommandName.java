package by.romanenko.web_project.controller.concrete;

public enum CommandName {
    // 1. Аутентификация и регистрация
    DO_AUTH,                            // Авторизация пользователя
    DO_REGISTRATION,                    // Регистрация нового пользователя
    GO_TO_AUTHENTICATION_PAGE,        // Переход на страницу авторизации
    GO_TO_REGISTRATION_PAGE,            // Переход на страницу регистрации

    // 2. Навигация по страницам
    GO_TO_INDEX_PAGE,                   // Переход на главную страницу
    GO_TO_NEWS_PAGE,                    // Переход на страницу новостей
    GO_TO_USER_ACCOUNT_PAGE,            // Переход на страницу пользователя
    GO_TO_AUTHOR_ACCOUNT_PAGE,          // Переход на страницу автора
    GO_TO_ADMIN_ACCOUNT_PAGE,           // Переход на страницу админа
    GO_TO_ADD_NEWS_FORM_PAGE,           // Переход на страницу добавления новости
    SHOW_STUB_PAGE,                     // Переход на заглушку

    // 3. Управление новостями
    SHOW_ALL_NEWS,                      // Просмотр всех новостей
    SHOW_ALL_AUTHOR_NEWS,               // Просмотр всех новостей автора
    ADD_NEWS,                           // Добавление новой новости
    CHANGE_NEWS_ARTICLE,                // Изменение существующей новости
    DELETE_FROM_DATABASE,               // Удаление новости из базы данных

    // 4. Управление профилем пользователя
    GO_TO_CHANGE_FORM,                  // Переход на страницу изменения данных (например, имени, пароля)
    CHANGE_ACCOUNT,                     // Подтверждение изменений в аккаунте пользователя
    CHANGE_BIO,                         // Изменение поля биографии пользователя

    // 5. Иное
    WRITE_ADMIN,                        // Форма отправки письма
    NO_SUCH_COMMAND,                    // Ошибка - несуществующая команда
    LOGOUT,                             // Выход из системы
}
