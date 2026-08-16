package com.example.toeicapp.repository;

import com.example.toeicapp.model.MissedQuestion;
import com.example.toeicapp.model.Question;
import com.example.toeicapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MissedQuestionRepository extends JpaRepository<MissedQuestion, Long> {
    List<MissedQuestion> findByUser(User user);
    Optional<MissedQuestion> findByUserAndQuestion(User user, Question question);
    long countByUser(User user);
    void deleteByQuestion(Question question);
}
