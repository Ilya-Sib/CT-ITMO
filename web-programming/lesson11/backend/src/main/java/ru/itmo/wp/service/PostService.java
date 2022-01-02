package ru.itmo.wp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.form.PostForm;
import ru.itmo.wp.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final JwtService jwtService;

    public PostService(PostRepository postRepository, JwtService jwtService) {
        this.postRepository = postRepository;
        this.jwtService = jwtService;
    }

    public List<Post> findAll() {
        return postRepository.findAllByOrderByCreationTimeDesc();
    }

    @Transactional
    public void register(PostForm postForm) {
        postRepository.save(toPost(postForm));
    }

    private Post toPost(PostForm postForm) {
        Post post = new Post();

        post.setTitle(postForm.getTitle());
        post.setText(postForm.getText());
        post.setUser(postForm.getUser());

        return post;
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }
}
