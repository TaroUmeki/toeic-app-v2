package com.example.toeicapp.controller;

import com.example.toeicapp.model.User;
import com.example.toeicapp.repository.UserRepository;

import java.security.Principal;

final class CurrentUser {

    private CurrentUser() {}

    static User resolve(Principal principal, UserRepository userRepository) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Unknown user: " + principal.getName()));
    }
}
