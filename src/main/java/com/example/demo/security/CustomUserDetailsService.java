package com.example.demo.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

public class CustomUserDetailsService implements UserDetailsService {

    private final Map<String, String> users = new HashMap<>();
    private final Map<String, String> roles = new HashMap<>();

    public CustomUserDetailsService() {
    }

    // ***** IMPORTANT FOR TESTS *****
    public Map<String, Object> registerUser(String name,
                                             String email,
                                             String password,
                                             String role) {
        Map<String, Object> map = new HashMap<>();
        long id = new Random().nextLong(1000, 9999);

        map.put("userId", id);
        map.put("name", name);
        map.put("email", email);
        map.put("password", password);
        map.put("role", role);

        users.put(email, password);
        roles.put(email, role);

        return map;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        if (!users.containsKey(username)) {
            throw new UsernameNotFoundException("User not found");
        }

        return User
                .withUsername(username)
                .password(users.get(username))
                .roles(roles.get(username))
                .build();
    }
}
