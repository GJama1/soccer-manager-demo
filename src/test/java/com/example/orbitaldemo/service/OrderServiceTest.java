package com.example.orbitaldemo.service;

import com.example.orbitaldemo.model.domain.database.OrderEntity;
import com.example.orbitaldemo.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllOrdersByTeamId_ShouldReturnPagedOrders() {
        Long teamId = 1L;
        Pageable pageable = Pageable.unpaged();
        Page<OrderEntity> page = new PageImpl<>(Collections.emptyList());
        when(orderRepository.findAllByBuyerTeamId(teamId, pageable)).thenReturn(page);

        Page<OrderEntity> result = orderService.getAllOrdersByTeamId(teamId, pageable);

        assertNotNull(result);
        assertEquals(page, result);
        verify(orderRepository).findAllByBuyerTeamId(teamId, pageable);
    }

    @Test
    void saveOrder_ShouldSaveAndReturnOrder() {
        OrderEntity order = new OrderEntity();
        when(orderRepository.save(order)).thenReturn(order);

        OrderEntity result = orderService.saveOrder(order);

        assertNotNull(result);
        assertEquals(order, result);
        verify(orderRepository).save(order);
    }

}