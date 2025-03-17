package org.example.repository;

import org.example.model.Quiz;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuizRepository {
    private static final String QUIZ_FOLDER = "quizzes";
    private static final String DB_FILE = QUIZ_FOLDER + "/quizzes.txt";
    private static final String NEW_LINE = System.lineSeparator();

    public QuizRepository() {
        new File(QUIZ_FOLDER).mkdirs();
    }

    private void appendToFile(Path path, String content) throws IOException {
        Files.write(path, content.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public int add(Quiz quiz) throws IOException {
        List<Quiz> all = findAll();
        int newId = all.stream().mapToInt(Quiz::id).max().orElse(0) + 1;

        Quiz newQuiz = new Quiz(
                newId,
                quiz.title(),
                quiz.questionIds()
        );
        Path path = Paths.get(DB_FILE);
        appendToFile(path, newQuiz.toLine() + NEW_LINE);
        return newId;
    }

    public List<Quiz> findAll() throws IOException {
        Path path = Paths.get(DB_FILE);
        if (!Files.exists(path)) return new ArrayList<>();

        return Files.readAllLines(path).stream()
                .filter(line -> !line.trim().isEmpty())
                .filter(line -> line.split(",", -1).length >= 3)
                .map(Quiz::fromLine)
                .filter(quiz -> quiz != null)
                .toList();
    }

    public Quiz findById(int id) throws IOException {
        return findAll().stream()
                .filter(q -> q.id() == id)
                .findFirst()
                .orElse(null);
    }

    public void update(Quiz quiz) throws IOException {
        List<Quiz> all = findAll();
        List<Quiz> updated = all.stream()
                .map(q -> q.id() == quiz.id() ? quiz : q)
                .toList();

        String content = updated.stream()
                .map(Quiz::toLine)
                .collect(Collectors.joining(NEW_LINE));

        Files.write(Paths.get(DB_FILE), content.getBytes(StandardCharsets.UTF_8));
    }

}
