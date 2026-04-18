package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.OrderDto;
import org.example.entity.OrderEntity;
import org.example.entity.UserEntity;
import org.example.mapper.OrderMapper;
import org.example.repository.JpaOrderRepository;
import org.example.repository.JpaUserRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class OrderService {

    private final JpaOrderRepository orderRepository;
    private final JpaUserRepository userRepository;

    public void addOrder(OrderDto dto) {
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        OrderEntity order = OrderMapper.toEntity(dto, user);

        user.getOrders().add(order);
        orderRepository.save(order);
        if (user.getOrders() == null) {
            user.setOrders(new ArrayList<>());
        }
        user.getOrders().add(order);
    }

    public List<OrderDto> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(OrderMapper::toDto)
                .toList();
    }
}