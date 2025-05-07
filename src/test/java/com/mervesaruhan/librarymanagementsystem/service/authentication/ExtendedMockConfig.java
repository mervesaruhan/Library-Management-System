package com.mervesaruhan.librarymanagementsystem.service.authentication;

import com.mervesaruhan.librarymanagementsystem.service.UserService;
import com.mervesaruhan.librarymanagementsystem.util.LogHelper;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ExtendedMockConfig {

    @Bean
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }

    @Bean
    public LogHelper logHelper() {
        return Mockito.mock(LogHelper.class);
    }
}