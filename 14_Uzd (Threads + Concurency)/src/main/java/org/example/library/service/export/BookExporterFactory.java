package org.example.library.service.export;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookExporterFactory {

    private final List<BookExporter> exporters;

    public BookExporter getExporter(String format) {
        String selectedFormat = format == null || format.isBlank() ? "txt" : format;
        return exporters.stream()
                .filter(exporter -> exporter.supports(selectedFormat))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported format: " + selectedFormat));
    }
}
