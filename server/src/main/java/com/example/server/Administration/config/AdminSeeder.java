package com.example.server.Administration.config;

import com.example.server.Administration.model.Administrator;
import com.example.server.Administration.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements ApplicationRunner {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {

        if (!userRepository.existsByLogin("admin")) {
            Administrator admin = Administrator.builder()
                    .login("admin")
                    .email("admin@admin.pl")
                    .active(true)
                    .passwordHash(passwordEncoder.encode("admin"))
                    .firstName("admin")
                    .lastName("admin")
                    .build();

            userRepository.save(admin);
        }
    }
}

