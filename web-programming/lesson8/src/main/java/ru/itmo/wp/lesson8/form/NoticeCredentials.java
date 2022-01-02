package ru.itmo.wp.lesson8.form;

import javax.validation.constraints.*;

public class NoticeCredentials {
    @NotEmpty
    @NotNull
    @Size(min = 10, message = "Size must be greater than 10")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
