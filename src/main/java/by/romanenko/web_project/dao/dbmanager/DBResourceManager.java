package by.romanenko.web_project.dao.dbmanager;

import java.util.ResourceBundle;

/**
 * Класс предоставляет доступ к ресурсам конфигурации,
 * хранящимся в файле свойств (properties file). Этот класс реализует
 * паттерн Singleton, обеспечивая единственный экземпляр для доступа
 * к ресурсам в приложении.
 * <p>
 * Файл свойств должен находиться в classpath, в папке
 * src/main/resources, и будет автоматически скопирован в выходной
 * каталог (target/classes для проектов, использующих Maven).
 */
public class DBResourceManager {
    private final static DBResourceManager instance = new DBResourceManager();
    private ResourceBundle bundle = ResourceBundle.getBundle("db");

    public static DBResourceManager getInstance() {
        return instance;
    }

    public String getValue(String key) {
        return bundle.getString(key);
    }
}