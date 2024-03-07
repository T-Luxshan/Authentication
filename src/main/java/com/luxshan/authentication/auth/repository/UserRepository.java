package com.luxshan.authentication.auth.repository;

import com.luxshan.authentication.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

//    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String username);
}
