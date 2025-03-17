package org.example.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
public record Quiz(
        int id,
        String title,
        List<Integer> questionIds
) {
    public String toLine() {
        return String.format("%d,%s,%s",
                id, title, String.join("|",
                        questionIds.stream()
                                .map(String::valueOf)
                                .toList()));
    }

    public static Quiz fromLine(String line) {
        String[] tokens = line.split(",", 3);
        List<Integer> questionIds = new ArrayList<>();

        if(tokens.length > 2 && !tokens[2].isEmpty()) {
            questionIds = Arrays.stream(tokens[2].split("\\|"))
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .toList();
        }

        return new Quiz(
                Integer.parseInt(tokens[0]),
                tokens[1],
                questionIds
        );
    }
}