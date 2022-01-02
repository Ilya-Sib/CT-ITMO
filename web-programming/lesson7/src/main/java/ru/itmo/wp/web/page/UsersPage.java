package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.annotation.Json;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/** @noinspection unused*/
public class UsersPage {
    private final UserService userService = new UserService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        userService.updateSessionUser(request.getSession());
    }

    private void findAll(HttpServletRequest request, Map<String, Object> view) {
        view.put("users", userService.findAll());
    }

    private void findUser(HttpServletRequest request, Map<String, Object> view) {
        view.put("user",
                userService.find(Long.parseLong(request.getParameter("userId"))));
    }

    @Json
    private void addAdmin(HttpServletRequest request, Map<String, Object> view) {
        userService.updateSessionUser(request.getSession());
        User sessionUser = (User) request.getSession().getAttribute("user");
        String userId = request.getParameter("userId");

        try {
            userService.validateAdmin(sessionUser, userId);
        } catch (ValidationException e) {
            request.getSession().setAttribute("message", e.getMessage());
            throw new RedirectException("/index");
        }

        User paramUser = userService.find(Long.parseLong(userId));

        try {
            userService.validateUser(paramUser);
        } catch (ValidationException e) {
            request.getSession().setAttribute("message", "Unknown target");
            throw new RedirectException("/index");
        }

        String buttonText = request.getParameter("buttonText");
        if (buttonText == null || (!buttonText.equals("enable") && !buttonText.equals("disable"))) {
            request.getSession().setAttribute("message", "Unknown button text");
            throw new RedirectException("/index");
        }

        userService.updateAdmin(paramUser, buttonText.equals("enable"));
        userService.updateSessionUser(request.getSession());
    }
}
