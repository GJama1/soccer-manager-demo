package com.example.orbitaldemo.model.mapper;

import com.example.orbitaldemo.model.domain.database.PlayerEntity;
import com.example.orbitaldemo.model.dto.PlayerDTO;
import com.example.orbitaldemo.model.dto.PlayerShortDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerMapper {

    public static PlayerDTO toPlayerDTO(PlayerEntity player) {
        return PlayerDTO.builder()
                .id(player.getId())
                .firstname(player.getFirstName())
                .lastname(player.getLastName())
                .age(player.getAge())
                .position(player.getPosition())
                .marketValue(player.getMarketValue())
                .build();
    }

    public static PlayerShortDTO toPlayerShortDTO(PlayerEntity player) {
        return PlayerShortDTO.builder()
                .id(player.getId())
                .firstname(player.getFirstName())
                .lastname(player.getLastName())
                .build();
    }

}
