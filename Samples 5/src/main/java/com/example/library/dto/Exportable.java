package com.example.library.dto;

public interface Exportable {

    String toCsvLine();

    String getCsvHeader();
}
