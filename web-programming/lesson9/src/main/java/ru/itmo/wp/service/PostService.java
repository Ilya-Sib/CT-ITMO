package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.wp.domain.*;
import ru.itmo.wp.form.PostForm;
import ru.itmo.wp.repository.PostRepository;
import ru.itmo.wp.repository.TagRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    /**
     * @noinspection FieldCanBeLocal, unused
     */
    private final TagRepository tagRepository;

    public PostService(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    public List<Post> findAll() {
        return postRepository.findAllByOrderByCreationTimeDesc();
    }

    public Post find(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Transactional
    public void writeComment(Long postId, User user, Comment comment) {
        Post post = find(postId);
        if (post != null) {
            comment.setUser(user);
            comment.setPost(post);
            post.addComment(comment);
            postRepository.save(post);
        }
    }

    public Post toPost(PostForm postForm) {
        Post post = new Post();

        post.setTitle(postForm.getTitle());
        post.setText(postForm.getText());
        if (!postForm.getTags().isEmpty()) {
            post.setTags(new HashSet<>());
            Arrays.stream(postForm.getTags().trim().split("\\s+"))
                    .filter(s -> !s.isEmpty()).map(String::toUpperCase).forEach(tag -> {
                Tag tagByName = tagRepository.findTagByName(tag);
                if (tagByName == null) {
                    tagByName = new Tag();
                    tagByName.setName(tag);
                    tagRepository.save(tagByName);
                }
                post.addTag(tagByName);
            });
        }

        return post;
    }
}
