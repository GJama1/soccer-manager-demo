package com.example.orbitaldemo.facade;

import com.example.orbitaldemo.model.domain.database.TeamEntity;
import com.example.orbitaldemo.model.dto.PagedDTO;
import com.example.orbitaldemo.model.dto.PlayerDTO;
import com.example.orbitaldemo.model.dto.TeamDTO;
import com.example.orbitaldemo.model.dto.TeamUpdateRequestDTO;
import com.example.orbitaldemo.model.mapper.PlayerMapper;
import com.example.orbitaldemo.model.mapper.TeamMapper;
import com.example.orbitaldemo.service.CountryService;
import com.example.orbitaldemo.service.PlayerService;
import com.example.orbitaldemo.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.example.orbitaldemo.util.AuthUtils.getUserId;

@Service
@RequiredArgsConstructor
public class TeamFacade {

    private final PlayerService playerService;

    private final TeamService teamService;

    private final CountryService countryService;

    public TeamDTO getUserTeam() {
        return TeamMapper.toTeamDTO(getTeamByPrincipalUserId());
    }

    public PagedDTO<PlayerDTO> getUserTeamPlayers(int page, int size) {
        TeamEntity team = getTeamByPrincipalUserId();
        return PagedDTO.of(
                playerService.getPlayersByTeamId(team.getId(), PageRequest.of(page, size)).map(PlayerMapper::toPlayerDTO)
        );
    }

    public TeamDTO updateTeam(TeamUpdateRequestDTO request) {
        TeamEntity team = getTeamByPrincipalUserId();
        if (StringUtils.hasText(request.getName())) {
            team.setName(request.getName());
        }
        if (request.getCountryId() != null) {
            team.setCountry(countryService.getCountryById(request.getCountryId()));
        }
        return TeamMapper.toTeamDTO(teamService.saveTeam(team));
    }

    private TeamEntity getTeamByPrincipalUserId() {
        return teamService.getTeamByUserId(getUserId());
    }

}
