package by.romanenko.web_project.dao.impl;

import by.romanenko.web_project.dao.DAOException;
import by.romanenko.web_project.dao.INewsDAO;
import by.romanenko.web_project.dao.dbmanager.ConnectionPool;
import by.romanenko.web_project.dao.dbmanager.ConnectionPoolException;
import by.romanenko.web_project.model.News;
import by.romanenko.web_project.model.NewsImportance;
import by.romanenko.web_project.model.User;
import by.romanenko.web_project.model.UserRole;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NewsDAOImpl implements INewsDAO {
    private static final String ADD_NEWS_QUERY = "INSERT INTO news_management.news (importance, title, image, brief, content, publish_date, categories_id) VALUES (?, ?, ?, ?, ?, ?, (SELECT id FROM news_management.categories WHERE title = ? LIMIT 1))";
    private static final String ADD_PRIMARY_AUTHOR_QUERY = "INSERT INTO news_management.news_authors (news_id, users_id) VALUES ((SELECT id FROM news_management.news WHERE id = ? LIMIT 1), (SELECT id FROM news_management.users WHERE id = ? ))";
    private static final String ADD_COAUTHOR_QUERY = "INSERT INTO news_management.news_authors (news_id, users_id) VALUES (?, ?)";
    private static final String DELETE_NEWS_AUTHORS_QUERY = "DELETE FROM news_management.news_authors WHERE news_id = ?";
    private static final String DELETE_NEWS_QUERY = "DELETE FROM news_management.news WHERE id = ?";
    private static final String GET_ALL_NEWS_QUERY = "SELECT n.id, n.importance, n.title, n.image, n.brief, n.content, n.publish_date, n.categories_id, c.title AS category_title FROM news_management.news n JOIN news_management.categories c ON n.categories_id = c.id ORDER BY n.publish_date DESC";
    private static final String GET_AUTHORS_BY_NEWS_ID_QUERY = "SELECT u.id, u.name FROM news_management.users u JOIN news_management.news_authors na ON u.id = na.users_id WHERE na.news_id = ?";
    private static final String GET_NEWS_BY_ID_QUERY = "SELECT * FROM news_management.news WHERE id = ?";
    private static final String GET_CATEGORY_BY_ID_QUERY = "SELECT title FROM news_management.categories WHERE id = ?";
    private static final String GET_ALL_NEWS_BY_AUTHOR_QUERY = "SELECT news_id FROM news_management.news_authors WHERE users_id = ? ORDER BY (SELECT publish_date FROM news_management.news WHERE id = news_id) DESC";
    private static final String UPDATE_NEWS_QUERY = "UPDATE news_management.news SET importance = ?, title = ?, image = ?, brief = ?, content = ?, publish_date = ?, news.update_date = ?, categories_id = (SELECT id FROM news_management.categories WHERE title = ?) WHERE id = ?";
    private static final String FIND_NEWS_BY_TYPE_QUERY = "SELECT * FROM news_management.news WHERE importance = ?";
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    /**
     * Метод для добавления новости в БД
     *
     * @param news - объект класса News, представляет собой текст новости и сопутствующие поля
     * @return id добавленной новости
     * @throws DAOException
     */
    @Override
    public int addNews(News news) throws DAOException {
        //автора добавляем отдельно
        int id;
        LocalDateTime publishDate;

        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement1 = connection.prepareStatement(ADD_NEWS_QUERY, Statement.RETURN_GENERATED_KEYS);) {
            preparedStatement1.setString(1, news.getImportance().name());
            preparedStatement1.setString(2, news.getTitle());
            preparedStatement1.setString(3, news.getImageUrl());
            preparedStatement1.setString(4, news.getBrief());
            preparedStatement1.setString(5, news.getContent());
            publishDate = LocalDateTime.now();
            preparedStatement1.setTimestamp(6, Timestamp.valueOf(publishDate));
            preparedStatement1.setString(7, news.getCategory());

            int affectedRows = preparedStatement1.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet resultSet1 = preparedStatement1.getGeneratedKeys()) {
                    if (resultSet1.next()) {
                        id = resultSet1.getInt(1);
                    } else {
                        throw new SQLException("Не удалось получить ID новостей");
                    }
                }
            } else {
                throw new SQLException("Не добавлены строки в таблицу news");
            }

        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
        return id;
    }

    /**
     * Метод для добавления первоначального автора в новость. У новости может быть несколько авторов, соавторы добавляются с помощью метода addCoauthor.
     *
     * @param newsId - id новости, к которой будет добавляться автор
     * @param authId - id первоначального автора, который будет закреплен за новостью
     * @return true в случае успешного добавления автора
     * @throws DAOException
     */
    public boolean addPrimaryAuthor(int newsId, int authId) throws DAOException {
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement2 = connection.prepareStatement(ADD_PRIMARY_AUTHOR_QUERY)) {
            // хоть у нас возможно авторство нескольких, но при добавлении статьи за новостью числится тот, от чьего id зашли в кабинет и отправили новость
            preparedStatement2.setInt(1, newsId);
            preparedStatement2.setInt(2, authId);
            int affectedRowsForAuthor = preparedStatement2.executeUpdate();
            return affectedRowsForAuthor > 0;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Метод для добавления соавтора автора в новость. У новости может быть несколько авторов, первоначальный автор добавляется с помощью метода addPrimaryAuthor.
     *
     * @param coauthorId - id соавтора, который вносил изменения в новость
     * @param newsId     - id новости, за которой должен быть закреплен соавтор
     * @return true в случае успешного добавления автора
     * @throws DAOException
     */
    public boolean addCoauthor(int coauthorId, int newsId) throws DAOException {
        //здесь мы уже не update запрос делаем, а именно добавляем еще одного автора
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_COAUTHOR_QUERY)) {
            preparedStatement.setInt(1, newsId);
            preparedStatement.setInt(2, coauthorId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException("Не удалось добавить соавтора в новость " + newsId, e);
        }
    }

    /**
     * Метод для удаления новости из БД
     *
     * @param newsId - id новости для удаления
     * @return true в случае успешного удаления новости
     * @throws DAOException
     */
    public boolean deleteNews(int newsId) throws DAOException {
        // Сначала удаляем связанные записи в news_authors
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement deleteAuthorsStatement = connection.prepareStatement(DELETE_NEWS_AUTHORS_QUERY);
             PreparedStatement deleteNewsStatement = connection.prepareStatement(DELETE_NEWS_QUERY)
        ) {
            // Удаляем связанные записи
            deleteAuthorsStatement.setInt(1, newsId);
            deleteAuthorsStatement.executeUpdate();

            // Теперь удаляем саму новость
            deleteNewsStatement.setInt(1, newsId);
            int rowsAffected = deleteNewsStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Новость успешно удалена.");
                return true;
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
        return false;
    }

    /**
     * Метод для получения полного списка новостей всех категорий и авторов
     *
     * @return список объектов типа News
     * @throws DAOException
     */
    public List<News> getAllNews() throws DAOException {
        List<News> newsList = new ArrayList<>();

        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_NEWS_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                NewsImportance importance = NewsImportance.valueOf(resultSet.getString("importance"));
                String title = resultSet.getString("title");
                String imageUrl = resultSet.getString("image");
                String brief = resultSet.getString("brief");
                String content = resultSet.getString("content");
                LocalDateTime publishDate = resultSet.getTimestamp("publish_date").toLocalDateTime();
                String categoryTitle = resultSet.getString("category_title");  // category_title из таблицы categories

                List<User> newsAuthors = getNewsAuthorsByNewsId(id);

                newsList.add(new News(id, importance, title, imageUrl, brief, content, publishDate, newsAuthors, categoryTitle));
            }

        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }

        return newsList;
    }

    /**
     * Метод для получения списка авторов, закреплённых за новостью
     *
     * @param newsId - id новости, информацию об авторах которой необходимо получить
     * @return список объектов типа User
     * @throws DAOException
     */
    public List<User> getNewsAuthorsByNewsId(int newsId) throws DAOException {
        List<User> authors = new ArrayList<>();

        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_AUTHORS_BY_NEWS_ID_QUERY)) {
            preparedStatement.setInt(1, newsId);  // Устанавливаем id новости в запрос
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String name = resultSet.getString("name");

                    authors.add(new User(userId, name, UserRole.AUTHOR));
                }
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
        return authors;
    }

    /**
     * Метод для получения новости по ее id
     *
     * @param id - id новости, полную информацию о которой, необходимо получить
     * @return объект типа News
     * @throws DAOException
     */
    public News getNewsById(int id) throws DAOException {
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_NEWS_BY_ID_QUERY)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int categoryId = resultSet.getInt("categories_id");

                    // Теперь выполняем запрос, чтобы получить title категории
                    try (PreparedStatement categoryStatement = connection.prepareStatement(GET_CATEGORY_BY_ID_QUERY)) {
                        categoryStatement.setInt(1, categoryId);
                        try (ResultSet categoryResultSet = categoryStatement.executeQuery()) {
                            String categoryTitle = null;
                            if (categoryResultSet.next()) {
                                categoryTitle = categoryResultSet.getString("title");
                            }


                            return new News(id,
                                    NewsImportance.valueOf(resultSet.getString("importance")),
                                    resultSet.getString("title"),
                                    resultSet.getString("image"),
                                    resultSet.getString("brief"),
                                    resultSet.getString("content"),
                                    resultSet.getTimestamp("publish_date").toLocalDateTime(),
                                    getNewsAuthorsByNewsId(id),
                                    categoryTitle);  // Передаем categoryTitle
                        }
                    }
                }
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
        return null;
    }

    /**
     * Метод для получения списка новостей, написанных определённым автором
     *
     * @param authorId - id автора
     * @return список объектов типа News
     * @throws DAOException
     */
    public List<News> getAllNewsByAuthor(int authorId) throws DAOException {
        List<News> resList = new ArrayList<>();
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_NEWS_BY_AUTHOR_QUERY)) {
            preparedStatement.setInt(1, authorId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    resList.add(getNewsById(resultSet.getInt("news_id")));
                }

            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
        return resList;
    }

    /**
     * Метод для изменения объекта типа News по id
     *
     * @param newsId - id новости, которую следует изменить
     * @param news   - информация, которую следует переписать в новости
     * @return true в случае успешного изменения
     * @throws DAOException
     */
    public boolean updateNewsArticle(int newsId, News news) throws DAOException {
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_NEWS_QUERY)) {

            preparedStatement.setString(1, news.getImportance().name());
            preparedStatement.setString(2, news.getTitle());
            preparedStatement.setString(3, news.getImageUrl());
            preparedStatement.setString(4, news.getBrief());
            preparedStatement.setString(5, news.getContent());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(news.getPublishDate()));
            preparedStatement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(8, news.getCategory());
            preparedStatement.setInt(9, newsId);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException("Ошибка при обновлении новости с ID " + newsId + ": " + e.getMessage(), e);
        }
    }

    /**
     * Метод для получения списка новостей определённой степени важности (breaking, top, regular)
     *
     * @param newsImportance - тип важности новости
     * @return список объектов типа News
     * @throws DAOException
     */
    public List<News> findNewsByType(NewsImportance newsImportance) throws DAOException {
        List<News> resList = new ArrayList<>();
        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_NEWS_BY_TYPE_QUERY)) {
            preparedStatement.setString(1, newsImportance.name());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    resList.add(getNewsById(resultSet.getInt("id")));
                }
            }
        } catch (SQLException | ConnectionPoolException e) {
            throw new DAOException(e);
        }
        return resList;
    }
}

