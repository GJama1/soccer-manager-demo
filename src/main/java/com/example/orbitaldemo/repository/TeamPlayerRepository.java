package com.example.orbitaldemo.repository;

import com.example.orbitaldemo.model.domain.database.TeamPlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface TeamPlayerRepository extends JpaRepository<TeamPlayerEntity, Long> {

    @Query("""
            select sum(p.marketValue) from TeamEntity t
            join TeamPlayerEntity tp on t.id = tp.team.id
            join tp.player p
            where t.id = :teamId
            """)
    BigDecimal calculateTeamValue(@Param("teamId") Long teamId);

    Optional<TeamPlayerEntity> findByPlayerIdAndTeamId(Long playerId, Long teamId);


}
