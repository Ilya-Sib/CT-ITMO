package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;

import java.util.List;

public interface ArticleRepository {
    Article find(long id);
    List<Article> findAll();
    void save(Article user);
    List<Article> findAllNotHidden();
    List<Article> findAllByUser(User user);
    void updateHidden(Article article, boolean hidden);
}
