package org.example.library.service.policy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BorrowPolicyFactory {

    private final List<BorrowPolicy> policies;

    public BorrowPolicy getPolicy(String shelfType) {
        return policies.stream()
                .filter(policy -> policy.supports(shelfType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown shelf type: " + shelfType));
    }
}
