package com.example.orbitaldemo.facade;

import com.example.orbitaldemo.model.domain.database.*;
import com.example.orbitaldemo.model.dto.PagedDTO;
import com.example.orbitaldemo.model.dto.TransferListCreateRequestDTO;
import com.example.orbitaldemo.model.dto.TransferListFilterRequestDTO;
import com.example.orbitaldemo.model.dto.TransferListItemDTO;
import com.example.orbitaldemo.model.enums.ListingStatus;
import com.example.orbitaldemo.service.OrderService;
import com.example.orbitaldemo.service.PlayerService;
import com.example.orbitaldemo.service.TeamService;
import com.example.orbitaldemo.service.TransferListService;
import com.example.orbitaldemo.util.AuthUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransferListFacadeTest {

    @Mock
    private TransferListService transferListService;

    @Mock
    private TeamService teamService;

    @Mock
    private PlayerService playerService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private TransferListFacade transferListFacade;

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
    void getOpenListings_ShouldReturnPagedListings() {
        TransferListFilterRequestDTO request = new TransferListFilterRequestDTO();
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<TransferListEntity> pagedListings = new PageImpl<>(Collections.emptyList());
        when(transferListService.getAllListings(request, List.of(ListingStatus.OPEN), pageRequest)).thenReturn(pagedListings);

        PagedDTO<TransferListItemDTO> result = transferListFacade.getOpenListings(request, page, size);

        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        verify(transferListService).getAllListings(request, List.of(ListingStatus.OPEN), pageRequest);
    }

    @Test
    void createListing_ShouldCreateAndReturnListing() {
        TransferListCreateRequestDTO request = new TransferListCreateRequestDTO();
        request.setPlayerId(1L);
        request.setPrice(BigDecimal.valueOf(1000));

        TeamEntity team = new TeamEntity();
        team.setId(1L);
        PlayerEntity player = new PlayerEntity();
        player.setId(1L);
        when(teamService.getTeamByUserId(AuthUtils.getUserId())).thenReturn(team);
        when(playerService.getPlayerByIdAndTeamId(request.getPlayerId(), team.getId())).thenReturn(player);
        when(transferListService.listingExists(player.getId(), team.getId(), ListingStatus.OPEN)).thenReturn(false);

        TransferListEntity listing = new TransferListEntity();
        listing.setId(1L);
        listing.setPlayer(player);
        listing.setSellerTeam(team);
        listing.setPrice(request.getPrice());
        listing.setStatus(ListingStatus.OPEN);
        when(transferListService.saveListing(any(TransferListEntity.class))).thenReturn(listing);

        TransferListItemDTO result = transferListFacade.createListing(request);

        assertNotNull(result);
        assertEquals(listing.getId(), result.getId());
        verify(teamService).getTeamByUserId(AuthUtils.getUserId());
        verify(playerService).getPlayerByIdAndTeamId(request.getPlayerId(), team.getId());
        verify(transferListService).listingExists(player.getId(), team.getId(), ListingStatus.OPEN);
        verify(transferListService).saveListing(any(TransferListEntity.class));
    }

    @Test
    void purchaseListing_ShouldCompletePurchase() {
        Long listingId = 1L;
        Long buyerTeamId = 2L;
        Long sellerTeamId = 3L;

        TransferListEntity listing = new TransferListEntity();
        listing.setId(listingId);
        listing.setPrice(BigDecimal.valueOf(1000));
        listing.setStatus(ListingStatus.OPEN);
        TeamEntity sellerTeam = new TeamEntity();
        sellerTeam.setId(sellerTeamId);
        sellerTeam.setBudget(BigDecimal.valueOf(5000));
        listing.setSellerTeam(sellerTeam);
        PlayerEntity player = new PlayerEntity();
        player.setId(1L);
        listing.setPlayer(player);

        TeamEntity buyerTeam = new TeamEntity();
        buyerTeam.setId(buyerTeamId);
        buyerTeam.setBudget(BigDecimal.valueOf(2000));

        TeamPlayerEntity oldRelation = new TeamPlayerEntity();
        oldRelation.setPlayer(player);
        oldRelation.setTeam(sellerTeam);

        when(transferListService.getListingByIdAndStatusWithLock(listingId, ListingStatus.OPEN)).thenReturn(listing);
        when(teamService.getTeamIdByUserId(AuthUtils.getUserId())).thenReturn(buyerTeamId);
        when(teamService.getTeamByIdWithLock(buyerTeamId)).thenReturn(buyerTeam);
        when(teamService.getTeamByIdWithLock(sellerTeamId)).thenReturn(sellerTeam);
        when(teamService.findRelationByPlayerIdAndTeamId(player.getId(), sellerTeamId)).thenReturn(oldRelation);

        transferListFacade.purchaseListing(listingId);

        assertEquals(BigDecimal.valueOf(6000), sellerTeam.getBudget());
        assertEquals(BigDecimal.valueOf(1000), buyerTeam.getBudget());
        assertEquals(ListingStatus.CLOSED, listing.getStatus());
        verify(playerService).savePlayer(player);
        verify(teamService).deleteRelation(oldRelation);
        verify(teamService).saveRelation(any(TeamPlayerEntity.class));
        verify(orderService).saveOrder(any(OrderEntity.class));
        verify(teamService).calculateTeamValue(buyerTeam);
        verify(teamService).calculateTeamValue(sellerTeam);
    }

    @Test
    void purchaseListing_ShouldThrowException_WhenInsufficientFunds() {
        Long listingId = 1L;
        Long buyerTeamId = 2L;

        TransferListEntity listing = new TransferListEntity();
        listing.setId(listingId);
        listing.setPrice(BigDecimal.valueOf(1000));
        listing.setStatus(ListingStatus.OPEN);
        TeamEntity sellerTeam = new TeamEntity();
        sellerTeam.setId(3L);
        listing.setSellerTeam(sellerTeam);

        TeamEntity buyerTeam = new TeamEntity();
        buyerTeam.setId(buyerTeamId);
        buyerTeam.setBudget(BigDecimal.valueOf(500));

        when(transferListService.getListingByIdAndStatusWithLock(listingId, ListingStatus.OPEN)).thenReturn(listing);
        when(teamService.getTeamIdByUserId(AuthUtils.getUserId())).thenReturn(buyerTeamId);
        when(teamService.getTeamByIdWithLock(buyerTeamId)).thenReturn(buyerTeam);
        when(teamService.getTeamByIdWithLock(sellerTeam.getId())).thenReturn(sellerTeam);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transferListFacade.purchaseListing(listingId));
        assertEquals(HttpStatus.PAYMENT_REQUIRED, exception.getStatusCode());
    }

    @Test
    void purchaseListing_ShouldThrowException_WhenBuyingFromOwnTeam() {
        Long listingId = 1L;
        Long teamId = 2L;

        TransferListEntity listing = new TransferListEntity();
        listing.setId(listingId);
        listing.setPrice(BigDecimal.valueOf(1000));
        listing.setStatus(ListingStatus.OPEN);
        TeamEntity sellerTeam = new TeamEntity();
        sellerTeam.setId(teamId);
        listing.setSellerTeam(sellerTeam);

        when(transferListService.getListingByIdAndStatusWithLock(listingId, ListingStatus.OPEN)).thenReturn(listing);
        when(teamService.getTeamIdByUserId(AuthUtils.getUserId())).thenReturn(teamId);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transferListFacade.purchaseListing(listingId));
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void purchaseListing_ShouldThrowException_WhenListingNotFound() {
        Long listingId = 1L;

        when(transferListService.getListingByIdAndStatusWithLock(listingId, ListingStatus.OPEN)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing not found"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> transferListFacade.purchaseListing(listingId));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

}