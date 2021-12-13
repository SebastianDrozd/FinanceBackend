package com.srankoin.finance.repositories;

import com.srankoin.finance.entity.UserClass;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserClassRepository extends JpaRepository<UserClass, UUID> {
    Optional<UserClass> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
