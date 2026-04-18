package org.example.dto;

import lombok.Getter;

@Getter
public enum Status {
    ACTIVE(1),
    BLOCKED(0);

    private final int value;

    Status(int value) {
        this.value = value;
    }

    public static Status fromValue(int value) {
        for (Status status : Status.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
