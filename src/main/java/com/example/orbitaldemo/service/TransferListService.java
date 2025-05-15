package com.example.orbitaldemo.service;

import com.example.orbitaldemo.model.domain.database.PlayerEntity;
import com.example.orbitaldemo.model.domain.database.TransferListEntity;
import com.example.orbitaldemo.model.dto.TransferListFilterRequestDTO;
import com.example.orbitaldemo.model.enums.ListingStatus;
import com.example.orbitaldemo.repository.TransferListRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferListService {

    private final TransferListRepository transferListRepository;

    @Transactional(propagation = Propagation.SUPPORTS)
    public TransferListEntity saveListing(TransferListEntity listing) {
        return transferListRepository.save(listing);
    }

    public Page<TransferListEntity> getAllListings(TransferListFilterRequestDTO request,
                                                   List<ListingStatus> statuses,
                                                   Pageable pageRequest) {
        Specification<TransferListEntity> specification = getSpecification(request, statuses);
        return transferListRepository.findAll(specification, pageRequest);
    }

    @Transactional
    public TransferListEntity getListingByIdAndStatusWithLock(Long id, ListingStatus status) {
        return transferListRepository.findByIdForWithLock(id, status)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing not found"));
    }

    public boolean listingExists(Long playerId, Long teamId, ListingStatus status) {
        return transferListRepository.existsByPlayerIdAndSellerTeamIdAndStatus(playerId, teamId, status);
    }

    private Specification<TransferListEntity> getSpecification(TransferListFilterRequestDTO request,
                                                               List<ListingStatus> statuses) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new LinkedList<>();

            if (statuses != null && !statuses.isEmpty()) {
                predicates.add(root.get("status").in(statuses));
            }

            if (request != null) {
                Join<TransferListEntity, PlayerEntity> playerJoin = root.join("player", JoinType.LEFT);
                if (request.getMinPrice() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("price"), request.getMinPrice()));
                }
                if (request.getMaxPrice() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("price"), request.getMaxPrice()));
                }
                if (request.getMinAge() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(playerJoin.get("age"), request.getMinAge()));
                }
                if (request.getMaxAge() != null) {
                    predicates.add(cb.lessThanOrEqualTo(playerJoin.get("age"), request.getMaxAge()));
                }
                if (StringUtils.hasText(request.getSearchText())) {
                    String searchText = "%" + request.getSearchText().toLowerCase() + "%";
                    predicates.add(cb.or(
                            cb.like(cb.lower(playerJoin.get("firstName")), searchText),
                            cb.like(cb.lower(playerJoin.get("lastName")), searchText)
                    ));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}
