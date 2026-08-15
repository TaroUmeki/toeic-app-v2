package com.example.toeicapp.controller;

import jakarta.servlet.http.HttpSession;

import java.util.LinkedHashSet;
import java.util.Set;

final class ReviewTracker {

    private static final String SESSION_KEY = "incorrectQuestionIds";

    private ReviewTracker() {}

    @SuppressWarnings("unchecked")
    static Set<Long> incorrectIds(HttpSession session) {
        Set<Long> ids = (Set<Long>) session.getAttribute(SESSION_KEY);
        if (ids == null) {
            ids = new LinkedHashSet<>();
            session.setAttribute(SESSION_KEY, ids);
        }
        return ids;
    }
}
