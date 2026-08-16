package com.example.toeicapp.repository;

import com.example.toeicapp.model.Passage;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassageRepository extends JpaRepository<Passage, Long> {
    List<Passage> findByPartType(String partType, Sort sort);
}
