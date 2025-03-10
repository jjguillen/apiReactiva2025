package com.jaroso.apireactiva.services;

import com.jaroso.apireactiva.dto.UserRegisterDTO;
import com.jaroso.apireactiva.entities.User;
import com.jaroso.apireactiva.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public Mono<User> findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public Mono<User> save(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        user.setUsername(userRegisterDTO.username());
        user.setEmail(userRegisterDTO.email());
        user.setPassword(this.passwordEncoder.encode(userRegisterDTO.password()));
        return this.userRepository.save(user);
    }



}
