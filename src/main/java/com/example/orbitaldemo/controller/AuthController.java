package com.example.orbitaldemo.controller;

import com.example.orbitaldemo.facade.AuthFacade;
import com.example.orbitaldemo.model.dto.UserCreateDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/signup")
    public ResponseEntity<Void> signupUser(@RequestBody @Valid UserCreateDTO request) {
        authFacade.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
