package ru.itmo.wp.form;

import ru.itmo.wp.domain.User;

import javax.persistence.Lob;
import javax.validation.constraints.*;

public class PostForm {
    @NotEmpty
    @Size(min = 1, max = 100)
    private String title;


    @NotEmpty
    @Size(min = 1, max = 10000)
    @Lob
    private String text;

    @NotNull
    private User user;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
