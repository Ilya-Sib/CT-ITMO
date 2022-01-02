package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.form.CommentForm;
import ru.itmo.wp.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> findAll() {
        return commentRepository.findAllByOrderByCreationTimeDesc();
    }

    @Transactional
    public void save(CommentForm commentForm) {
        commentRepository.save(toComment(commentForm));
    }

    private Comment toComment(CommentForm commentForm) {
        Comment comment = new Comment();

        comment.setText(commentForm.getText());
        comment.setUser(commentForm.getUser());
        comment.setPost(commentForm.getPost());

        return comment;
    }
}
