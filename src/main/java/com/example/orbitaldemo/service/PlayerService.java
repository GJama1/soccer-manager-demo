package com.example.orbitaldemo.service;

import com.example.orbitaldemo.config.properties.PlayerGenerationProperties;
import com.example.orbitaldemo.model.domain.database.PlayerEntity;
import com.example.orbitaldemo.model.enums.PlayerPosition;
import com.example.orbitaldemo.repository.PlayerRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;

import static com.example.orbitaldemo.model.enums.PlayerPosition.*;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final Faker faker = new Faker();

    private final PlayerRepository playerRepository;

    private final PlayerGenerationProperties genProps;

    @Transactional(propagation = Propagation.SUPPORTS)
    public PlayerEntity savePlayer(PlayerEntity player) {
        return playerRepository.save(player);
    }

    public Page<PlayerEntity> getPlayersByTeamId(Long teamId, Pageable pageRequest) {
        return playerRepository.findAllByTeamId(teamId, pageRequest);
    }

    public PlayerEntity getPlayerByIdAndTeamId(Long playerId, Long teamId) {
        return playerRepository.findByPlayerIdAndTeamId(playerId, teamId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<PlayerEntity> generateRandomPlayers() {

        List<PlayerEntity> players = new LinkedList<>();
        players.addAll(generateNPlayersByPosition(GLK, genProps.getGoalKeepers()));
        players.addAll(generateNPlayersByPosition(DEF, genProps.getDefenders()));
        players.addAll(generateNPlayersByPosition(MID, genProps.getMidfielders()));
        players.addAll(generateNPlayersByPosition(ATT, genProps.getAttackers()));

        return playerRepository.saveAll(players);
    }

    public List<PlayerEntity> generateNPlayersByPosition(PlayerPosition position, int count) {

        List<PlayerEntity> players = new LinkedList<>();
        for (int i = 0; i < count; i++) {

            PlayerEntity player = new PlayerEntity();
            player.setFirstName(faker.name().firstName());
            player.setLastName(faker.name().lastName());
            player.setAge(faker.number().numberBetween(genProps.getMinAge(), genProps.getMaxAge() + 1));
            player.setPosition(position);
            player.setMarketValue(genProps.getMarketValue());

            players.add(player);
        }

        return players;
    }

}
