package com.example.orbitaldemo.service;

import com.example.orbitaldemo.config.properties.PlayerGenerationProperties;
import com.example.orbitaldemo.model.domain.database.PlayerEntity;
import com.example.orbitaldemo.model.enums.PlayerPosition;
import com.example.orbitaldemo.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerGenerationProperties genProps;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void savePlayer_ShouldSaveAndReturnPlayer() {
        PlayerEntity player = new PlayerEntity();
        when(playerRepository.save(player)).thenReturn(player);

        PlayerEntity result = playerService.savePlayer(player);

        assertNotNull(result);
        assertEquals(player, result);
        verify(playerRepository).save(player);
    }

    @Test
    void getPlayersByTeamId_ShouldReturnPagedPlayers() {
        Long teamId = 1L;
        Pageable pageable = Pageable.unpaged();
        Page<PlayerEntity> page = new PageImpl<>(Collections.emptyList());
        when(playerRepository.findAllByTeamId(teamId, pageable)).thenReturn(page);

        Page<PlayerEntity> result = playerService.getPlayersByTeamId(teamId, pageable);

        assertNotNull(result);
        assertEquals(page, result);
        verify(playerRepository).findAllByTeamId(teamId, pageable);
    }

    @Test
    void getPlayerByIdAndTeamId_ShouldReturnPlayer_WhenFound() {
        Long playerId = 1L;
        Long teamId = 2L;
        PlayerEntity player = new PlayerEntity();
        when(playerRepository.findByPlayerIdAndTeamId(playerId, teamId)).thenReturn(Optional.of(player));

        PlayerEntity result = playerService.getPlayerByIdAndTeamId(playerId, teamId);

        assertNotNull(result);
        assertEquals(player, result);
        verify(playerRepository).findByPlayerIdAndTeamId(playerId, teamId);
    }

    @Test
    void getPlayerByIdAndTeamId_ShouldThrowException_WhenNotFound() {
        Long playerId = 1L;
        Long teamId = 2L;
        when(playerRepository.findByPlayerIdAndTeamId(playerId, teamId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> playerService.getPlayerByIdAndTeamId(playerId, teamId));
        assertEquals("404 NOT_FOUND \"Player not found\"", exception.getMessage());
        verify(playerRepository).findByPlayerIdAndTeamId(playerId, teamId);
    }

    @Test
    void generateRandomPlayers_ShouldGenerateAndSavePlayers() {
        when(genProps.getGoalKeepers()).thenReturn(1);
        when(genProps.getDefenders()).thenReturn(2);
        when(genProps.getMidfielders()).thenReturn(3);
        when(genProps.getAttackers()).thenReturn(4);
        when(genProps.getMinAge()).thenReturn(18);
        when(genProps.getMaxAge()).thenReturn(35);
        when(genProps.getMarketValue()).thenReturn(new BigDecimal(1000));
        when(playerRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<PlayerEntity> result = playerService.generateRandomPlayers();

        assertNotNull(result);
        assertEquals(10, result.size());
        verify(playerRepository).saveAll(any());
    }

    @Test
    void generateNPlayersByPosition_ShouldGenerateCorrectNumberOfPlayers() {
        PlayerPosition position = PlayerPosition.DEF;
        int count = 3;
        when(genProps.getMinAge()).thenReturn(18);
        when(genProps.getMaxAge()).thenReturn(35);
        when(genProps.getMarketValue()).thenReturn(new BigDecimal(1000L));

        List<PlayerEntity> result = playerService.generateNPlayersByPosition(position, count);

        assertNotNull(result);
        assertEquals(count, result.size());
        result.forEach(player -> assertEquals(position, player.getPosition()));
    }

}