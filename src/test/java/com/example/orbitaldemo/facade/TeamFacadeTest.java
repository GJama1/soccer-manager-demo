package com.example.orbitaldemo.facade;

import com.example.orbitaldemo.model.domain.database.CountryEntity;
import com.example.orbitaldemo.model.domain.database.PlayerEntity;
import com.example.orbitaldemo.model.domain.database.TeamEntity;
import com.example.orbitaldemo.model.dto.PagedDTO;
import com.example.orbitaldemo.model.dto.PlayerDTO;
import com.example.orbitaldemo.model.dto.TeamDTO;
import com.example.orbitaldemo.model.dto.TeamUpdateRequestDTO;
import com.example.orbitaldemo.service.CountryService;
import com.example.orbitaldemo.service.PlayerService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TeamFacadeTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private TeamService teamService;

    @Mock
    private CountryService countryService;

    @InjectMocks
    private TeamFacade teamFacade;

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
    void getUserTeam_ShouldReturnTeamDTO() {
        CountryEntity country = new CountryEntity();
        country.setId(1L);
        country.setName("Country");
        TeamEntity team = new TeamEntity();
        team.setId(1L);
        team.setCountry(country);
        when(teamService.getTeamByUserId(AuthUtils.getUserId())).thenReturn(team);

        TeamDTO result = teamFacade.getUserTeam();

        assertNotNull(result);
        assertEquals(team.getId(), result.getId());
        verify(teamService).getTeamByUserId(AuthUtils.getUserId());
    }

    @Test
    void getUserTeamPlayers_ShouldReturnPagedPlayers() {
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        TeamEntity team = new TeamEntity();
        team.setId(1L);
        Page<PlayerEntity> pagedPlayers = new PageImpl<>(Collections.emptyList());
        when(teamService.getTeamByUserId(AuthUtils.getUserId())).thenReturn(team);
        when(playerService.getPlayersByTeamId(team.getId(), pageRequest)).thenReturn(pagedPlayers);

        PagedDTO<PlayerDTO> result = teamFacade.getUserTeamPlayers(page, size);

        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        verify(teamService).getTeamByUserId(AuthUtils.getUserId());
        verify(playerService).getPlayersByTeamId(team.getId(), pageRequest);
    }

    @Test
    void updateTeam_ShouldUpdateAndReturnTeamDTO() {
        TeamUpdateRequestDTO request = new TeamUpdateRequestDTO();
        request.setName("Updated Team");
        request.setCountryId(2L);

        TeamEntity team = new TeamEntity();
        team.setId(1L);
        CountryEntity country = new CountryEntity();
        country.setId(2L);

        when(teamService.getTeamByUserId(AuthUtils.getUserId())).thenReturn(team);
        when(countryService.getCountryById(request.getCountryId())).thenReturn(country);
        when(teamService.saveTeam(team)).thenReturn(team);

        TeamDTO result = teamFacade.updateTeam(request);

        assertNotNull(result);
        assertEquals(team.getId(), result.getId());
        assertEquals(request.getName(), team.getName());
        assertEquals(request.getCountryId(), team.getCountry().getId());
        verify(teamService).getTeamByUserId(AuthUtils.getUserId());
        verify(countryService).getCountryById(request.getCountryId());
        verify(teamService).saveTeam(team);
    }

}