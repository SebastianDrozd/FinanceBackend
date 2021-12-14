package com.srankoin.finance.service;

import com.srankoin.finance.entity.UserClass;
import com.srankoin.finance.repositories.UserClassRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserClassRepository userClassRepository;

    public UserDetailsServiceImpl(UserClassRepository userClassRepository) {
        this.userClassRepository = userClassRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserClass userModel = userClassRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username notfound"));
        return userModel;
    }

}
