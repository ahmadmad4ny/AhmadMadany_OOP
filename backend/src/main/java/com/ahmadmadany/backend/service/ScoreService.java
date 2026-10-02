package com.ahmadmadany.backend.service;

import com.ahmadmadany.backend.model.Score;
import com.ahmadmadany.backend.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.List;



@Service
public class ScoreService {

    public List<Score> getAllScores(){
        return scoreRepository.findAll();
    }

    public List<Score> getRecentScores(){
        return scoreRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Score> getRecent(){
        return scoreRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Score> getScoreAboveValue(Integer minValue){
        return scoreRepository.findPointGreaterThan(minValue);
    }

    public List<Score> getLeaderboard(Integer limit) {
        return scoreRepository.findTopScores(limit);
    }

    public void deleteScore(UUID scoreId) {
        Score = score 
    }

    @Autowired
    private ScoreRepository scoreRepository;

    public Score createScore(Score score) {
        return scoreRepository.save(score);
    }

    public Optional<Score> getScoreByID(UUID scoreId) {
        return scoreRepository.findById(scoreId);
    }
}