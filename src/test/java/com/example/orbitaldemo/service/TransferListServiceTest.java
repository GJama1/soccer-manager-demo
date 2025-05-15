package com.example.orbitaldemo.service;

import com.example.orbitaldemo.model.domain.database.TransferListEntity;
import com.example.orbitaldemo.model.dto.TransferListFilterRequestDTO;
import com.example.orbitaldemo.model.enums.ListingStatus;
import com.example.orbitaldemo.repository.TransferListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransferListServiceTest {

    @Mock
    private TransferListRepository transferListRepository;

    @InjectMocks
    private TransferListService transferListService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveListing_ShouldSaveAndReturnListing() {
        TransferListEntity listing = new TransferListEntity();
        when(transferListRepository.save(listing)).thenReturn(listing);

        TransferListEntity result = transferListService.saveListing(listing);

        assertNotNull(result);
        assertEquals(listing, result);
        verify(transferListRepository).save(listing);
    }

    @Test
    void getAllListings_ShouldReturnPagedListings() {
        TransferListFilterRequestDTO request = new TransferListFilterRequestDTO();
        List<ListingStatus> statuses = Collections.singletonList(ListingStatus.OPEN);
        Pageable pageable = Pageable.unpaged();
        Page<TransferListEntity> page = new PageImpl<>(Collections.emptyList());
        when(transferListRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<TransferListEntity> result = transferListService.getAllListings(request, statuses, pageable);

        assertNotNull(result);
        assertEquals(page, result);
        verify(transferListRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void getListingByIdAndStatusWithLock_ShouldReturnListing_WhenFound() {
        Long id = 1L;
        ListingStatus status = ListingStatus.OPEN;
        TransferListEntity listing = new TransferListEntity();
        when(transferListRepository.findByIdForWithLock(id, status)).thenReturn(Optional.of(listing));

        TransferListEntity result = transferListService.getListingByIdAndStatusWithLock(id, status);

        assertNotNull(result);
        assertEquals(listing, result);
        verify(transferListRepository).findByIdForWithLock(id, status);
    }

    @Test
    void getListingByIdAndStatusWithLock_ShouldThrowException_WhenNotFound() {
        Long id = 1L;
        ListingStatus status = ListingStatus.OPEN;
        when(transferListRepository.findByIdForWithLock(id, status)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> transferListService.getListingByIdAndStatusWithLock(id, status));
        assertEquals("404 NOT_FOUND \"Listing not found\"", exception.getMessage());
        verify(transferListRepository).findByIdForWithLock(id, status);
    }

    @Test
    void listingExists_ShouldReturnTrue_WhenListingExists() {
        Long playerId = 1L;
        Long teamId = 2L;
        ListingStatus status = ListingStatus.OPEN;
        when(transferListRepository.existsByPlayerIdAndSellerTeamIdAndStatus(playerId, teamId, status)).thenReturn(true);

        boolean result = transferListService.listingExists(playerId, teamId, status);

        assertTrue(result);
        verify(transferListRepository).existsByPlayerIdAndSellerTeamIdAndStatus(playerId, teamId, status);
    }

    @Test
    void listingExists_ShouldReturnFalse_WhenListingDoesNotExist() {
        Long playerId = 1L;
        Long teamId = 2L;
        ListingStatus status = ListingStatus.OPEN;
        when(transferListRepository.existsByPlayerIdAndSellerTeamIdAndStatus(playerId, teamId, status)).thenReturn(false);

        boolean result = transferListService.listingExists(playerId, teamId, status);

        assertFalse(result);
        verify(transferListRepository).existsByPlayerIdAndSellerTeamIdAndStatus(playerId, teamId, status);
    }

}