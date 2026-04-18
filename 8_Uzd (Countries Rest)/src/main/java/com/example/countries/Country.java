package com.example.countries;

import lombok.*;
import java.util.*;

@Data
@NoArgsConstructor
public class Country {

    private CountryName name;
    private List<String> capital;
    private String region;
    private long population;
    private double area;
    private Map<String, String> flags;
    private String startOfWeek;

    public String getCommonName() {
        return name != null ? name.getCommon() : null;
    }

    public String getOfficialName() {
        return name != null ? name.getOfficial() : null;
    }

    public String getCapitalAsText() {
        if (capital == null || capital.isEmpty()) {
            return "No capital data";
        }
        return String.join(", ", capital);
    }

    public String getFlagPng() {
        if (flags == null) {
            return null;
        }
        return flags.get("png");
    }
}