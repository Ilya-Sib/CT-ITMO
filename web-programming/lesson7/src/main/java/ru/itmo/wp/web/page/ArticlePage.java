package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.annotation.Json;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ArticlePage {
    private final UserService userService = new UserService();
    private final ArticleService articleService = new ArticleService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        try {
            userService.validateUser((User) request.getSession().getAttribute("user"));
        } catch (ValidationException e) {
            request.getSession().setAttribute("message", "You have to be logged to access Article");
            throw new RedirectException("/index");
        }
    }

    private void article(HttpServletRequest request, Map<String, Object> view)
            throws ValidationException {
        User user = (User) request.getSession().getAttribute("user");
        try {
            userService.validateUser(user);
        } catch (ValidationException e) {
            request.getSession().setAttribute("message", "You have to be logged to create Article");
            throw new RedirectException("/index");
        }

        Article article = new Article();
        article.setUserId(user.getId());
        article.setTitle(request.getParameter("title"));
        article.setText(request.getParameter("text"));

        articleService.validateNewArticle(article);
        articleService.save(article);

        request.getSession().setAttribute("message", "Article created");
        throw new RedirectException("/index");
    }

    @Json
    private void findAll(HttpServletRequest request, Map<String, Object> view) {
        view.put("articles", articleService.findAllNotHidden());
        view.put("users", userService.findAll());
    }
}
