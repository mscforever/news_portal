package by.romanenko.web_project.model;

public enum ProfileDataField {
    NAME("имя"),
    EMAIL("e-mail"),
    PASSWORD("пароль"),
    BIO("биография");

    private final String description;

    ProfileDataField(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
