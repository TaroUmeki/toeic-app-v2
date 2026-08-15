package com.example.toeicapp.controller;

import com.example.toeicapp.model.StudyActivity;
import com.example.toeicapp.model.User;
import com.example.toeicapp.repository.StudyActivityRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

final class StudyActivityTracker {

    private StudyActivityTracker() {}

    static void recordToday(User user, StudyActivityRepository studyActivityRepository) {
        LocalDate today = LocalDate.now();
        if (studyActivityRepository.findByUserAndActivityDate(user, today).isEmpty()) {
            studyActivityRepository.save(new StudyActivity(user, today));
        }
    }

    static int currentStreak(User user, StudyActivityRepository studyActivityRepository) {
        List<StudyActivity> activities = studyActivityRepository.findByUser(user);
        Set<LocalDate> activeDates = activities.stream()
                .map(StudyActivity::getActivityDate)
                .collect(Collectors.toSet());

        LocalDate cursor = LocalDate.now();
        if (!activeDates.contains(cursor)) {
            cursor = cursor.minusDays(1);
            if (!activeDates.contains(cursor)) {
                return 0;
            }
        }

        int streak = 0;
        while (activeDates.contains(cursor)) {
            streak++;
            cursor = cursor.minusDays(1);
        }
        return streak;
    }
}
