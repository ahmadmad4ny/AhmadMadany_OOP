package com.ahmadmadany.backend.controller;

import com.ahmadmadany.backend.model.Score;
import com.ahmadmadany.backend.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/scores")
@CrossOrigin(origins = "*")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    // GET /api/scores/{scoreId}
    @GetMapping("/{scoreId}")
    public ResponseEntity<?> getScoreById(@PathVariable UUID scoreId) {

        Optional<Score> score = scoreService.getScoreByID(scoreId);

        if (score.isPresent()) {
            return ResponseEntity.ok(score.get());
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("{\"error\": \"Score not found\"}");
    }

    // POST /api/scores
    @PostMapping
    public ResponseEntity<?> createScore(@RequestBody Score score) {
        try {
            Score newScore = scoreService.createScore(score);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(newScore);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}