package com.example.toeicapp.config;

import com.example.toeicapp.model.Choice;
import com.example.toeicapp.model.Passage;
import com.example.toeicapp.model.Question;
import com.example.toeicapp.model.User;
import com.example.toeicapp.repository.PassageRepository;
import com.example.toeicapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeedConfig {

    @Bean
    public CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("Taro").isEmpty()) {
                userRepository.save(new User("Taro", passwordEncoder.encode("pass")));
            }
        };
    }

    @Bean
    public CommandLineRunner seedPassages(PassageRepository passageRepository) {
        return args -> {
            if (passageRepository.count() > 0) {
                return;
            }

            Passage part5 = new Passage();
            part5.setTitle("Part5 sample");
            part5.setPartType("PART5");
            part5.setSkillType("READING");
            part5.setBody("");

            Question q1 = new Question();
            q1.setQuestionText("The report ___ by Friday.");
            q1.setExplanation("Future passive.");
            part5.addQuestion(q1);
            addChoice(q1, "A", "is finishing", false);
            addChoice(q1, "B", "is going to complete", false);
            addChoice(q1, "C", "will be completed", true);
            addChoice(q1, "D", "was completed", false);

            Passage part6 = new Passage();
            part6.setTitle("Part6 sample");
            part6.setPartType("PART6");
            part6.setSkillType("READING");
            part6.setBody("Dear team,\n\n"
                    + "Please review the attached report [1] provide feedback by end of day. "
                    + "The report should be [2] before the meeting.\n\n"
                    + "Best,\nManager");

            Question q2 = new Question();
            q2.setQuestionText("空欄(1)に入る最も適切な語を選びなさい。");
            q2.setBlankNumber(1);
            q2.setExplanation("Connective phrase.");
            part6.addQuestion(q2);
            addChoice(q2, "A", "and", true);
            addChoice(q2, "B", "so", false);
            addChoice(q2, "C", "but", false);
            addChoice(q2, "D", "or", false);

            Question q3 = new Question();
            q3.setQuestionText("空欄(2)に入る最も適切な語を選びなさい。");
            q3.setBlankNumber(2);
            q3.setExplanation("Timing.");
            part6.addQuestion(q3);
            addChoice(q3, "A", "sent", false);
            addChoice(q3, "B", "finalized", true);
            addChoice(q3, "C", "delayed", false);
            addChoice(q3, "D", "ignored", false);

            Passage part7 = new Passage();
            part7.setTitle("Part7 sample");
            part7.setPartType("PART7");
            part7.setSkillType("READING");
            part7.setBody("Announcement:\nOffice will be closed next Monday for maintenance.");

            Question q4 = new Question();
            q4.setQuestionText("When is the office closed?");
            q4.setExplanation("Reading comprehension.");
            part7.addQuestion(q4);
            addChoice(q4, "A", "This Friday", false);
            addChoice(q4, "B", "Next Monday", true);
            addChoice(q4, "C", "Tomorrow", false);
            addChoice(q4, "D", "Next month", false);

            Question q5 = new Question();
            q5.setQuestionText("What is the reason for closure?");
            q5.setExplanation("Reading comprehension.");
            part7.addQuestion(q5);
            addChoice(q5, "A", "Holiday", false);
            addChoice(q5, "B", "Maintenance", true);
            addChoice(q5, "C", "Meeting", false);
            addChoice(q5, "D", "Inspection", false);

            passageRepository.save(part5);
            passageRepository.save(part6);
            passageRepository.save(part7);
        };
    }

    private static void addChoice(Question question, String label, String text, boolean correct) {
        Choice choice = new Choice();
        choice.setLabel(label);
        choice.setChoiceText(text);
        choice.setCorrect(correct);
        question.addChoice(choice);
    }
}
