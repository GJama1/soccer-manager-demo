package com.example.orbitaldemo.repository;

import com.example.orbitaldemo.model.domain.database.TeamEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    Optional<TeamEntity> findByUserId(Long ownerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from TeamEntity t where t.id = :id")
    Optional<TeamEntity> findByIdWithLock(@Param("id") Long id);

    @Query("select t.id from TeamEntity t where t.user.id = :userId")
    Optional<Long> findIdByUserId(@Param("userId") Long userId);

}
