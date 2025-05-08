package com.mervesaruhan.librarymanagementsystem.Tests.authentication;

import com.mervesaruhan.librarymanagementsystem.Tests.UserService;
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