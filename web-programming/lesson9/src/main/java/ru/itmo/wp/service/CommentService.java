package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.form.CommentForm;

@Service
public class CommentService {
    public Comment toComment(CommentForm commentForm) {
        Comment comment = new Comment();
        comment.setText(commentForm.getText());
        return comment;
    }
}
