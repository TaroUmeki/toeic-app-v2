package com.example.toeicapp.controller;

import com.example.toeicapp.model.Choice;
import com.example.toeicapp.model.MissedQuestion;
import com.example.toeicapp.model.Passage;
import com.example.toeicapp.model.Question;
import com.example.toeicapp.model.User;
import com.example.toeicapp.repository.MissedQuestionRepository;
import com.example.toeicapp.repository.PassageRepository;
import com.example.toeicapp.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/passages")
public class PassageController {

    private final PassageRepository passageRepository;
    private final UserRepository userRepository;
    private final MissedQuestionRepository missedQuestionRepository;

    public PassageController(PassageRepository passageRepository, UserRepository userRepository,
                              MissedQuestionRepository missedQuestionRepository) {
        this.passageRepository = passageRepository;
        this.userRepository = userRepository;
        this.missedQuestionRepository = missedQuestionRepository;
    }

    @GetMapping
    public String list(Principal principal, Model model) {
        User user = CurrentUser.resolve(principal, userRepository);
        model.addAttribute("passages", passageRepository.findAll());
        model.addAttribute("reviewCount", missedQuestionRepository.countByUser(user));
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
    public String submit(@PathVariable Long id, @RequestParam Map<String, String> params, Principal principal, Model model) {
        Passage passage = passageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Passage not found: " + id));
        User user = CurrentUser.resolve(principal, userRepository);

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
                missedQuestionRepository.findByUserAndQuestion(user, q).ifPresent(missedQuestionRepository::delete);
            } else if (missedQuestionRepository.findByUserAndQuestion(user, q).isEmpty()) {
                missedQuestionRepository.save(new MissedQuestion(user, q));
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
