package com.example.toeicapp.controller;

import com.example.toeicapp.model.Choice;
import com.example.toeicapp.model.Passage;
import com.example.toeicapp.model.Question;
import com.example.toeicapp.repository.PassageRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/passages")
public class PassageController {

    private final PassageRepository passageRepository;

    public PassageController(PassageRepository passageRepository) {
        this.passageRepository = passageRepository;
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        model.addAttribute("passages", passageRepository.findAll());
        model.addAttribute("reviewCount", ReviewTracker.incorrectIds(session).size());
        return "passages";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        Passage passage = passageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Passage not found: " + id));
        model.addAttribute("passage", passage);
        return "quiz";
    }

    @PostMapping("/{id}/submit")
    public String submit(@PathVariable Long id, @RequestParam Map<String, String> params, HttpSession session, Model model) {
        Passage passage = passageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Passage not found: " + id));

        Set<Long> incorrectIds = ReviewTracker.incorrectIds(session);
        List<QuestionResult> results = new ArrayList<>();
        int correctCount = 0;
        for (Question q : passage.getQuestions()) {
            String selectedIdStr = params.get("answer_" + q.getId());
            Long selectedId = (selectedIdStr != null && !selectedIdStr.isEmpty()) ? Long.valueOf(selectedIdStr) : null;
            Choice correctChoice = q.getChoices().stream()
                    .filter(Choice::isCorrect)
                    .findFirst()
                    .orElse(null);
            boolean correct = correctChoice != null && correctChoice.getId().equals(selectedId);
            if (correct) {
                correctCount++;
                incorrectIds.remove(q.getId());
            } else {
                incorrectIds.add(q.getId());
            }
            results.add(new QuestionResult(q, correctChoice, correct));
        }

        model.addAttribute("passage", passage);
        model.addAttribute("results", results);
        model.addAttribute("correctCount", correctCount);
        model.addAttribute("total", passage.getQuestions().size());
        return "result";
    }

    public static class QuestionResult {
        private final Question question;
        private final Choice correctChoice;
        private final boolean correct;

        public QuestionResult(Question question, Choice correctChoice, boolean correct) {
            this.question = question;
            this.correctChoice = correctChoice;
            this.correct = correct;
        }

        public Question getQuestion() { return question; }
        public Choice getCorrectChoice() { return correctChoice; }
        public boolean isCorrect() { return correct; }
    }
}
