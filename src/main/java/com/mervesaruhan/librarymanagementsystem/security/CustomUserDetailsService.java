package com.mervesaruhan.librarymanagementsystem.security;

import com.mervesaruhan.librarymanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Statik kullanıcı - librarian
        if (username.equals("librarian")) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username("librarian")
                    .password(passwordEncoder.encode("password123"))
                    .roles("LIBRARIAN")
                    .build();
        }

        // Statik kullanıcı - patron
        if (username.equals("patron")) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username("patron")
                    .password(passwordEncoder.encode("password321"))
                    .roles("PATRON")
                    .build();
        }

        // Veritabanından kullanıcıyı ara
        com.mervesaruhan.librarymanagementsystem.model.entity.User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();
    }
}