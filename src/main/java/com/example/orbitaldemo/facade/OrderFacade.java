package com.example.orbitaldemo.facade;

import com.example.orbitaldemo.model.domain.database.TeamEntity;
import com.example.orbitaldemo.model.dto.OrderDTO;
import com.example.orbitaldemo.model.dto.PagedDTO;
import com.example.orbitaldemo.model.mapper.OrderMapper;
import com.example.orbitaldemo.service.OrderService;
import com.example.orbitaldemo.service.TeamService;
import com.example.orbitaldemo.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;

    private final TeamService teamService;

    public PagedDTO<OrderDTO> getAllUserOrders(int page, int size) {
        TeamEntity team = teamService.getTeamByUserId(AuthUtils.getUserId());
        return PagedDTO.of(
                orderService.getAllOrdersByTeamId(team.getId(), PageRequest.of(page, size)).map(OrderMapper::toOrderDTO)
        );
    }

}
