package by.romanenko.web_project.dao;

public class DAOException extends Exception {
    private static final long serialVersionUID = 1L;

    public DAOException() {
        super();
    }

    public DAOException(String message) {
        super(message);
    }

    //позволит обрабатывать не только Exception, но и другие типы ошибок, такие как RuntimeException, Error
    public DAOException(Throwable cause) {
        super(cause);
    }

    // Конструктор с сообщением и причиной
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}