package com.example.orbitaldemo.controller;

import com.example.orbitaldemo.facade.OrderFacade;
import com.example.orbitaldemo.model.dto.OrderDTO;
import com.example.orbitaldemo.model.dto.PagedDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderFacade orderFacade;

    @GetMapping("/my-orders")
    public ResponseEntity<PagedDTO<OrderDTO>> getUserOrders(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(orderFacade.getAllUserOrders(page, size));
    }

}