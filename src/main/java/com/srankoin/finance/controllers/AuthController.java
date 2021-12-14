package com.srankoin.finance.controllers;

import com.srankoin.finance.dto.JwtResponse;
import com.srankoin.finance.dto.LoginRequest;
import com.srankoin.finance.dto.SignUpDto;
import com.srankoin.finance.dto.VerifyTokenMessage;
import com.srankoin.finance.entity.UserClass;
import com.srankoin.finance.repositories.UserClassRepository;
import com.srankoin.finance.security.util.JwtUtil;
import com.srankoin.finance.service.UserClassService;
import org.apache.catalina.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final UserClassRepository userRepository;
    private final UserClassService userClassService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtUtil jwtUtil, UserClassRepository userRepository, UserClassService userClassService, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userClassService = userClassService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/api/public/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println(authentication);
        System.out.println(authentication.getPrincipal());
        UserClass user = (UserClass) authentication.getPrincipal();
        String jwt = this.jwtUtil.generateJwtToken(user);
        return ResponseEntity.ok(new JwtResponse(jwt, user.getUsername(), user.isAccountSetup()));

    }

    @CrossOrigin(origins = "*")
    @PostMapping("/api/public/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpRequest) {
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

    @PostMapping("/api/public/verify")
    public ResponseEntity<?> verifytoken(@RequestHeader HttpHeaders httpHeaders) {
        //read the authorization headers
        String token = httpHeaders.getFirst(HttpHeaders.AUTHORIZATION);
        //set empty username varbale
        String snipUsername = "";
        String realToken = token.substring(7);
        System.out.println("realtoken");
        //verify the jwt
        if (realToken != null && jwtUtil.validateJwtToken(realToken)) {
            String username = jwtUtil.getUserNameFromJwtToken(realToken);
            //return response
            return ResponseEntity.ok(new VerifyTokenMessage("valid", username));
        }
        return ResponseEntity.ok(new VerifyTokenMessage("invalid", null));
    }
}


