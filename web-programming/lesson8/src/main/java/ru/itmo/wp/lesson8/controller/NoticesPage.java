package ru.itmo.wp.lesson8.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.lesson8.form.NoticeCredentials;
import ru.itmo.wp.lesson8.form.validator.NoticeCreationValidator;
import ru.itmo.wp.lesson8.service.NoticeService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/notices")
public class NoticesPage extends Page {
    private final NoticeService noticeService;

    public NoticesPage(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping
    public String createNotices(Model model) {
        model.addAttribute("noticeFrom", new NoticeCredentials());
        return "NoticesPage";
    }

    @PostMapping
    public String newNotice(@Valid @ModelAttribute("noticeFrom") NoticeCredentials noticeForm,
                            BindingResult bindingResult,
                            HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return "NoticesPage";
        }

        noticeService.createNotice(noticeForm);
        setMessage(httpSession, "Congrats, notice have been created!");
        return "redirect:/";
    }
}
