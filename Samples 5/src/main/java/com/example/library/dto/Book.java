package com.example.library.dto;

import java.util.*;

public interface Book {

    long getId();

    String getTitle();

    String getAuthor();

    Set<String> getTags();

    BookType getType();

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

    default String tagsAsString() {
        return String.join("|", getTags());
    }

    static Set<String> parseTags(String raw) {

        Set<String> res = new LinkedHashSet<>();

        if (raw == null || raw.isBlank()) return res;

        for (String p : raw.split("\\|")) {

            String t = p.trim();

            if (!t.isEmpty()) res.add(t);
        }

        return res;
    }
}
