package com.example.toeicapp.repository;

import com.example.toeicapp.model.Passage;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PassageRepository extends JpaRepository<Passage, Long> {
    List<Passage> findByPartType(String partType, Sort sort);

    @Query("SELECT p FROM Passage p LEFT JOIN FETCH p.questions WHERE p.title = :title")
    Optional<Passage> findByTitleWithQuestions(@Param("title") String title);
}
