package by.romanenko.web_project.dao.impl;

import by.romanenko.web_project.dao.DAOException;
import by.romanenko.web_project.dao.IUserDAO;
import by.romanenko.web_project.dao.dbmanager.ConnectionPool;
import by.romanenko.web_project.dao.dbmanager.ConnectionPoolException;
import by.romanenko.web_project.model.Auth;
import by.romanenko.web_project.model.Token;
import by.romanenko.web_project.model.User;
import by.romanenko.web_project.model.UserRole;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class UserDAOImpl implements IUserDAO {

    private static final String LOGIN_QUERY = "SELECT u.id, u.name AS user_name, u.email, r.name AS role_name " +
                                              "FROM news_management.users u " +
                                              "JOIN news_management.roles r ON u.role_id = r.id " +
                                              "WHERE u.email = ? AND u.password = ?";
    private static final String FIND_EMAIL_QUERY = "SELECT 1 FROM news_management.users WHERE email = ?";
    private static final String REGISTER_USER_QUERY = "INSERT INTO news_management.users (registration_date, name, email, password, role_id, reg_keys_id) " +
                                                      "VALUES (?, ?, ?, ?, (SELECT id FROM news_management.roles WHERE name = ? LIMIT 1), ?)";
    private static final String REGISTER_EXCLUSIVE_USER_QUERY = "INSERT INTO news_management.users (registration_date, name, email, password, role_id, reg_keys_id) " +
                                                                "VALUES (?, ?, ?, ?, (SELECT id FROM news_management.roles WHERE name = ? LIMIT 1), " +
                                                                "(SELECT id FROM news_management.reg_keys WHERE value = ? LIMIT 1))";
    private static final String RESERVE_AUTH_KEY_QUERY = "UPDATE news_management.reg_keys SET is_reserved = 1 WHERE value = ?";
    private static final String SELECT_BIO_QUERY = "SELECT COUNT(*) FROM news_management.user_details WHERE user_id = ?";
    private static final String ADD_BIO_QUERY = "INSERT INTO news_management.user_details (bio, user_id) VALUES (?, ?)";

    private static final String UPDATE_BIO_QUERY = "UPDATE news_management.user_details SET bio = ? WHERE user_id = ?";
    private static final String UPDATE_NAME_QUERY = "UPDATE news_management.users SET name = ? WHERE id=?";
    private static final String UPDATE_EMAIL_QUERY = "UPDATE news_management.users SET email = ? WHERE id=?";
    private static final String UPDATE_PASSWORD_QUERY = "UPDATE news_management.users SET password = ? WHERE id=?";

    private static final String GET_KEYTYPE_QUERY = "SELECT r.name FROM news_management.reg_keys rk JOIN news_management.roles r ON rk.roles_id = r.id " +
                                                    "WHERE rk.value = ? && rk.is_reserved = ?";
    private static final String GET_USERPROFILE_QUERY = "SELECT u.email, u.password, ud.bio FROM news_management.users u " +
                                                        "LEFT JOIN news_management.user_details ud ON u.id = ud.user_id WHERE u.id = ?";

    private static final String CHECK_TOKEN_PRESENCE_QUERY = "SELECT * FROM news_management.tokens WHERE users_id = ?";
    private static final String GET_USER_TOKEN_QUERY = "SELECT * FROM news_management.tokens WHERE users_id = ?";
    private static final String SAVE_TOKEN_QUERY = "INSERT INTO news_management.tokens (token, reg_date, exp_date, users_id) VALUES (?, ?, ?, ?)";
    private static final String DELETE_TOKEN_QUERY = "DELETE FROM news_management.tokens WHERE users_id = ?";
    private static final String FIND_USER_BY_TOKEN_QUERY = "SELECT u.id, u.name, r.name AS role_name " +
                                                           "FROM news_management.users u JOIN news_management.tokens t ON u.id = t.users_id " +
                                                           "JOIN news_management.roles r ON u.role_id = r.id WHERE token = ?";

    //принцип работы: получаем соединение -> операция с бд -> возвращаем соединение в пул
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    /**
     * Метод авторизации
     *
     * @param email    передан по цепочке "форма авторизации -> слой сервисов"
     * @param password передан по цепочке "форма авторизации -> слой сервисов"
     * @return сформированный из БД объект, который был авторизован в сессии по переданным логину и паролю
     */
    @Override
    //ранее здесь быд user
    public Auth logIn(String email, String password) throws DAOException {
        //для сохранения в Объекте User выбираем не все поля
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(LOGIN_QUERY)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("user_name");  // Используем псевдоним user_name, который ранее задавали с помощью AS
                    String roleName = resultSet.getString("role_name");  // Используем псевдоним role_name, который ранее задавали с помощью AS
                    UserRole userRole = UserRole.valueOf(roleName.toUpperCase());
                    return new Auth(id, name, userRole);
                } else {
                    throw new DAOException("Пользователь с email " + email + " и паролем " + password + " не найден.");
                }
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Метод для проверки в БД, регистрировался ли данный email ранее. Используется при регистрации
     *
     * @param email передан по цепочке "форма -> слой сервисов"
     * @return true, если в бд уже есть этот email
     */
    @Override
    public boolean doesEmailExistInDB(String email) throws DAOException {
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_EMAIL_QUERY)) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Метод для добавления в БД пользователя с ролью user
     *
     * @param name     передан по цепочке "форма регистрации -> слой сервисов"
     * @param email    передан по цепочке "форма регистрации -> слой сервисов"
     * @param password передан по цепочке "форма регистрации -> слой сервисов"
     * @return сгенерированный в БД ID добавленного пользователя
     */
    @Override
    public int registerUserInDatabase(String name, String email, String password) throws DAOException {
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(REGISTER_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            Date regDate = Date.valueOf(LocalDate.now());
            preparedStatement.setDate(1, regDate);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, UserRole.USER.name());
            preparedStatement.setNull(6, java.sql.Types.INTEGER);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
        return 0;
    }

    @Override
    public int registerExclusiveUserInDatabase(String name, String email, String password, String regKey, UserRole userRole) throws DAOException {
        //био null ПОКА вставляем отдельным методом

        Connection connection = null;
        PreparedStatement insertStatement = null;
        PreparedStatement updateStatement = null;
        ResultSet generatedKeys = null;


        try {
            connection = connectionPool.takeConnection();

            connection.setAutoCommit(false);

            insertStatement = connection.prepareStatement(REGISTER_EXCLUSIVE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);

            Date regDate = Date.valueOf(LocalDate.now());
            insertStatement.setDate(1, regDate);
            insertStatement.setString(2, name);
            insertStatement.setString(3, email);
            insertStatement.setString(4, password);
            insertStatement.setString(5, userRole.name());
            insertStatement.setString(6, regKey);

            int affectedRows = insertStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Новый пользователь не добавлен в БД");
            }

            generatedKeys = insertStatement.getGeneratedKeys();
            int userId = 0;
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            }

            updateStatement = connection.prepareStatement(RESERVE_AUTH_KEY_QUERY);
            updateStatement.setString(1, regKey);

            int updatedRows = updateStatement.executeUpdate();
            if (updatedRows == 0) {
                throw new DAOException("Не удалось сменить состояние регистрационного ключа в бд с false на true");
            }

            connection.commit();

            return userId;


        } catch (SQLException | ConnectionPoolException e) {
            // В случае ошибки откатываем изменения
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    // Логируем ошибку отката
                    e.printStackTrace();
                }
            }
            throw new DAOException("Ошибка при регистрации пользователя: " + e.getMessage(), e);
        } finally {
            // Восстанавливаем автокоммит в нормальное состояние
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    // Логируем ошибку восстановления автокоммита
                    e.printStackTrace();
                }
                try {
                    connection.close(); // Закрытие соединения
                } catch (SQLException e) {
                    e.printStackTrace(); // Логируем ошибку при закрытии соединения
                }
            }

            // Закрытие ресурсов
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (insertStatement != null) {
                    insertStatement.close();
                }
                if (updateStatement != null) {
                    updateStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Метод добавляет новую биографию или обновляет существующую биографию пользователя.
     */
    public boolean addOrUpdateBio(int userId, String newBio) throws DAOException {
        try (Connection connection = connectionPool.takeConnection()) {
            // Проверяем, существует ли запись для данного userId
            try (PreparedStatement selectStatement = connection.prepareStatement(SELECT_BIO_QUERY)) {
                selectStatement.setInt(1, userId);
                ResultSet resultSet = selectStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count > 0) {
                    // Если запись существует, обновляем её
                    try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_BIO_QUERY)) {
                        updateStatement.setString(1, newBio);
                        updateStatement.setInt(2, userId);
                        int affectedRows = updateStatement.executeUpdate();
                        return affectedRows > 0;
                    }
                } else {
                    // Если записи нет, создаем новую
                    try (PreparedStatement insertStatement = connection.prepareStatement(ADD_BIO_QUERY)) {
                        insertStatement.setString(1, newBio);
                        insertStatement.setInt(2, userId);
                        int affectedRows = insertStatement.executeUpdate();
                        return affectedRows > 0;
                    }
                }
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Метод обновляет поле "имя" (информация добавляется в личном кабинете)
     */
    @Override
    public void updateName(int id, String newName) throws DAOException {
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_NAME_QUERY)) {
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Не удалось обновить имя пользователя с ID " + id);
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Метод обновляет поле "email" (информация добавляется в личном кабинете)
     */
    @Override
    public void updateEmail(int id, String newEmail) throws DAOException {
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EMAIL_QUERY)) {
            preparedStatement.setString(1, newEmail);
            preparedStatement.setInt(2, id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Не удалось обновить email пользователя с ID " + id);
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Метод обновляет поле "пароль" (информация добавляется в личном кабинете)
     */
    @Override
    public void updatePassword(int id, String newPassword) throws DAOException {
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PASSWORD_QUERY)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, id);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Не удалось обновить пароль пользователя с ID " + id);
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public UserRole specifyKeyTypeIfItIsNotReserved(String registrationKey) throws DAOException {
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_KEYTYPE_QUERY)) {
            preparedStatement.setString(1, registrationKey);
            preparedStatement.setBoolean(2, false);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return UserRole.valueOf(resultSet.getString("name").toUpperCase());
                }
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
        return null;
    }

    public Map<String, String> getUserProfileById(int id) throws DAOException {
        Map<String, String> userProfile = new HashMap<>();

        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USERPROFILE_QUERY)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String bio = resultSet.getString("bio");

                userProfile.put("email", email);
                userProfile.put("password", password);
                userProfile.put("bio", bio);
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }

        return userProfile;
    }

    public boolean checkTokenPresence(int userId) throws DAOException {
        try (
                Connection connection = connectionPool.takeConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(CHECK_TOKEN_PRESENCE_QUERY);
        ) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
    }

    public Token getFullTokenByUsersId(int userId) throws DAOException {
        try (
                Connection connection = connectionPool.takeConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_TOKEN_QUERY, Statement.RETURN_GENERATED_KEYS);
        ) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    return new Token(
                            resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getTimestamp(3).toLocalDateTime(),
                            resultSet.getTimestamp(4).toLocalDateTime(),
                            userId);
                }
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
        return null;
    }

    public Token saveTokenInDb(int userId, String token) throws DAOException {
        try (
                Connection connection = connectionPool.takeConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SAVE_TOKEN_QUERY, Statement.RETURN_GENERATED_KEYS);
        ) {
            LocalDateTime regDate = LocalDateTime.now();
            int tokenDurationInMonths = 12;
            preparedStatement.setString(1, token);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(regDate));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(regDate.plusMonths(tokenDurationInMonths)));
            preparedStatement.setInt(4, userId);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        System.out.println("Generated key: " + generatedId);

                        return new Token(
                                generatedId,
                                token,
                                regDate,
                                regDate.plusMonths(tokenDurationInMonths),
                                userId
                        );
                    }
                }
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
        return null;
    }

    public boolean deleteTokenFromDb(int userId) throws DAOException {
        try
                (Connection connection = connectionPool.takeConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TOKEN_QUERY)
                ) {
            preparedStatement.setInt(1, userId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
    }

    public User findUserByTokenInDb(String token) throws DAOException {
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_TOKEN_QUERY)) {
            preparedStatement.setString(1, token);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(resultSet.getInt("id"), resultSet.getString("name"), UserRole.valueOf((resultSet.getString("role_name")).toUpperCase()));
                }
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
        return null;
    }
}
