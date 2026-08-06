package com.yordanov.warehouse.User.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.yordanov.warehouse.User.Model.User;
import com.yordanov.warehouse.User.Model.UserRole;


@Component
public class AdminInit implements CommandLineRunner {

   private final UserService userService;
   private final PasswordEncoder passwordEncoder;

   @Autowired
    public AdminInit(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {

        Optional<User> adminInit = userService.findByUsername("admin123");

        if(adminInit.isPresent()) {
            return;
        }

       User admin =  User.builder()
                .username("admin123")
                .password(passwordEncoder.encode("admin123"))
                .email("admin123@gmail.com")
                .role(UserRole.ADMIN)
                .isActive(true)
                .build();

        userService.save(admin);

    }
}
