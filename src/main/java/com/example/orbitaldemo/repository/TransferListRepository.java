package com.example.orbitaldemo.repository;

import com.example.orbitaldemo.model.domain.database.TransferListEntity;
import com.example.orbitaldemo.model.enums.ListingStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferListRepository extends JpaRepository <TransferListEntity, Long>, JpaSpecificationExecutor<TransferListEntity> {

    boolean existsByPlayerIdAndSellerTeamIdAndStatus(Long playerId, Long teamId, ListingStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select tl from TransferListEntity tl
            WHERE tl.id = :id
            """)
    Optional<TransferListEntity> findByIdForWithLock(@Param("id") Long id, ListingStatus status);

}
