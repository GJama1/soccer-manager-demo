package com.example.orbitaldemo.model.mapper;

import com.example.orbitaldemo.model.domain.database.TeamEntity;
import com.example.orbitaldemo.model.dto.TeamDTO;
import com.example.orbitaldemo.model.dto.TeamShortDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamMapper {

    public static TeamDTO toTeamDTO(TeamEntity team) {
        return TeamDTO.builder()
                .id(team.getId())
                .name(team.getName())
                .country(CountryMapper.toCountryDTO(team.getCountry()))
                .teamValue(team.getTeamValue())
                .budget(team.getBudget())
                .build();
    }

    public static TeamShortDTO toTeamShortDTO(TeamEntity team) {
        return TeamShortDTO.builder()
                .id(team.getId())
                .name(team.getName())
                .build();
    }

}
