package com.example.orbitaldemo.model.mapper;

import com.example.orbitaldemo.model.domain.database.TransferListEntity;
import com.example.orbitaldemo.model.dto.TransferListItemDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransferListMapper {

    public static TransferListItemDTO toListingDTO(TransferListEntity listing) {
        return TransferListItemDTO.builder()
                .id(listing.getId())
                .sellerTeam(TeamMapper.toTeamShortDTO(listing.getSellerTeam()))
                .player(PlayerMapper.toPlayerDTO(listing.getPlayer()))
                .price(listing.getPrice())
                .createdAt(listing.getCreatedAt())
                .build();
    }

}
