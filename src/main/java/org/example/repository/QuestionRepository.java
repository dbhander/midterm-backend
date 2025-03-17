package org.example.repository;

import org.example.model.Question;
import org.example.model.Quiz;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionRepository {
    private static final String QUESTION_FOLDER = "questions";
    private static final String IMAGES_FOLDER = QUESTION_FOLDER + "/images/";
    private static final String DB_FILE = QUESTION_FOLDER + "/db.txt";
    private static final String NEW_LINE = System.lineSeparator();

    public QuestionRepository() {
        new File(IMAGES_FOLDER).mkdirs();
    }

    private void appendToFile(Path path, String content) throws IOException {
        Files.write(path, content.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public int add(Question question) throws IOException {
        List<Question> all = findAll();
        int newId = all.stream().mapToInt(Question::id).max().orElse(0) + 1;
        Path path = Paths.get(DB_FILE);
        appendToFile(path, question.toLine(newId) + NEW_LINE);
        return newId;
    }

    public List<Question> findAll() throws IOException {
        Path path = Paths.get(DB_FILE);
        if (!Files.exists(path)) return new ArrayList<>();

        return Files.readAllLines(path).stream()
                .filter(line -> !line.trim().isEmpty())
                .map(Question::fromLine)
                .toList();
    }

    public Question findById(int id) throws IOException {
        return findAll().stream()
                .filter(q -> q.id() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean updateImage(int id, MultipartFile file) throws IOException {
        String extension = ".png";
        Path path = Paths.get(IMAGES_FOLDER + id + extension);
        file.transferTo(path);
        return true;
    }

    public byte[] getImage(int id) throws IOException {
        Path path = Paths.get(IMAGES_FOLDER + id + ".png");
        return Files.exists(path) ? Files.readAllBytes(path) : null;
    }
}
