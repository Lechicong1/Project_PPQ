package com.example.PPQ.Config;

import com.example.PPQ.Entity.UserEntity;
import com.example.PPQ.respository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig
{
    @Autowired
    private UsersRepository user_repository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner runner() {
        return args -> {
            if (user_repository.findByUsername("admin") == null) {
                UserEntity user = new UserEntity();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin"));
                user.setIdRoles(16);
                try {
                    user_repository.save(user);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}

