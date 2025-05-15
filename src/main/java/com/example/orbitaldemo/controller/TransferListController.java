package com.example.orbitaldemo.controller;

import com.example.orbitaldemo.facade.TransferListFacade;
import com.example.orbitaldemo.model.dto.TransferListCreateRequestDTO;
import com.example.orbitaldemo.model.dto.TransferListItemDTO;
import com.example.orbitaldemo.model.dto.PagedDTO;
import com.example.orbitaldemo.model.dto.TransferListFilterRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer-list")
@RequiredArgsConstructor
public class TransferListController {

    private final TransferListFacade transferListFacade;

    @GetMapping
    public ResponseEntity<PagedDTO<TransferListItemDTO>> getOpenListings(TransferListFilterRequestDTO request,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(transferListFacade.getOpenListings(request, page, size));
    }

    @PostMapping
    public ResponseEntity<TransferListItemDTO> createListing(@RequestBody @Valid TransferListCreateRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transferListFacade.createListing(request));
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<Void> purchaseListing(@PathVariable Long id) {
        transferListFacade.purchaseListing(id);
        return ResponseEntity.ok().build();
    }

}
