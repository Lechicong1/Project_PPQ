package com.example.PPQ.Security;

import com.example.PPQ.Entity.User_Entity;
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
                User_Entity user = new User_Entity();
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

