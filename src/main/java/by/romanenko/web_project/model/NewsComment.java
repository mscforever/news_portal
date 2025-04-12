package by.romanenko.web_project.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class NewsComment implements Serializable {
    private static final long serialVersionUID = 1L;

    private int commentId;
    private int commentedNewsId;
    //это может быть как автор, так и пользователь
    private String loggedVisitorEmail;
    private String commentText;
    private LocalDateTime dateCreated;

    public NewsComment(int commentId, int commentedNewsId, String loggedVisitorEmail, String commentText, LocalDateTime dateCreated) {
        this.commentId = commentId;
        this.commentedNewsId = commentedNewsId;
        this.loggedVisitorEmail = loggedVisitorEmail;
        this.commentText = commentText;
        this.dateCreated = dateCreated;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getCommentedNewsId() {
        return commentedNewsId;
    }

    public void setCommentedNewsId(int commentedNewsId) {
        this.commentedNewsId = commentedNewsId;
    }

    public String getLoggedVisitorEmail() {
        return loggedVisitorEmail;
    }

    public void setLoggedVisitorEmail(String loggedVisitorEmail) {
        this.loggedVisitorEmail = loggedVisitorEmail;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsComment that = (NewsComment) o;
        return commentId == that.commentId && commentedNewsId == that.commentedNewsId && Objects.equals(loggedVisitorEmail, that.loggedVisitorEmail) && Objects.equals(commentText, that.commentText) && Objects.equals(dateCreated, that.dateCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, commentedNewsId, loggedVisitorEmail, commentText, dateCreated);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "commentId=" + commentId +
                ", commentedNewsId=" + commentedNewsId +
                ", loggedVisitorEmail='" + loggedVisitorEmail + '\'' +
                ", commentText='" + commentText + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                '}';
    }
}
