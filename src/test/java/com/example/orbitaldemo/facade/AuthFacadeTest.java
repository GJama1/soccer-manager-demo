package com.example.orbitaldemo.facade;

import com.example.orbitaldemo.model.domain.database.CountryEntity;
import com.example.orbitaldemo.model.domain.database.PlayerEntity;
import com.example.orbitaldemo.model.domain.database.TeamEntity;
import com.example.orbitaldemo.model.domain.database.UserEntity;
import com.example.orbitaldemo.model.dto.UserCreateDTO;
import com.example.orbitaldemo.service.CountryService;
import com.example.orbitaldemo.service.PlayerService;
import com.example.orbitaldemo.service.TeamService;
import com.example.orbitaldemo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthFacadeTest {

    @Mock
    private UserService userService;

    @Mock
    private TeamService teamService;

    @Mock
    private CountryService countryService;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private AuthFacade authFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldCreateUserAndAssignTeamAndPlayers() {
        UserCreateDTO request = new UserCreateDTO();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setCountryId(1L);
        request.setTeamName("Test Team");

        UserEntity user = new UserEntity();
        CountryEntity country = new CountryEntity();
        TeamEntity team = new TeamEntity();
        List<PlayerEntity> players = Collections.singletonList(new PlayerEntity());

        when(userService.createUser(request.getEmail(), request.getPassword())).thenReturn(user);
        when(countryService.getCountryById(request.getCountryId())).thenReturn(country);
        when(teamService.createTeam(user, country, request.getTeamName())).thenReturn(team);
        when(playerService.generateRandomPlayers()).thenReturn(players);

        authFacade.createUser(request);

        verify(userService).createUser(request.getEmail(), request.getPassword());
        verify(countryService).getCountryById(request.getCountryId());
        verify(teamService).createTeam(user, country, request.getTeamName());
        verify(playerService).generateRandomPlayers();
        verify(teamService).assignPlayersToTeam(team, players);
        verify(teamService).calculateTeamValue(team);
    }

}