package com.example.orbitaldemo.facade;

import com.example.orbitaldemo.model.domain.database.OrderEntity;
import com.example.orbitaldemo.model.domain.database.TeamEntity;
import com.example.orbitaldemo.model.dto.OrderDTO;
import com.example.orbitaldemo.model.dto.PagedDTO;
import com.example.orbitaldemo.service.OrderService;
import com.example.orbitaldemo.service.TeamService;
import com.example.orbitaldemo.util.AuthUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderFacadeTest {

    @Mock
    private OrderService orderService;

    @Mock
    private TeamService teamService;

    @InjectMocks
    private OrderFacade orderFacade;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaim("user_id")).thenReturn(1L); // Mock user ID
    }

    @Test
    void getAllUserOrders_ShouldReturnPagedOrders() {
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        TeamEntity team = new TeamEntity();
        team.setId(1L);
        Page<OrderEntity> pagedOrders = new PageImpl<>(Collections.emptyList());
        when(teamService.getTeamByUserId(AuthUtils.getUserId())).thenReturn(team);
        when(orderService.getAllOrdersByTeamId(team.getId(), pageRequest)).thenReturn(pagedOrders);

        PagedDTO<OrderDTO> result = orderFacade.getAllUserOrders(page, size);

        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        verify(teamService).getTeamByUserId(AuthUtils.getUserId());
        verify(orderService).getAllOrdersByTeamId(team.getId(), pageRequest);
    }

}