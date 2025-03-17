package org.example.model;

import java.util.Arrays;
import java.util.List;

public record Question(
        int id,
        String description,
        List<String> choices,
        String answer,
        String imageUrl
) {
    public String toLine(int newId) {
        return String.format("%d,%s,%s,%s,%s",
                newId, description, String.join("|", choices), answer, imageUrl);
    }

    public static Question fromLine(String line) {
        String[] tokens = line.split(",", 5);
        return new Question(
                Integer.parseInt(tokens[0]),
                tokens[1],
                Arrays.asList(tokens[2].split("\\|")),
                tokens[3],
                tokens.length > 4 ? tokens[4] : "default-image.jpg"
        );
    }
}
