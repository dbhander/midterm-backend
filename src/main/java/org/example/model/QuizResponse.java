package org.example.model;

import org.example.model.Question;
import org.example.model.Quiz;
import java.util.List;

public record QuizResponse(
        Quiz quiz,
        List<Question> questions
) {}
