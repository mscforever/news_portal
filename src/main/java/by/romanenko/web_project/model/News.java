package by.romanenko.web_project.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class News implements Serializable {
    private static final long serialVersionUID = 1L;

    private int newsId;
    private NewsImportance importance;
    private String title;
    private String imageUrl;
    private String brief;
    private String content;
    private LocalDateTime publishDate;
    private List<User> newsAuthor;
    //впоследствии заменить на NewsCategory?
    private String category;

    public News() {
    }

    public News(NewsImportance importance, String title, String imageUrl, String brief, String content, List<User> newsAuthor, String category) {
        this.importance = importance;
        this.title = title;
        this.imageUrl = imageUrl;
        this.brief = brief;
        this.content = content;
        this.newsAuthor = newsAuthor;
        this.category = category;
    }

    public News(int newsId, NewsImportance importance, String title, String imageUrl, String brief, String content, LocalDateTime publishDate, List<User> newsAuthor, String category) {
        this.newsId = newsId;
        this.importance = importance;
        this.title = title;
        this.imageUrl = imageUrl;
        this.brief = brief;
        this.content = content;
        this.publishDate = publishDate;
        this.newsAuthor = newsAuthor;
        this.category = category;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public NewsImportance getImportance() {
        return importance;
    }

    public void setImportance(NewsImportance importance) {
        this.importance = importance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public List<User> getNewsAuthor() {
        return newsAuthor;
    }

    public void setNewsAuthor(List<User> newsAuthor) {
        this.newsAuthor = newsAuthor;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    //чтобы избавиться от обновления полей путем комбо "проверка на null + проверка на empty + set поле"
    public void updateFields(String title, String brief, String content, String category) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title;
        }
        if (brief != null && !brief.trim().isEmpty()) {
            this.brief = brief;
        }
        if (content != null && !content.trim().isEmpty()) {
            this.content = content;
        }
        if (category != null && !category.trim().isEmpty()) {
            this.category = category;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return newsId == news.newsId && importance == news.importance && Objects.equals(title, news.title) && Objects.equals(imageUrl, news.imageUrl) && Objects.equals(brief, news.brief) && Objects.equals(content, news.content) && Objects.equals(publishDate, news.publishDate) && Objects.equals(newsAuthor, news.newsAuthor) && Objects.equals(category, news.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newsId, importance, title, imageUrl, brief, content, publishDate, newsAuthor, category);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
               "newsId=" + newsId +
               ", importance=" + importance +
               ", title='" + title + '\'' +
               ", imageUrl='" + imageUrl + '\'' +
               ", brief='" + brief + '\'' +
               ", content='" + content + '\'' +
               ", publishDate=" + publishDate +
               ", newsAuthor=" + newsAuthor +
               ", category='" + category + '\'' +
               '}';
    }
}
