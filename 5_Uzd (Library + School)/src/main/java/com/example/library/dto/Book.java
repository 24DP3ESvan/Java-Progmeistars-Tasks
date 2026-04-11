package com.example.library.dto;

import java.util.*;
import java.util.stream.Collectors;

public interface Book {
    long getId();
    String getTitle();
    String getAuthor();
    Set<String> getTags();
    BookType getType();
    boolean canBeBorrowed();

    // ===== default methods =====
    default void addTag(String tag) {
        if (tag != null && !tag.isBlank()) {
            getTags().add(tag.trim().toLowerCase());
        }
    }

    default void addTags(Set<String> tags) {
        if (tags == null) return;
        for (String t : tags) addTag(t);
    }

    default Set<String> getTagsReadOnly() {
        return Collections.unmodifiableSet(getTags());
    }

    /** Склеиваем теги в одну строку для хранения в CSV: tag1|tag2|tag3 */
    default String tagsAsString() {
        // Stream API + method reference
        return String.join("|", getTags());
    }

    /** парсим строгу тегов tag1|tag2|tag3 в коллекцию */
    static Set<String> parseTags(String raw) {
        if (raw == null || raw.isBlank()) return new LinkedHashSet<>();

        // Stream API + lambdas
        return Arrays.stream(raw.split("\\|"))
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}