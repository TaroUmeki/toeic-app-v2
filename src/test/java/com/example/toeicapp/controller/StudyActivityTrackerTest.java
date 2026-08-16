package com.example.toeicapp.controller;

import com.example.toeicapp.model.StudyActivity;
import com.example.toeicapp.model.User;
import com.example.toeicapp.repository.StudyActivityRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StudyActivityTrackerTest {

    private final User user = new User("Taro", "hash");
    private final StudyActivityRepository repository = mock(StudyActivityRepository.class);

    @Test
    void recordToday_savesWhenNoActivityYetToday() {
        when(repository.findByUserAndActivityDate(user, LocalDate.now())).thenReturn(Optional.empty());

        StudyActivityTracker.recordToday(user, repository);

        verify(repository).save(any(StudyActivity.class));
    }

    @Test
    void recordToday_doesNotDuplicateWhenAlreadyRecordedToday() {
        LocalDate today = LocalDate.now();
        when(repository.findByUserAndActivityDate(user, today))
                .thenReturn(Optional.of(new StudyActivity(user, today)));

        StudyActivityTracker.recordToday(user, repository);

        verify(repository, never()).save(any());
    }

    @Test
    void currentStreak_isZeroWithNoActivity() {
        when(repository.findByUser(user)).thenReturn(List.of());

        assertThat(StudyActivityTracker.currentStreak(user, repository)).isEqualTo(0);
    }

    @Test
    void currentStreak_isOneWhenOnlyTodayRecorded() {
        LocalDate today = LocalDate.now();
        when(repository.findByUser(user)).thenReturn(List.of(new StudyActivity(user, today)));

        assertThat(StudyActivityTracker.currentStreak(user, repository)).isEqualTo(1);
    }

    @Test
    void currentStreak_countsConsecutiveDaysEndingToday() {
        LocalDate today = LocalDate.now();
        List<StudyActivity> activities = List.of(
                new StudyActivity(user, today),
                new StudyActivity(user, today.minusDays(1)),
                new StudyActivity(user, today.minusDays(2)));
        when(repository.findByUser(user)).thenReturn(activities);

        assertThat(StudyActivityTracker.currentStreak(user, repository)).isEqualTo(3);
    }

    @Test
    void currentStreak_fallsBackToYesterdayWhenTodayNotRecordedYet() {
        LocalDate today = LocalDate.now();
        List<StudyActivity> activities = List.of(
                new StudyActivity(user, today.minusDays(1)),
                new StudyActivity(user, today.minusDays(2)));
        when(repository.findByUser(user)).thenReturn(activities);

        assertThat(StudyActivityTracker.currentStreak(user, repository)).isEqualTo(2);
    }

    @Test
    void currentStreak_stopsAtGapBeforeToday() {
        LocalDate today = LocalDate.now();
        List<StudyActivity> activities = List.of(
                new StudyActivity(user, today),
                new StudyActivity(user, today.minusDays(3)));
        when(repository.findByUser(user)).thenReturn(activities);

        assertThat(StudyActivityTracker.currentStreak(user, repository)).isEqualTo(1);
    }
}
