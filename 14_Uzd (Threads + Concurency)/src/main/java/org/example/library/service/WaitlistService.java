package org.example.library.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WaitlistService {

    // This in-memory structure is deliberately naive for the concurrency exercise.
    private final Map<Long, List<String>> readersByBookId = new HashMap<>();

    public int addReader(Long bookId, String readerName) {
        List<String> readers = readersByBookId.computeIfAbsent(bookId, ignored -> new ArrayList<>());
        String normalizedReaderName = readerName.trim();

        if (!readers.contains(normalizedReaderName)) {
            readers.add(normalizedReaderName);
        }

        return readers.size();
    }

    public List<String> getReaders(Long bookId) {
        return readersByBookId.getOrDefault(bookId, List.of());
    }

    public String peekNextReader(Long bookId) {
        List<String> readers = readersByBookId.get(bookId);
        if (readers == null || readers.isEmpty()) {
            return null;
        }
        return readers.get(0);
    }

    public void removeReader(Long bookId, String readerName) {
        List<String> readers = readersByBookId.get(bookId);
        if (readers == null) {
            return;
        }

        readers.remove(readerName.trim());
        if (readers.isEmpty()) {
            readersByBookId.remove(bookId);
        }
    }

    public void removeNextReader(Long bookId) {
        List<String> readers = readersByBookId.get(bookId);
        if (readers == null || readers.isEmpty()) {
            return;
        }

        readers.remove(0);
        if (readers.isEmpty()) {
            readersByBookId.remove(bookId);
        }
    }
}
