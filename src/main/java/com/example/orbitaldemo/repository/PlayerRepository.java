package com.example.orbitaldemo.repository;

import com.example.orbitaldemo.model.domain.database.PlayerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    @Query("""
            select p from PlayerEntity p
            join TeamPlayerEntity tp on p.id = tp.player.id
            where tp.team.id = :teamId
            """)
    Page<PlayerEntity> findAllByTeamId(@Param("teamId") Long teamId, Pageable pageRequest);

    @Query("""
            select p from PlayerEntity p
            join TeamPlayerEntity tp on p.id = tp.player.id
            join tp.team t
            where p.id = :playerId and t.id = :teamId
            """)
    Optional<PlayerEntity> findByPlayerIdAndTeamId(@Param("playerId") Long playerId, @Param("teamId") Long teamId);

}
