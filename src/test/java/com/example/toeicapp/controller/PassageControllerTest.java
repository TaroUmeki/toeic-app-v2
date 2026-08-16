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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PassageControllerTest {

    private final PassageRepository passageRepository = mock(PassageRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final MissedQuestionRepository missedQuestionRepository = mock(MissedQuestionRepository.class);
    private final StudyActivityRepository studyActivityRepository = mock(StudyActivityRepository.class);

    private final PassageController controller = new PassageController(
            passageRepository, userRepository, missedQuestionRepository, studyActivityRepository);

    private final User user = new User("Taro", "hash");
    private Question question;
    private Principal principal;

    @BeforeEach
    void setUp() {
        Passage passage = new Passage();
        passage.setId(1L);

        question = new Question();
        question.setId(10L);
        passage.addQuestion(question);

        Choice correctChoice = new Choice();
        correctChoice.setId(100L);
        correctChoice.setCorrect(true);
        question.addChoice(correctChoice);

        Choice wrongChoice = new Choice();
        wrongChoice.setId(101L);
        wrongChoice.setCorrect(false);
        question.addChoice(wrongChoice);

        principal = () -> "Taro";
        when(userRepository.findByUsername("Taro")).thenReturn(Optional.of(user));
        when(passageRepository.findById(1L)).thenReturn(Optional.of(passage));
        when(missedQuestionRepository.findByUserAndQuestion(user, question)).thenReturn(Optional.empty());
    }

    @Test
    void submit_correctAnswer_countsAsCorrectAndDoesNotSaveMissedQuestion() {
        Map<String, String> params = new HashMap<>();
        params.put("answer_10", "100");
        Model model = new ExtendedModelMap();

        controller.submit(1L, params, principal, model);

        assertThat(model.getAttribute("correctCount")).isEqualTo(1);
        verify(missedQuestionRepository, never()).save(any());
    }

    @Test
    void submit_incorrectAnswer_savesMissedQuestion() {
        Map<String, String> params = new HashMap<>();
        params.put("answer_10", "101");
        Model model = new ExtendedModelMap();

        controller.submit(1L, params, principal, model);

        assertThat(model.getAttribute("correctCount")).isEqualTo(0);
        verify(missedQuestionRepository).save(any(MissedQuestion.class));
    }

    @Test
    void submit_correctingAPreviouslyMissedQuestion_removesIt() {
        MissedQuestion existing = new MissedQuestion(user, question);
        when(missedQuestionRepository.findByUserAndQuestion(user, question)).thenReturn(Optional.of(existing));

        Map<String, String> params = new HashMap<>();
        params.put("answer_10", "100");
        Model model = new ExtendedModelMap();

        controller.submit(1L, params, principal, model);

        verify(missedQuestionRepository).delete(existing);
    }

    @Test
    void submit_stillIncorrectOnAlreadyMissedQuestion_doesNotDuplicate() {
        MissedQuestion existing = new MissedQuestion(user, question);
        when(missedQuestionRepository.findByUserAndQuestion(user, question)).thenReturn(Optional.of(existing));

        Map<String, String> params = new HashMap<>();
        params.put("answer_10", "101");
        Model model = new ExtendedModelMap();

        controller.submit(1L, params, principal, model);

        verify(missedQuestionRepository, never()).save(any());
    }
}
