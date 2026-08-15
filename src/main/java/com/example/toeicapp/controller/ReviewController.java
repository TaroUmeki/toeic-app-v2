package com.example.toeicapp.controller;

import com.example.toeicapp.model.Choice;
import com.example.toeicapp.model.MissedQuestion;
import com.example.toeicapp.model.Passage;
import com.example.toeicapp.model.Question;
import com.example.toeicapp.model.User;
import com.example.toeicapp.repository.MissedQuestionRepository;
import com.example.toeicapp.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/review")
public class ReviewController {

    private final UserRepository userRepository;
    private final MissedQuestionRepository missedQuestionRepository;

    public ReviewController(UserRepository userRepository, MissedQuestionRepository missedQuestionRepository) {
        this.userRepository = userRepository;
        this.missedQuestionRepository = missedQuestionRepository;
    }

    @GetMapping
    public String review(Principal principal, Model model) {
        User user = CurrentUser.resolve(principal, userRepository);
        List<Question> questions = missedQuestionRepository.findByUser(user).stream()
                .map(MissedQuestion::getQuestion)
                .collect(Collectors.toList());

        Map<Long, List<Question>> byPassageId = questions.stream()
                .collect(Collectors.groupingBy(q -> q.getPassage().getId(), LinkedHashMap::new, Collectors.toList()));
        List<ReviewGroup> groups = byPassageId.values().stream()
                .map(qs -> new ReviewGroup(qs.get(0).getPassage(), qs))
                .collect(Collectors.toList());

        model.addAttribute("groups", groups);
        return "review";
    }

    @PostMapping("/submit")
    public String submit(@RequestParam Map<String, String> params, Principal principal, Model model) {
        User user = CurrentUser.resolve(principal, userRepository);
        List<Question> questions = missedQuestionRepository.findByUser(user).stream()
                .map(MissedQuestion::getQuestion)
                .collect(Collectors.toList());

        List<PassageController.QuestionResult> results = new ArrayList<>();
        int correctCount = 0;
        for (Question q : questions) {
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
            }
            results.add(new PassageController.QuestionResult(q, correctChoice, correct));
        }

        model.addAttribute("results", results);
        model.addAttribute("correctCount", correctCount);
        model.addAttribute("total", questions.size());
        return "review_result";
    }

    public static class ReviewGroup {
        private final Passage passage;
        private final List<Question> questions;

        public ReviewGroup(Passage passage, List<Question> questions) {
            this.passage = passage;
            this.questions = questions;
        }

        public Passage getPassage() { return passage; }
        public List<Question> getQuestions() { return questions; }
    }
}
