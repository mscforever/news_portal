package by.romanenko.web_project.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Token {
    private int id;
    private String tokenName;
    private LocalDateTime tokenRegDate;
    private LocalDateTime tokenExpirationDate;
    private int userId;

    public Token(String tokenName) {
        this.tokenName = tokenName;
    }

    public Token(int id, String tokenName, LocalDateTime tokenRegDate, LocalDateTime tokenExpirationDate, int userId) {
        this.id = id;
        this.tokenName = tokenName;
        this.tokenRegDate = tokenRegDate;
        this.tokenExpirationDate = tokenExpirationDate;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public LocalDateTime getTokenRegDate() {
        return tokenRegDate;
    }

    public void setTokenRegDate(LocalDateTime tokenRegDate) {
        this.tokenRegDate = tokenRegDate;
    }

    public LocalDateTime getTokenExpirationDate() {
        return tokenExpirationDate;
    }

    public void setTokenExpirationDate(LocalDateTime tokenExpirationDate) {
        this.tokenExpirationDate = tokenExpirationDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return id == token.id && userId == token.userId && Objects.equals(tokenName, token.tokenName) && Objects.equals(tokenRegDate, token.tokenRegDate) && Objects.equals(tokenExpirationDate, token.tokenExpirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tokenName, tokenRegDate, tokenExpirationDate, userId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", tokenName='" + tokenName + '\'' +
                ", tokenRegDate=" + tokenRegDate +
                ", tokenExpirationDate=" + tokenExpirationDate +
                ", userId=" + userId +
                '}';
    }
}
