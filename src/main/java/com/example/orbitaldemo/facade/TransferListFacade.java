package com.example.orbitaldemo.facade;

import com.example.orbitaldemo.model.domain.database.*;
import com.example.orbitaldemo.model.dto.PagedDTO;
import com.example.orbitaldemo.model.dto.TransferListCreateRequestDTO;
import com.example.orbitaldemo.model.dto.TransferListFilterRequestDTO;
import com.example.orbitaldemo.model.dto.TransferListItemDTO;
import com.example.orbitaldemo.model.enums.ListingStatus;
import com.example.orbitaldemo.model.enums.OrderStatus;
import com.example.orbitaldemo.model.mapper.TransferListMapper;
import com.example.orbitaldemo.service.OrderService;
import com.example.orbitaldemo.service.PlayerService;
import com.example.orbitaldemo.service.TeamService;
import com.example.orbitaldemo.service.TransferListService;
import com.example.orbitaldemo.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferListFacade {

    private final TransferListService transferListService;

    private final TeamService teamService;

    private final PlayerService playerService;

    private final OrderService orderService;

    public PagedDTO<TransferListItemDTO> getOpenListings(TransferListFilterRequestDTO request, int page, int size) {
        return PagedDTO.of(
                transferListService.getAllListings(
                                request,
                                List.of(ListingStatus.OPEN),
                                PageRequest.of(page, size))
                        .map(TransferListMapper::toListingDTO)
        );
    }

    @Transactional
    public TransferListItemDTO createListing(TransferListCreateRequestDTO request) {

        Long userId = AuthUtils.getUserId();
        TeamEntity team = teamService.getTeamByUserId(userId);
        PlayerEntity player = playerService.getPlayerByIdAndTeamId(request.getPlayerId(), team.getId());

        if (transferListService.listingExists(player.getId(), team.getId(), ListingStatus.OPEN)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Player is already listed for transfer");
        }

        TransferListEntity listing = new TransferListEntity();
        listing.setPlayer(player);
        listing.setSellerTeam(team);
        listing.setPrice(request.getPrice());
        listing.setStatus(ListingStatus.OPEN);

        return TransferListMapper.toListingDTO(transferListService.saveListing(listing));
    }

    @Transactional
    public void purchaseListing(Long id) {

        Long userId = AuthUtils.getUserId();

        TransferListEntity listing = transferListService.getListingByIdAndStatusWithLock(id, ListingStatus.OPEN);
        Long sellerTeamId = listing.getSellerTeam().getId();
        Long buyerTeamId = teamService.getTeamIdByUserId(userId);

        if (sellerTeamId.equals(buyerTeamId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot buy from your own team");
        }

        //Lock teams in consistent order
        TeamEntity firstTeam;
        TeamEntity secondTeam;
        if (buyerTeamId < sellerTeamId) {
            firstTeam = teamService.getTeamByIdWithLock(buyerTeamId);
            secondTeam = teamService.getTeamByIdWithLock(sellerTeamId);
        } else {
            firstTeam = teamService.getTeamByIdWithLock(sellerTeamId);
            secondTeam = teamService.getTeamByIdWithLock(buyerTeamId);
        }
        TeamEntity buyerTeam = buyerTeamId.equals(firstTeam.getId()) ? firstTeam : secondTeam;
        TeamEntity sellerTeam = sellerTeamId.equals(firstTeam.getId()) ? firstTeam : secondTeam;

        BigDecimal price = listing.getPrice();
        PlayerEntity player = listing.getPlayer();

        //Check and update team budgets
        if (buyerTeam.getBudget().compareTo(price) < 0) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Insufficient funds");
        }

        buyerTeam.setBudget(buyerTeam.getBudget().subtract(price));
        sellerTeam.setBudget(sellerTeam.getBudget().add(price));

        player.setMarketValue(price);
        playerService.savePlayer(player);

        TeamPlayerEntity oldRelation = teamService.findRelationByPlayerIdAndTeamId(player.getId(), sellerTeamId);
        TeamPlayerEntity newRelation = new TeamPlayerEntity();
        newRelation.setPlayer(player);
        newRelation.setTeam(buyerTeam);

        teamService.deleteRelation(oldRelation);
        teamService.saveRelation(newRelation);

        listing.setStatus(ListingStatus.CLOSED);

        OrderEntity order = new OrderEntity();
        order.setBuyerTeam(buyerTeam);
        order.setListing(listing);
        order.setPrice(price);
        order.setStatus(OrderStatus.COMPLETED);
        orderService.saveOrder(order);

        teamService.calculateTeamValue(buyerTeam);
        teamService.calculateTeamValue(sellerTeam);

    }

}
