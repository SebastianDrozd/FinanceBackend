package com.srankoin.finance.controllers;

import com.srankoin.finance.dto.SignUpDto;
import com.srankoin.finance.entity.UserClass;
import com.srankoin.finance.repositories.UserClassRepository;
import com.srankoin.finance.service.UserClassService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserClassRepository userRepository;
    private final UserClassService userClassService;

    public AuthController(UserClassRepository userRepository, UserClassService userClassService) {
        this.userRepository = userRepository;
        this.userClassService = userClassService;
    }

    @PostMapping("/api/public/signup")
    public ResponseEntity<?> registerUser( @RequestBody SignUpDto signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }
       UserClass user = userClassService.saveUser(signUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}


