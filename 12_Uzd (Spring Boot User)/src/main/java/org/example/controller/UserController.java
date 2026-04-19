package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users")
    public java.util.List<UserDto> getAllUsers() {
        return userService.listAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public ResponseEntity<UserDto> getUserById(@PathVariable long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/active-adults")
    @Operation(summary = "Get active adult users")
    public java.util.List<UserDto> getActiveAdults() {
        return userService.findActiveOver18();
    }

    @GetMapping("/stats/status-count")
    @Operation(summary = "Get user counts grouped by status")
    public Map<String, Long> getStatusCount() {
        return userService.countByStatusAsStrings();
    }

    @GetMapping("/stats/average-age")
    @Operation(summary = "Get average user age")
    public Map<String, BigDecimal> getAverageAge() {
        return Map.of("averageAge", userService.averageAge());
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto savedUser = userService.add(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}
