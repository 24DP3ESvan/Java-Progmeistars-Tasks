package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String product;

    @Column(nullable = false)
    private double price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}