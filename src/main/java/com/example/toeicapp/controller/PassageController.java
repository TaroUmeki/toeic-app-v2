package com.example.toeicapp.controller;

import com.example.toeicapp.model.Choice;
import com.example.toeicapp.model.MissedQuestion;
import com.example.toeicapp.model.Passage;
import com.example.toeicapp.model.Question;
import com.example.toeicapp.model.User;
import com.example.toeicapp.repository.MissedQuestionRepository;
import com.example.toeicapp.repository.PassageRepository;
import com.example.toeicapp.repository.StudyActivityRepository;
import com.example.toeicapp.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/passages")
public class PassageController {

    private static final Map<String, String> PART_LABELS = new LinkedHashMap<>();
    static {
        PART_LABELS.put("PART1", "Part1 - 写真描写問題");
        PART_LABELS.put("PART2", "Part2 - 応答問題");
        PART_LABELS.put("PART3", "Part3 - 会話問題");
        PART_LABELS.put("PART4", "Part4 - 説明文問題");
        PART_LABELS.put("PART5", "Part5 - 短文穴埋め問題");
        PART_LABELS.put("PART6", "Part6 - 長文穴埋め問題");
        PART_LABELS.put("PART7", "Part7 - 読解問題");
    }

    private final PassageRepository passageRepository;
    private final UserRepository userRepository;
    private final MissedQuestionRepository missedQuestionRepository;
    private final StudyActivityRepository studyActivityRepository;

    public PassageController(PassageRepository passageRepository, UserRepository userRepository,
                              MissedQuestionRepository missedQuestionRepository,
                              StudyActivityRepository studyActivityRepository) {
        this.passageRepository = passageRepository;
        this.userRepository = userRepository;
        this.missedQuestionRepository = missedQuestionRepository;
        this.studyActivityRepository = studyActivityRepository;
    }

    @GetMapping
    public String list(Principal principal, Model model) {
        User user = CurrentUser.resolve(principal, userRepository);
        Map<String, Long> countsByPart = new LinkedHashMap<>();
        for (Passage p : passageRepository.findAll()) {
            countsByPart.merge(p.getPartType(), 1L, Long::sum);
        }
        List<Part> parts = new ArrayList<>();
        for (Map.Entry<String, String> entry : PART_LABELS.entrySet()) {
            Long count = countsByPart.get(entry.getKey());
            if (count != null) {
                parts.add(new Part(entry.getKey(), entry.getValue(), count));
            }
        }
        model.addAttribute("parts", parts);
        model.addAttribute("reviewCount", missedQuestionRepository.countByUser(user));
        model.addAttribute("streak", StudyActivityTracker.currentStreak(user, studyActivityRepository));
        return "parts";
    }

    @GetMapping("/part/{partType}")
    public String listByPart(@PathVariable String partType, Principal principal, Model model) {
        User user = CurrentUser.resolve(principal, userRepository);
        model.addAttribute("passages", passageRepository.findByPartType(partType, Sort.by("title")));
        model.addAttribute("partType", partType);
        model.addAttribute("partLabel", PART_LABELS.getOrDefault(partType, partType));
        model.addAttribute("reviewCount", missedQuestionRepository.countByUser(user));
        model.addAttribute("streak", StudyActivityTracker.currentStreak(user, studyActivityRepository));
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
        StudyActivityTracker.recordToday(user, studyActivityRepository);

        model.addAttribute("passage", passage);
        model.addAttribute("results", results);
        model.addAttribute("correctCount", correctCount);
        model.addAttribute("total", passage.getQuestions().size());
        return "result";
    }

    public static class Part {
        private final String partType;
        private final String label;
        private final long count;

        public Part(String partType, String label, long count) {
            this.partType = partType;
            this.label = label;
            this.count = count;
        }

        public String getPartType() { return partType; }
        public String getLabel() { return label; }
        public long getCount() { return count; }
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
