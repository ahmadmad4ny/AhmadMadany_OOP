package com.ahmadmadany.backend.repository;

import com.ahmadmadany.backend.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScoreRepository extends JpaRepository<Score, UUID> {

    List<Score> findPointGreaterThan(Integer minValue);

    List<Score> findAllByOrderByCreatedAtDesc();

    @Query("SELECT s FROM Score s ORDER BY s.point DESC")
    List<Score> findTopScores(Integer limit);
}