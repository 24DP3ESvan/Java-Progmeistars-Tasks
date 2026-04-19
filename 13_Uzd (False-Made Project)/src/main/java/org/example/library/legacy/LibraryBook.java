package org.example.library.legacy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LibraryBook {
    private String title;

    public String borrowHome() {
        return "Borrowed home: " + title;
    }
}
