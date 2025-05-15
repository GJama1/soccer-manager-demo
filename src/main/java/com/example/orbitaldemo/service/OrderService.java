package com.example.orbitaldemo.service;

import com.example.orbitaldemo.model.domain.database.OrderEntity;
import com.example.orbitaldemo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Page<OrderEntity> getAllOrdersByTeamId(Long teamId, Pageable pageRequest) {
        return orderRepository.findAllByBuyerTeamId(teamId, pageRequest);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public OrderEntity saveOrder(OrderEntity order) {
        return orderRepository.save(order);
    }

}
