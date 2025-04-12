package by.romanenko.web_project.model;

import java.io.Serializable;
import java.util.Objects;

public class Favorites implements Serializable {
    private static final long serialVersionUID = 1L;

    //это может быть как автор, так и пользователь
    private String loggedVisitorEmail;
    private int newsId;

    // Конструктор
    public Favorites(String loggedVisitorEmail, int newsId) {
        this.loggedVisitorEmail = loggedVisitorEmail;
        this.newsId = newsId;
    }

    // Геттеры и сеттеры
    public String getLoggedVisitorEmail() {
        return loggedVisitorEmail;
    }

    public void setLoggedVisitorEmail(String loggedVisitorEmail) {
        this.loggedVisitorEmail = loggedVisitorEmail;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    // Переопределяем equals и hashCode для корректного сравнения объектов
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorites favorites = (Favorites) o;
        return loggedVisitorEmail.equals(favorites.loggedVisitorEmail) && newsId == favorites.newsId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(loggedVisitorEmail, newsId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "loggedVisitorEmail=" + loggedVisitorEmail +
                ", newsId=" + newsId +
                '}';
    }
}
