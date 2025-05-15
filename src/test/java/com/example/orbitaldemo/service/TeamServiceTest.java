package com.example.orbitaldemo.service;

import com.example.orbitaldemo.model.domain.database.*;
import com.example.orbitaldemo.repository.TeamPlayerRepository;
import com.example.orbitaldemo.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamPlayerRepository teamPlayerRepository;

    @InjectMocks
    private TeamService teamService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTeamByUserId_ShouldReturnTeam_WhenTeamExists() {
        // Arrange
        Long userId = 1L;
        TeamEntity team = new TeamEntity();
        when(teamRepository.findByUserId(userId)).thenReturn(Optional.of(team));

        // Act
        TeamEntity result = teamService.getTeamByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(team, result);
        verify(teamRepository).findByUserId(userId);
    }

    @Test
    void getTeamByUserId_ShouldThrowException_WhenTeamNotFound() {
        // Arrange
        Long userId = 1L;
        when(teamRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> teamService.getTeamByUserId(userId));
        assertEquals("404 NOT_FOUND \"Team not found\"", exception.getMessage());
        verify(teamRepository).findByUserId(userId);
    }

    @Test
    void calculateTeamValue_ShouldUpdateTeamValue() {
        // Arrange
        Long teamId = 1L;
        BigDecimal teamValue = new BigDecimal("1000");
        TeamEntity team = new TeamEntity();
        team.setId(teamId);
        when(teamPlayerRepository.calculateTeamValue(teamId)).thenReturn(teamValue);
        when(teamRepository.save(team)).thenReturn(team);

        // Act
        teamService.calculateTeamValue(team);

        // Assert
        assertEquals(teamValue, team.getTeamValue());
        verify(teamPlayerRepository).calculateTeamValue(teamId);
        verify(teamRepository).save(team);
    }

    @Test
    void createTeam_ShouldCreateAndSaveTeam() {
        UserEntity user = new UserEntity();
        CountryEntity country = new CountryEntity();
        String name = "Test Team";
        TeamEntity team = new TeamEntity();
        team.setName(name);
        when(teamRepository.save(any(TeamEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TeamEntity result = teamService.createTeam(user, country, name);

        assertNotNull(result);
        assertEquals(name, result.getName());
        verify(teamRepository).save(any(TeamEntity.class));
    }

    @Test
    void getTeamIdByUserId_ShouldReturnTeamId_WhenTeamExists() {
        Long userId = 1L;
        Long teamId = 2L;
        when(teamRepository.findIdByUserId(userId)).thenReturn(Optional.of(teamId));

        Long result = teamService.getTeamIdByUserId(userId);

        assertNotNull(result);
        assertEquals(teamId, result);
        verify(teamRepository).findIdByUserId(userId);
    }

    @Test
    void getTeamIdByUserId_ShouldThrowException_WhenTeamNotFound() {
        Long userId = 1L;
        when(teamRepository.findIdByUserId(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> teamService.getTeamIdByUserId(userId));
        assertEquals("404 NOT_FOUND \"Team not found\"", exception.getMessage());
        verify(teamRepository).findIdByUserId(userId);
    }

    @Test
    void getTeamByIdWithLock_ShouldReturnTeam_WhenTeamExists() {
        Long teamId = 1L;
        TeamEntity team = new TeamEntity();
        when(teamRepository.findByIdWithLock(teamId)).thenReturn(Optional.of(team));

        TeamEntity result = teamService.getTeamByIdWithLock(teamId);

        assertNotNull(result);
        assertEquals(team, result);
        verify(teamRepository).findByIdWithLock(teamId);
    }

    @Test
    void getTeamByIdWithLock_ShouldThrowException_WhenTeamNotFound() {
        Long teamId = 1L;
        when(teamRepository.findByIdWithLock(teamId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> teamService.getTeamByIdWithLock(teamId));
        assertEquals("404 NOT_FOUND \"Team not found\"", exception.getMessage());
        verify(teamRepository).findByIdWithLock(teamId);
    }

    @Test
    void findRelationByPlayerIdAndTeamId_ShouldReturnRelation_WhenExists() {
        Long playerId = 1L;
        Long teamId = 2L;
        TeamPlayerEntity relation = new TeamPlayerEntity();
        when(teamPlayerRepository.findByPlayerIdAndTeamId(playerId, teamId)).thenReturn(Optional.of(relation));

        TeamPlayerEntity result = teamService.findRelationByPlayerIdAndTeamId(playerId, teamId);

        assertNotNull(result);
        assertEquals(relation, result);
        verify(teamPlayerRepository).findByPlayerIdAndTeamId(playerId, teamId);
    }

    @Test
    void findRelationByPlayerIdAndTeamId_ShouldThrowException_WhenNotFound() {
        Long playerId = 1L;
        Long teamId = 2L;
        when(teamPlayerRepository.findByPlayerIdAndTeamId(playerId, teamId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> teamService.findRelationByPlayerIdAndTeamId(playerId, teamId));
        assertEquals("404 NOT_FOUND \"Player not found in team\"", exception.getMessage());
        verify(teamPlayerRepository).findByPlayerIdAndTeamId(playerId, teamId);
    }

    @Test
    void assignPlayersToTeam_ShouldSaveAllRelations() {
        TeamEntity team = new TeamEntity();
        PlayerEntity player1 = new PlayerEntity();
        PlayerEntity player2 = new PlayerEntity();
        List<PlayerEntity> players = List.of(player1, player2);

        teamService.assignPlayersToTeam(team, players);

        verify(teamPlayerRepository).saveAll(anyList());
    }

    @Test
    void saveRelation_ShouldSaveAndReturnRelation() {
        TeamPlayerEntity relation = new TeamPlayerEntity();
        when(teamPlayerRepository.save(relation)).thenReturn(relation);

        TeamPlayerEntity result = teamService.saveRelation(relation);

        assertNotNull(result);
        assertEquals(relation, result);
        verify(teamPlayerRepository).save(relation);
    }

    @Test
    void deleteRelation_ShouldDeleteRelation() {
        TeamPlayerEntity relation = new TeamPlayerEntity();

        teamService.deleteRelation(relation);

        verify(teamPlayerRepository).delete(relation);
    }

}