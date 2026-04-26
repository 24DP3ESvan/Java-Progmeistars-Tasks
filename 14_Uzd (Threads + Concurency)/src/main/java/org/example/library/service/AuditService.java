package org.example.library.service;

import org.springframework.stereotype.Service;

@Service
public class AuditService {

    public void write(String message) {
        System.out.println("AUDIT: " + message);
    }
}
