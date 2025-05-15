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
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private final UserService userService;

    private final TeamService teamService;

    private final CountryService countryService;

    private final PlayerService playerService;

    @Transactional
    public void createUser(UserCreateDTO request) {

        UserEntity user = userService.createUser(request.getEmail(), request.getPassword());
        CountryEntity country = countryService.getCountryById(request.getCountryId());
        TeamEntity team = teamService.createTeam(user, country, request.getTeamName());
        List<PlayerEntity> players = playerService.generateRandomPlayers();

        teamService.assignPlayersToTeam(team, players);
        teamService.calculateTeamValue(team);
    }


}
