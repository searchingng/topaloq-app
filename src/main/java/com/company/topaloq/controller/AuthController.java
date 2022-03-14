package com.company.topaloq.controller;

import com.company.topaloq.dto.auth.AuthDTO;
import com.company.topaloq.dto.auth.RegistrationDTO;
import com.company.topaloq.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registration")
    public ResponseEntity registration(@Valid @RequestBody RegistrationDTO dto){
        return ResponseEntity.ok(authService.registration(dto));
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody AuthDTO dto){
        return ResponseEntity.ok(authService.login(dto));
    }

}
