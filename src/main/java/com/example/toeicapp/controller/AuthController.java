package com.example.toeicapp.controller;

import com.example.toeicapp.model.User;
import com.example.toeicapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password,
                            @RequestParam String confirmPassword, Model model) {
        String trimmedUsername = username == null ? "" : username.trim();

        if (trimmedUsername.isEmpty() || password == null || password.isEmpty()) {
            model.addAttribute("error", "ユーザー名とパスワードを入力してください。");
            model.addAttribute("username", trimmedUsername);
            return "register";
        }
        if (password.length() < 4) {
            model.addAttribute("error", "パスワードは4文字以上にしてください。");
            model.addAttribute("username", trimmedUsername);
            return "register";
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "パスワードが一致しません。");
            model.addAttribute("username", trimmedUsername);
            return "register";
        }
        if (userRepository.existsByUsername(trimmedUsername)) {
            model.addAttribute("error", "そのユーザー名は既に使われています。");
            return "register";
        }

        userRepository.save(new User(trimmedUsername, passwordEncoder.encode(password)));
        return "redirect:/login?registered";
    }
}
