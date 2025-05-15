package com.example.orbitaldemo.repository;

import com.example.orbitaldemo.model.domain.database.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Page<OrderEntity> findAllByBuyerTeamId(Long buyerTeamId, Pageable pageRequest);

}
