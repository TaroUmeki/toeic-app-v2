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
            if (!existingTitles.contains("Part7 sample (advertisement & e-mail)")) {
                passageRepository.save(buildPart7Double());
            }
            if (!existingTitles.contains("Part3 sample")) {
                passageRepository.save(buildPart3());
            }
            if (!existingTitles.contains("Part4 sample")) {
                passageRepository.save(buildPart4());
            }
            if (!existingTitles.contains("Part1 sample (office)")) {
                passageRepository.save(buildPart1Office());
            }
            if (!existingTitles.contains("Part1 sample (park)")) {
                passageRepository.save(buildPart1Park());
            }
            if (!existingTitles.contains("Part2 sample (training)")) {
                passageRepository.save(buildPart2Training());
            }
            if (!existingTitles.contains("Part2 sample (invoice)")) {
                passageRepository.save(buildPart2Invoice());
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

    private static Passage buildPart7Double() {
        Passage part7 = new Passage();
        part7.setTitle("Part7 sample (advertisement & e-mail)");
        part7.setPartType("PART7");
        part7.setSkillType("READING");
        part7.setBody(
                "Questions 176-180 refer to the following advertisement and e-mail.\n\n"
                + "[Advertisement]\n"
                + "QuickPrint Solutions\n"
                + "Professional Printing for Every Occasion\n\n"
                + "Need business cards, brochures, or banners printed quickly and affordably? QuickPrint Solutions has you covered! "
                + "Visit quickprintsolutions.com to view samples of our previous work and get an instant price quote.\n\n"
                + "Services We Offer:\n"
                + "1. Business Card Printing - Choose from dozens of templates or upload your own design.\n"
                + "2. Large-Format Banners - Perfect for trade shows, store openings, and outdoor events.\n"
                + "3. Custom Design Services - Our in-house designers can create a design from scratch based on your ideas.\n"
                + "4. Rush Order Processing - Need it fast? We offer next-day delivery for an additional fee.\n\n"
                + "To place an order, e-mail us at orders@quickprintsolutions.com with your requirements, and a representative "
                + "will respond within one business day with a detailed quote.\n\n"
                + "[E-mail]\n"
                + "To: orders@quickprintsolutions.com\n"
                + "From: r.tanaka@harborcafe.com\n"
                + "Date: March 3\n"
                + "Subject: Printing Request\n\n"
                + "I came across your website while searching for a local printing company and would like to place an order "
                + "for my new cafe. I need 500 business cards featuring our logo, which I can send as an attachment, and I was "
                + "also hoping to get a large banner made for our grand opening event on March 20. Since the event is coming "
                + "up soon, I would appreciate a quick turnaround if possible. Could you let me know the cost for both items, "
                + "including any rush fees? You can reach me at my office phone between 9:00 A.M. and 4:00 P.M.\n\n"
                + "Thank you,\n"
                + "Ryo Tanaka\n"
                + "Harbor Cafe\n"
                + "045-555-2938"
        );

        Question q176 = new Question();
        q176.setQuestionText("According to the advertisement, why should customers visit the QuickPrint Solutions Web site?");
        q176.setExplanation("The advertisement says to visit the Web site to view samples of previous work and get an instant price quote.");
        part7.addQuestion(q176);
        addChoice(q176, "A", "To view samples of previous work", true);
        addChoice(q176, "B", "To submit a payment", false);
        addChoice(q176, "C", "To download a design template", false);
        addChoice(q176, "D", "To contact a representative by phone", false);

        Question q177 = new Question();
        q177.setQuestionText("What is suggested about QuickPrint Solutions?");
        q177.setExplanation("Because the ad offers a custom design service for customers without their own design, it can be inferred that not all customers arrive with a finished design.");
        part7.addQuestion(q177);
        addChoice(q177, "A", "It only accepts orders by phone.", false);
        addChoice(q177, "B", "It offers a design service for customers without their own designs.", true);
        addChoice(q177, "C", "It specializes in outdoor advertising only.", false);
        addChoice(q177, "D", "It recently opened a new office.", false);

        Question q178 = new Question();
        q178.setQuestionText("Who most likely is Mr. Tanaka?");
        q178.setExplanation("He refers to \"my new cafe\" and \"our grand opening event,\" indicating he owns the business.");
        part7.addQuestion(q178);
        addChoice(q178, "A", "A graphic designer", false);
        addChoice(q178, "B", "A printing company employee", false);
        addChoice(q178, "C", "A business owner", true);
        addChoice(q178, "D", "A delivery driver", false);

        Question q179 = new Question();
        q179.setQuestionText("What service does Mr. Tanaka NOT request from QuickPrint Solutions?");
        q179.setExplanation("He already has a logo to send as an attachment, so he does not request custom design services.");
        part7.addQuestion(q179);
        addChoice(q179, "A", "Business card printing", false);
        addChoice(q179, "B", "Rush order processing", false);
        addChoice(q179, "C", "Custom design services", true);
        addChoice(q179, "D", "Banner printing", false);

        Question q180 = new Question();
        q180.setQuestionText("What will Mr. Tanaka most likely do next?");
        q180.setExplanation("He asks to be told the cost for both items, so he is most likely waiting for a price quote from QuickPrint Solutions.");
        part7.addQuestion(q180);
        addChoice(q180, "A", "Visit the QuickPrint Solutions office", false);
        addChoice(q180, "B", "Wait for a price quote", true);
        addChoice(q180, "C", "Attend a trade show", false);
        addChoice(q180, "D", "Design a business card", false);

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

    private static Passage buildPart1Office() {
        Passage part1 = new Passage();
        part1.setTitle("Part1 sample (office)");
        part1.setPartType("PART1");
        part1.setSkillType("LISTENING");
        part1.setImageUrl("/images/part1-office.svg");
        part1.setAudioUrl("/audio/part1-office-sample.mp3");
        part1.setAudioScript("A. The woman is watering the plants.\n"
                + "B. The man is pointing at the whiteboard.\n"
                + "C. The chairs are being stacked in the corner.\n"
                + "D. The window is being cleaned.");

        Question q12 = new Question();
        q12.setQuestionText("写真を最も適切に描写している文を選びなさい。");
        q12.setExplanation("The man in the picture is pointing at the whiteboard, which matches statement B.");
        part1.addQuestion(q12);
        addChoice(q12, "A", "The woman is watering the plants.", false);
        addChoice(q12, "B", "The man is pointing at the whiteboard.", true);
        addChoice(q12, "C", "The chairs are being stacked in the corner.", false);
        addChoice(q12, "D", "The window is being cleaned.", false);

        return part1;
    }

    private static Passage buildPart1Park() {
        Passage part1 = new Passage();
        part1.setTitle("Part1 sample (park)");
        part1.setPartType("PART1");
        part1.setSkillType("LISTENING");
        part1.setImageUrl("/images/part1-park.svg");
        part1.setAudioUrl("/audio/part1-park-sample.mp3");
        part1.setAudioScript("A. The man is riding a bicycle.\n"
                + "B. The woman is sitting on a bench reading a book.\n"
                + "C. A jogger is running along the path.\n"
                + "D. Workers are planting trees.");

        Question q13 = new Question();
        q13.setQuestionText("写真を最も適切に描写している文を選びなさい。");
        q13.setExplanation("The picture shows a person jogging along the park path, which matches statement C.");
        part1.addQuestion(q13);
        addChoice(q13, "A", "The man is riding a bicycle.", false);
        addChoice(q13, "B", "The woman is sitting on a bench reading a book.", false);
        addChoice(q13, "C", "A jogger is running along the path.", true);
        addChoice(q13, "D", "Workers are planting trees.", false);

        return part1;
    }

    private static Passage buildPart2Training() {
        Passage part2 = new Passage();
        part2.setTitle("Part2 sample (training)");
        part2.setPartType("PART2");
        part2.setSkillType("LISTENING");
        part2.setAudioUrl("/audio/part2-training-sample.mp3");
        part2.setAudioScript("When does the training session start?\n"
                + "A. It starts at 9 AM.\n"
                + "B. In the main conference room.\n"
                + "C. I already finished it.");

        Question q14 = new Question();
        q14.setQuestionText("質問に対する最も適切な応答を選びなさい。「When does the training session start?」");
        q14.setExplanation("The question asks \"when\", so the reply giving a time (9 AM) is correct.");
        part2.addQuestion(q14);
        addChoice(q14, "A", "It starts at 9 AM.", true);
        addChoice(q14, "B", "In the main conference room.", false);
        addChoice(q14, "C", "I already finished it.", false);

        return part2;
    }

    private static Passage buildPart2Invoice() {
        Passage part2 = new Passage();
        part2.setTitle("Part2 sample (invoice)");
        part2.setPartType("PART2");
        part2.setSkillType("LISTENING");
        part2.setAudioUrl("/audio/part2-invoice-sample.mp3");
        part2.setAudioScript("Could you send me the invoice by tomorrow?\n"
                + "A. Yes, at the printer.\n"
                + "B. Sure, I'll email it this afternoon.\n"
                + "C. It costs about $200.");

        Question q15 = new Question();
        q15.setQuestionText("質問に対する最も適切な応答を選びなさい。「Could you send me the invoice by tomorrow?」");
        q15.setExplanation("The request is agreed to and a concrete plan (emailing it this afternoon) is given.");
        part2.addQuestion(q15);
        addChoice(q15, "A", "Yes, at the printer.", false);
        addChoice(q15, "B", "Sure, I'll email it this afternoon.", true);
        addChoice(q15, "C", "It costs about $200.", false);

        return part2;
    }

    private static void addChoice(Question question, String label, String text, boolean correct) {
        Choice choice = new Choice();
        choice.setLabel(label);
        choice.setChoiceText(text);
        choice.setCorrect(correct);
        question.addChoice(choice);
    }
}
