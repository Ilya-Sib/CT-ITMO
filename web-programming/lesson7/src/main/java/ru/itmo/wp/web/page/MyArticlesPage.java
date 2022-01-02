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

public class MyArticlesPage {
    private final UserService userService = new UserService();
    private final ArticleService articleService = new ArticleService();

    protected void action(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        try {
            userService.validateUser(user);
        } catch (ValidationException e) {
            request.getSession().setAttribute("message", "You have to be logged to see Articles");
            throw new RedirectException("/index");
        }

        view.put("articles", articleService.findAllByUser(user));
    }

    @Json
    private void changeHidden(HttpServletRequest request, Map<String, Object> view) {
        User sessionUser = (User) request.getSession().getAttribute("user");
        String articleId = request.getParameter("articleId");

        try {
            userService.validateChangeHidden(sessionUser, articleId);
        } catch (ValidationException e) {
            request.getSession().setAttribute("message", e.getMessage());
            throw new RedirectException("/index");
        }

        Article article = articleService.find(Long.parseLong(articleId));

        try {
            articleService.validateArticle(article, sessionUser);
        } catch (ValidationException e) {
            request.getSession().setAttribute("message", "You can't change this article");
            throw new RedirectException("/index");
        }

        String buttonText = request.getParameter("buttonText");
        if (buttonText == null) {
            request.getSession().setAttribute("message", "Button text required");
            throw new RedirectException("/index");
        }

        articleService.updateHidden(article, !buttonText.equals("Show"));
    }

    @Json
    private void findAll(HttpServletRequest request, Map<String, Object> view) {
        view.put("articles", articleService.findAllByUser(
                (User) request.getSession().getAttribute("user")));
        view.put("users", userService.findAll());
    }
}
