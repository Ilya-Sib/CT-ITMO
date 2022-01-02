package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.form.CommentForm;
import ru.itmo.wp.service.CommentService;
import ru.itmo.wp.service.PostService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/post")
public class PostPage extends Page {
    private final PostService postService;
    private final CommentService commentService;

    public PostPage(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    public String writePostGet(@PathVariable Long id,
                               Model model) {
        model.addAttribute("post", postService.find(id));
        model.addAttribute("comment", new Comment());
        return "PostPage";
    }

    @PostMapping("/{id}")
    public String writeCommentPost(@Valid @ModelAttribute CommentForm comment,
                                   @PathVariable Long id,
                                   BindingResult bindingResult,
                                   HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return "PostPage";
        }

        postService.writeComment(id, getUser(httpSession), commentService.toComment(comment));
        putMessage(httpSession, "You wrote the comment!");

        return "redirect:/post/" + id;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private String illegalArgumentHandler() {
        return "PostPage";
    }
}
