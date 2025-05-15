package com.example.orbitaldemo.controller;

import com.example.orbitaldemo.facade.TeamFacade;
import com.example.orbitaldemo.model.dto.PagedDTO;
import com.example.orbitaldemo.model.dto.PlayerDTO;
import com.example.orbitaldemo.model.dto.TeamDTO;
import com.example.orbitaldemo.model.dto.TeamUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamFacade teamFacade;

    @GetMapping("/my-team")
    public ResponseEntity<TeamDTO> getUserTeam() {
        return ResponseEntity.ok(teamFacade.getUserTeam());
    }

    @GetMapping("/my-team/players")
    public ResponseEntity<PagedDTO<PlayerDTO>> getUserTeamPlayers(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(teamFacade.getUserTeamPlayers(page, size));
    }

    @PutMapping("/my-team")
    public ResponseEntity<TeamDTO> updateTeam(@RequestBody TeamUpdateRequestDTO request) {
        return ResponseEntity.ok(teamFacade.updateTeam(request));
    }

}
