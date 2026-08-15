package com.example.toeicapp.repository;

import com.example.toeicapp.model.StudyActivity;
import com.example.toeicapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyActivityRepository extends JpaRepository<StudyActivity, Long> {
    Optional<StudyActivity> findByUserAndActivityDate(User user, LocalDate activityDate);
    List<StudyActivity> findByUser(User user);
}
