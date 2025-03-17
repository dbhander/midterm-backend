package org.example.controllers;

import org.example.model.Question;
import org.example.repository.QuestionRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/questions")
public class QuizQuestionController {

    private final QuestionRepository questionRepository;

    public QuizQuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @PostMapping
    public ResponseEntity<Integer> addQuestion(@RequestBody Question question) {
        try {
            if (question.imageUrl() == null || question.imageUrl().isEmpty()) {
                question = new Question(
                        question.id(),
                        question.description(),
                        question.choices(),
                        question.answer(),
                        "default-image.jpg"
                );
            }
            int newId = questionRepository.add(question);
            return ResponseEntity.ok(newId);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return ResponseEntity.ok(questionRepository.findAll());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable int id) {
        try {
            Question question = questionRepository.findById(id);
            return question != null ?
                    ResponseEntity.ok(question) :
                    ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Void> uploadImage(@PathVariable int id,
                                            @RequestParam("file") MultipartFile file) {
        try {
            if (questionRepository.findById(id) == null) {
                return ResponseEntity.notFound().build();
            }
            questionRepository.updateImage(id, file);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable int id) {
        try {
            byte[] imageBytes = questionRepository.getImage(id);
            if (imageBytes == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + id + ".png\"")
                    .body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Question>> searchQuestions(
            @RequestParam("name") String searchTerm) {
        try {
            List<Question> results = questionRepository.findAll().stream()
                    .filter(q -> q.description().toLowerCase().contains(searchTerm.toLowerCase()))
                    .toList();
            return ResponseEntity.ok(results);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
