package ru.itmo.wp.web.page;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public abstract class Page {
    private final UserService userService = new UserService();
    private HttpServletRequest request;

    private void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    protected void before(HttpServletRequest request, Map<String, Object> view) {
        this.request = request;
        view.put("userCount", userService.findCount());
        putUser(request, view);
        putMessage(request, view);
    }

    private void putUser(HttpServletRequest request, Map<String, Object> view) {
        User user = getUser();
        if (user != null) {
            view.put("user", user);
        }
    }

    public void setUser(User user) {
        request.getSession().setAttribute("user", user);
    }

    public User getUser() {
        return (User) request.getSession().getAttribute("user");
    }

    public void setMessage(String message) {
        request.getSession().setAttribute("message", message);
    }

    public String getMessage() {
        return (String) request.getSession().getAttribute("message");
    }

    public void removeMessage(HttpServletRequest request) {
        request.getSession().removeAttribute("message");
    }

    private void putMessage(HttpServletRequest request, Map<String, Object> view) {
        String message = getMessage();
        if (!Strings.isNullOrEmpty(message)) {
            view.put("message", message);
            removeMessage(request);
        }
    }

    protected void after(HttpServletRequest request, Map<String, Object> view) {
        // Do nothing
    }
}
