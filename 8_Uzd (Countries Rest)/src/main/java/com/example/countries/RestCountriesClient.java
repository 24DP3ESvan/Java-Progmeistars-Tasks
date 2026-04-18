package com.example.countries;

import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

public class RestCountriesClient {

    private static final String BASE_URL = "https://restcountries.com/v4/name/";
    private static final String ALL_URL = "https://restcountries.com/v4/all";

    private static final String FIELDS_BY_NAME =
            "name,capital,region,population,flags";

    private static final String FIELDS_ALL =
            "name,region,population,area,startOfWeek";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public RestCountriesClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Country[] getCountriesByName(String countryName)
            throws IOException, InterruptedException {

        String encodedName = URLEncoder.encode(countryName, StandardCharsets.UTF_8);
        String url = BASE_URL + encodedName + "?fields=" + FIELDS_BY_NAME;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 404) {
            return new Country[0];
        }

        if (response.statusCode() != 200) {
            throw new IOException("HTTP error: " + response.statusCode());
        }

        return objectMapper.readValue(response.body(), Country[].class);
    }

    public Country[] getAllCountries()
            throws IOException, InterruptedException {

        String url = ALL_URL + "?fields=" + FIELDS_ALL;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("HTTP error: " + response.statusCode());
        }

        return objectMapper.readValue(response.body(), Country[].class);
    }

    public String toJson(Country country) throws IOException {
        return objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(country);
    }
}