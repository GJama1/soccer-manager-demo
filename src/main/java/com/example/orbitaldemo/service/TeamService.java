package com.example.orbitaldemo.service;

import com.example.orbitaldemo.model.domain.database.*;
import com.example.orbitaldemo.repository.TeamPlayerRepository;
import com.example.orbitaldemo.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
@RequiredArgsConstructor
public class TeamService {

    private static final String TEAM_NOT_FOUND = "Team not found";

    @Value("${application.constants.team-generation.budget}")
    private BigDecimal defaultBudget;

    private final TeamRepository teamRepository;

    private final TeamPlayerRepository teamPlayerRepository;

    public void calculateTeamValue(TeamEntity team) {
        BigDecimal totalValue = teamPlayerRepository.calculateTeamValue(team.getId());
        team.setTeamValue(totalValue);
        saveTeam(team);
    }

    public TeamEntity getTeamByUserId(Long userId) {
        return teamRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, TEAM_NOT_FOUND));
    }

    public Long getTeamIdByUserId(Long userId) {
        return teamRepository.findIdByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, TEAM_NOT_FOUND));
    }

    public TeamEntity getTeamByIdWithLock(Long id) {
        return teamRepository.findByIdWithLock(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, TEAM_NOT_FOUND));
    }

    public TeamPlayerEntity findRelationByPlayerIdAndTeamId(Long playerId, Long teamId) {
        return teamPlayerRepository.findByPlayerIdAndTeamId(playerId, teamId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Player not found in team"));
    }

    public void assignPlayersToTeam(TeamEntity team,
                                    List<PlayerEntity> players) {
        List<TeamPlayerEntity> teamPlayers = players.stream().map(p -> {
            TeamPlayerEntity teamPlayer = new TeamPlayerEntity();
            teamPlayer.setTeam(team);
            teamPlayer.setPlayer(p);
            return teamPlayer;
        }).toList();
        teamPlayerRepository.saveAll(teamPlayers);
    }

    public TeamPlayerEntity saveRelation(TeamPlayerEntity teamPlayer) {
        return teamPlayerRepository.save(teamPlayer);
    }

    public void deleteRelation(TeamPlayerEntity teamPlayer) {
        teamPlayerRepository.delete(teamPlayer);
    }

    public TeamEntity createTeam(UserEntity user,
                                 CountryEntity country,
                                 String name) {
        TeamEntity team = new TeamEntity();
        team.setUser(user);
        team.setTeamValue(BigDecimal.ZERO);
        team.setName(name);
        team.setCountry(country);
        team.setBudget(defaultBudget);
        team.setTeamValue(BigDecimal.ZERO);
        return saveTeam(team);
    }

    public TeamEntity saveTeam(TeamEntity team) {
        return teamRepository.save(team);
    }

}
