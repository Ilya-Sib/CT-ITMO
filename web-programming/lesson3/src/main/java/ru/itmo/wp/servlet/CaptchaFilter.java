package ru.itmo.wp.servlet;

import ru.itmo.wp.util.ImageUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

public class CaptchaFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        if (session.getAttribute("captcha-passed") == null
                && !request.getRequestURI().equals("/favicon.ico")) {
            String actualAnswer = request.getParameter("actual-answer");
            String expectedAnswer = (String) session.getAttribute("expected-answer");
            if (actualAnswer != null && actualAnswer.equals(expectedAnswer)) {
                session.setAttribute("captcha-passed", "yes.");
                response.sendRedirect(request.getRequestURI());
            } else {
                String randomNumber = String.valueOf(100 + new Random().nextInt(900));
                session.setAttribute("expected-answer", randomNumber);
                session.setAttribute("captcha-image",
                        Base64.getEncoder().encodeToString(ImageUtils.toPng(randomNumber)));
                request.getRequestDispatcher("/static/js/captcha.jsp").forward(request, response);
            }
        } else if (request.getParameter("actual-answer") != null) {
            response.sendRedirect(request.getRequestURI());
        } else {
            super.doFilter(request, response, chain);
        }
    }
}
