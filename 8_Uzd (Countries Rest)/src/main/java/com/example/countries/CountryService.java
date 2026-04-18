package com.example.countries;

import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
public class CountryService {

    private final RestCountriesClient client;

    public Optional<Country> findCountryByName(String name)
            throws IOException, InterruptedException {

        Country[] countries = client.getCountriesByName(name);

        if (countries.length == 0) {
            return Optional.empty();
        }

        return Optional.of(countries[0]);
    }

    public String toJson(Country country) throws IOException {
        return client.toJson(country);
    }
    
    public List<Country> getCountriesStartingWeekOnSunday()
            throws IOException, InterruptedException {

        Country[] countries = client.getAllCountries();

        return Arrays.stream(countries)
                .filter(c -> "sunday".equalsIgnoreCase(c.getStartOfWeek()))
                .toList();
    }

    public List<Country> top5DensityInEurope()
            throws IOException, InterruptedException {

        Country[] countries = client.getAllCountries();

        return Arrays.stream(countries)
                .filter(c -> "Europe".equalsIgnoreCase(c.getRegion()))
                .filter(c -> c.getArea() > 0)
                .sorted((c1, c2) -> Double.compare(
                        c2.getPopulation() / c2.getArea(),
                        c1.getPopulation() / c1.getArea()
                ))
                .limit(5)
                .toList();
    }
}