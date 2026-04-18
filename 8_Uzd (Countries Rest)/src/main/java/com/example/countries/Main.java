package com.example.countries;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        RestCountriesClient client = new RestCountriesClient();
        CountryService service = new CountryService(client);

        System.out.print("Enter country name: ");
        String input = scanner.nextLine().trim();

        try {
            Optional<Country> countryOpt = service.findCountryByName(input);

            if (countryOpt.isEmpty()) {
                System.out.println("Country not found");
            } else {
                Country country = countryOpt.get();

                System.out.println("\n=== Country info ===");
                System.out.println("Common name: " + country.getCommonName());
                System.out.println("Official name: " + country.getOfficialName());
                System.out.println("Capital: " + country.getCapitalAsText());
                System.out.println("Region: " + country.getRegion());
                System.out.println("Population: " + country.getPopulation());
                System.out.println("Flag URL: " + country.getFlagPng());
            }

            System.out.println("\n=== Countries where week starts on Sunday ===");

            List<Country> sundayCountries =
                    service.getCountriesStartingWeekOnSunday();

            sundayCountries.forEach(c ->
                    System.out.println(c.getCommonName())
            );

            System.out.println("\n=== Top 5 density countries in Europe ===");

            List<Country> top = service.top5DensityInEurope();

            top.forEach(c -> {
                double density = c.getPopulation() / c.getArea();
                System.out.println(c.getCommonName() + " -> " + density);
            });

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}