package ru.itmo.wp.lesson8.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itmo.wp.lesson8.service.UserService;

@Controller
@RequestMapping("/user")
public class UserPage extends Page {
    private final UserService userService;

    public UserPage(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String userProfile(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "UserPage";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String numberFormatHandler(Model model) {
        setNotices(model);
        return "UserPage";
    }
}
