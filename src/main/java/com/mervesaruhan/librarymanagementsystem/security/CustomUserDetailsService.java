package com.mervesaruhan.librarymanagementsystem.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username.equals("librarian")) {
            return User.builder()
                    .username("librarian")
                    .password(new BCryptPasswordEncoder().encode("password123"))
                    .roles("LIBRARIAN")
                    .build();
        }


        if (username.equals("patron")) {
            return User.builder()
                    .username("patron")
                    .password(new BCryptPasswordEncoder().encode("password321"))
                    .roles("PATRON")
                    .build();
        }

        throw new UsernameNotFoundException("User not found");

    }
}
