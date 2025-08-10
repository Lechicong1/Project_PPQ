package com.example.PPQ.Config;

import com.example.PPQ.Entity.RolesEntity;
import com.example.PPQ.Entity.UserEntity;
import com.example.PPQ.Enums.Role;
import com.example.PPQ.respository.RoleRepository;
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
    @Autowired
    RoleRepository  roleRepo;

    @Bean
    ApplicationRunner runner() {
        return args -> {
            // them role truoc
            // Thêm role nếu chưa có
            for (Role roleType : Role.values()) {
                if (roleRepo.findByRoleName(roleType) == null) {
                    RolesEntity role = new RolesEntity();
                    role.setRoleName(roleType.name());
                    roleRepo.save(role);
                }
            }
            if (user_repository.findByUsername("admin") == null) {
                UserEntity user = new UserEntity();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin"));
                user.setIdRoles(roleRepo.findByRoleName(Role.ADMIN).getId());
                try {
                    user_repository.save(user);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}

