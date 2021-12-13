package com.srankoin.finance.service;

import com.srankoin.finance.dto.SignUpDto;
import com.srankoin.finance.entity.UserClass;
import com.srankoin.finance.repositories.UserClassRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserClassService {
    private final UserClassRepository userClassRepository;
    private final PasswordEncoder encoder;

    public UserClassService(UserClassRepository userClassRepository, PasswordEncoder encoder) {
        this.userClassRepository = userClassRepository;
        this.encoder = encoder;
    }

    public UserClass saveUser(SignUpDto signUpRequest) {

       UserClass user = UserClass.builder().username(signUpRequest.getUsername()).password(encoder.encode(signUpRequest.getPassword())).email(signUpRequest.getEmail()).build();

        return this.userClassRepository.save(user);
    }
}
