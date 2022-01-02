package ru.itmo.wp.lesson8.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itmo.wp.lesson8.form.UserDisableCredentials;
import ru.itmo.wp.lesson8.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UsersPage extends Page {
    private final UserService userService;

    public UsersPage(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "UsersPage";
    }

    @PostMapping("/changeStatus")
    public String users(@Valid UserDisableCredentials userDisableFrom) {
        userService.updateStatus(userDisableFrom.getUserId(), userDisableFrom.getStatus());
        return "redirect:/users/all";
    }
}
