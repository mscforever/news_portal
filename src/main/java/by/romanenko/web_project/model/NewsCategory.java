package by.romanenko.web_project.model;

import java.util.Objects;

public class NewsCategory {

    private int newsCategoryId;
    private final String name;
    private final String description;

    public NewsCategory(int newsCategoryId, String name, String description) {
        this.newsCategoryId = newsCategoryId;
        this.name = name;
        this.description = description;
    }

    public int getNewsCategoryId() {
        return newsCategoryId;
    }

    public void setNewsCategoryId(int newsCategoryId) {
        this.newsCategoryId = newsCategoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsCategory that = (NewsCategory) o;
        return newsCategoryId == that.newsCategoryId &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newsCategoryId, name, description);
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "newsCategoryId=" + newsCategoryId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
