package ru.itmo.wp.model.service;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.ArticleRepository;
import ru.itmo.wp.model.repository.impl.ArticleRepositoryImpl;

import java.util.List;

public class ArticleService {
    private final ArticleRepository articleRepository = new ArticleRepositoryImpl();

    public void validateNewArticle(Article article) throws ValidationException {
        validate(article.getTitle(), "Title", ".+", 5, 100);
        validate(article.getText(), "Text", ".*", 10, 255);
    }

    private void validate(String field, String fieldName, String regex, int minLength, int maxLength)
            throws ValidationException {
        if (Strings.isNullOrEmpty(field)) {
            throw new ValidationException(fieldName + " is required");
        }
        if (!field.matches(regex)) {
            throw new ValidationException("Invalid " + fieldName);
        }
        if (field.length() < minLength) {
            throw new ValidationException(fieldName + " can't be shorter than " + minLength + " characters");
        }
        if (field.length() > maxLength) {
            throw new ValidationException(fieldName + " can't be longer than " + maxLength +  " characters");
        }
    }

    public void save(Article article) {
        articleRepository.save(article);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public List<Article> findAllNotHidden() {
        return articleRepository.findAllNotHidden();
    }

    public List<Article> findAllByUser(User user) {
        return articleRepository.findAllByUser(user);
    }

    public Article find(long id) {
        return articleRepository.find(id);
    }


    public void updateHidden(Article article, boolean hidden) {
        articleRepository.updateHidden(article, hidden);
    }

    public void validateArticle(Article article, User user) throws ValidationException {
        if (article == null || article.getUserId() != user.getId()) {
            throw new ValidationException("Bad article");
        }
    }
}
