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

import java.util.Set;
import java.util.stream.Collectors;

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
            Set<String> existingTitles = passageRepository.findAll().stream()
                    .map(Passage::getTitle)
                    .collect(Collectors.toSet());

            if (!existingTitles.contains("Part5 sample")) {
                passageRepository.save(buildPart5());
            }
            if (!existingTitles.contains("Part6 sample")) {
                passageRepository.save(buildPart6());
            }
            if (!existingTitles.contains("Part7 sample")) {
                passageRepository.save(buildPart7());
            }
            if (!existingTitles.contains("Part3 sample")) {
                passageRepository.save(buildPart3());
            }
            if (!existingTitles.contains("Part4 sample")) {
                passageRepository.save(buildPart4());
            }
        };
    }

    private static Passage buildPart5() {
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

        return part5;
    }

    private static Passage buildPart6() {
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

        return part6;
    }

    private static Passage buildPart7() {
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

        return part7;
    }

    private static Passage buildPart3() {
        Passage part3 = new Passage();
        part3.setTitle("Part3 sample");
        part3.setPartType("PART3");
        part3.setSkillType("LISTENING");
        part3.setAudioUrl("/audio/part3-sample.mp3");
        part3.setAudioScript("Man: Hi Sarah, do you have a few minutes? I wanted to check the schedule for tomorrow's meeting.\n\n"
                + "Woman: Sure. The meeting was originally set for 10 AM, but we had to move it to 2 PM because the conference room wasn't available in the morning.\n\n"
                + "Man: Oh, that works better for me actually. Should I still prepare the sales report, or has that changed too?\n\n"
                + "Woman: No, the report is still needed. Just make sure you send it to me by 1 PM so I can print copies before everyone arrives.");

        Question q6 = new Question();
        q6.setQuestionText("What are the speakers mainly discussing?");
        q6.setExplanation("The conversation is centered on rescheduling tomorrow's meeting.");
        part3.addQuestion(q6);
        addChoice(q6, "A", "A job interview", false);
        addChoice(q6, "B", "A meeting schedule", true);
        addChoice(q6, "C", "A sales report deadline", false);
        addChoice(q6, "D", "A conference room booking", false);

        Question q7 = new Question();
        q7.setQuestionText("Why was the meeting time changed?");
        q7.setExplanation("The woman says the conference room wasn't available in the morning.");
        part3.addQuestion(q7);
        addChoice(q7, "A", "The manager was unavailable", false);
        addChoice(q7, "B", "The room was already reserved", true);
        addChoice(q7, "C", "The woman was busy in the morning", false);
        addChoice(q7, "D", "The man requested a later time", false);

        Question q8 = new Question();
        q8.setQuestionText("What does the woman ask the man to do?");
        q8.setExplanation("She asks him to send the report by 1 PM so she can print copies.");
        part3.addQuestion(q8);
        addChoice(q8, "A", "Book a conference room", false);
        addChoice(q8, "B", "Attend the meeting at 10 AM", false);
        addChoice(q8, "C", "Send the report by 1 PM", true);
        addChoice(q8, "D", "Cancel the meeting", false);

        return part3;
    }

    private static Passage buildPart4() {
        Passage part4 = new Passage();
        part4.setTitle("Part4 sample");
        part4.setPartType("PART4");
        part4.setSkillType("LISTENING");
        part4.setAudioUrl("/audio/part4-sample.mp3");
        part4.setAudioScript("Attention all staff. This is a reminder that our office network will be undergoing "
                + "scheduled maintenance this Friday evening starting at 6 PM. During this time, email and internal "
                + "file access will be temporarily unavailable. We expect the maintenance to be completed by 9 PM. "
                + "If you need to access any files after hours, please make sure to download them before you leave "
                + "the office today. We apologize for any inconvenience this may cause. Thank you for your cooperation.");

        Question q9 = new Question();
        q9.setQuestionText("What is the purpose of the announcement?");
        q9.setExplanation("The speaker is informing staff about scheduled network maintenance.");
        part4.addQuestion(q9);
        addChoice(q9, "A", "To announce a company holiday", false);
        addChoice(q9, "B", "To inform staff about network maintenance", true);
        addChoice(q9, "C", "To request feedback from employees", false);
        addChoice(q9, "D", "To reschedule a meeting", false);

        Question q10 = new Question();
        q10.setQuestionText("When will the maintenance begin?");
        q10.setExplanation("The speaker says maintenance starts Friday evening at 6 PM.");
        part4.addQuestion(q10);
        addChoice(q10, "A", "Monday morning", false);
        addChoice(q10, "B", "Friday at 6 PM", true);
        addChoice(q10, "C", "Friday at 9 PM", false);
        addChoice(q10, "D", "This weekend", false);

        Question q11 = new Question();
        q11.setQuestionText("What should employees do if they need files after hours?");
        q11.setExplanation("The speaker asks staff to download files before leaving the office.");
        part4.addQuestion(q11);
        addChoice(q11, "A", "Contact IT support", false);
        addChoice(q11, "B", "Come into the office", false);
        addChoice(q11, "C", "Download them before leaving", true);
        addChoice(q11, "D", "Wait until Monday", false);

        return part4;
    }

    private static void addChoice(Question question, String label, String text, boolean correct) {
        Choice choice = new Choice();
        choice.setLabel(label);
        choice.setChoiceText(text);
        choice.setCorrect(correct);
        question.addChoice(choice);
    }
}
