package com.mervesaruhan.librarymanagementsystem.authentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class TestMethodSecurityConfig {

}