package by.romanenko.web_project.dao.dbmanager;

/**
 * Класс содержит константы, представляющие ключи
 * параметров конфигурации базы данных, используемых в приложении.
 * ТЕОРЕТИЧЕСКИ МОЖНО ЗАМЕНИТЬ НА ЕНАМ
 */
public final class DBParameter {
    private DBParameter() {
    }

    public static final String DB_DRIVER = "db.driver";
    public static final String DB_URL = "db.url";
    public static final String DB_USER = "db.user";
    public static final String DB_PASSWORD = "db.password";
    public static final String DB_POLL_SIZE = "db.poolsize";
}