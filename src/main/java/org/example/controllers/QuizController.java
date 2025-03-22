package org.example.controllers;

import org.example.model.Question;
import org.example.model.Quiz;
import org.example.model.QuizResponse;
import org.example.repository.QuizRepository;
import org.example.repository.QuestionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "https://dbhander.github.io/midterm-frontend/")
@RestController
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    public QuizController(QuizRepository quizRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    @PostMapping
    public ResponseEntity<Integer> createQuiz(@RequestBody Quiz quiz) {
        try {
            int newId = quizRepository.add(quiz);
            return ResponseEntity.ok(newId);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizResponse> getQuiz(@PathVariable int id) {
        try {
            Quiz quiz = quizRepository.findById(id);
            if (quiz == null) return ResponseEntity.notFound().build();

            List<Question> orderedQuestions = new ArrayList<>();
            for (Integer qId : quiz.questionIds()) {
                Question q = questionRepository.findById(qId);
                if (q != null) {
                    orderedQuestions.add(q);
                }
            }

            return ResponseEntity.ok(new QuizResponse(
                    new Quiz(quiz.id(), quiz.title(), quiz.questionIds()),
                    orderedQuestions
            ));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        try {
            return ResponseEntity.ok(quizRepository.findAll());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateQuiz(
            @PathVariable int id,
            @RequestBody Quiz updateRequest
    ) {
        try {
            Quiz existing = quizRepository.findById(id);
            if (existing == null) return ResponseEntity.notFound().build();

            String newTitle = updateRequest.title() != null ?
                    updateRequest.title() : existing.title();
            List<Integer> newQuestionIds = updateRequest.questionIds() != null ?
                    updateRequest.questionIds() : existing.questionIds();

            Quiz updated = new Quiz(
                    existing.id(),
                    newTitle,
                    newQuestionIds
            );

            quizRepository.update(updated);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
