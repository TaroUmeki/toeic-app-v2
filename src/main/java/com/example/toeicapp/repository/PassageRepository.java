package com.example.toeicapp.repository;

import com.example.toeicapp.model.Passage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassageRepository extends JpaRepository<Passage, Long> {
}
