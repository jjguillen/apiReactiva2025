package com.jaroso.apireactiva.security;

import com.jaroso.apireactiva.services.UserService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserDetailServiceImpl implements ReactiveUserDetailsService {

    private final UserService userService;

    public UserDetailServiceImpl(UserService userService) {
        this.userService = userService;
    }


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return this.userService.findByUsername(username)
                .map(user -> (UserDetails) user);  //Castear mi User a UserDetails (User implementa UserDetails)
    }
}
