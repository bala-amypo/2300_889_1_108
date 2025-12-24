package com.example.demo.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Map<String, UserDetails> users = new HashMap<>();
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // *** TEST EXPECTS THIS METHOD ***
    public String registerUser(String username, String password, String email, String role) {
        UserDetails user = User.builder()
                .username(username)
                .password(encoder.encode(password))
                .authorities(new SimpleGrantedAuthority(role))
                .build();

        users.put(username, user);
        return "User Registered Successfully";
    }

    // *** TEST EXPECTS THIS OVERRIDE ***
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        if (!users.containsKey(username)) {
            throw new UsernameNotFoundException("User Not Found");
        }
        return users.get(username);
    }
}
