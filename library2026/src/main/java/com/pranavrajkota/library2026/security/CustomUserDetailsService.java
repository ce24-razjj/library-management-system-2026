package com.pranavrajkota.library2026.security;

import com.pranavrajkota.library2026.entity.User;
import com.pranavrajkota.library2026.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("USER with username " + username + " not found"));
        //
        System.out.println("DB USER: " + user.getUserName());
        System.out.println("DB PASS: " + user.getPassword());
        //

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .roles(user.getRole().split(","))
                .build();
    }
}
