package com.example.library.service;

import com.example.library.dto.*;
import com.fasterxml.jackson.dataformat.csv.*;
import com.fasterxml.jackson.databind.MappingIterator;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public final class LibraryCsvStorage {

    private static final CsvMapper MAPPER = new CsvMapper();

    private static final CsvSchema SCHEMA = CsvSchema.builder()
            .setUseHeader(true)
            .setColumnSeparator(';')
            .addColumn("type")
            .addColumn("title")
            .addColumn("author")
            .addColumn("tags")
            .addColumn("maxDays")
            .build();

    private LibraryCsvStorage() {}

    public static void save(File file, Iterable<Book> books) throws IOException {
        try (Writer w = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8)) {
            var writer = MAPPER.writerFor(BookRow.class).with(SCHEMA);
            var seq = writer.writeValues(w);

            for (Book b : books) {
                seq.write(toRow(b));
            }
            seq.close();
        }
    }

    public static void load(File file, java.util.function.Consumer<Book> bookConsumer) throws IOException {
        try (Reader r = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
            MappingIterator<BookRow> it = MAPPER
                    .readerFor(BookRow.class)
                    .with(SCHEMA)
                    .readValues(r);

            while (it.hasNext()) {
                bookConsumer.accept(fromRow(it.next()));
            }
        }
    }

    private static BookRow toRow(Book b) {
        BookRow row = new BookRow();
        row.type = b.getType().name();
        row.title = b.getTitle();
        row.author = b.getAuthor();
        row.tags = b.tagsAsString();
        row.maxDays = (b instanceof HomeLoanBook hl) ? hl.getMaxDays() : null;
        return row;
    }

    private static Book fromRow(BookRow row) {
        BookType type;
        try {
            type = BookType.valueOf(row.type);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid book type: " + row.type);
        }

        Book b = switch (type) {
            case READING_ROOM -> new ReadingRoomBook(row.title, row.author);
            case HOME_LOAN -> new HomeLoanBook(row.title, row.author, row.maxDays == null ? 14 : row.maxDays);
        };

        b.addTags(Book.parseTags(row.tags));
        return b;
    }
}