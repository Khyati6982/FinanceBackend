package com.zorvyn.financebackend.config;

import com.zorvyn.financebackend.model.Role;
import com.zorvyn.financebackend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("VIEWER").isEmpty()) {
                roleRepository.save(new Role("VIEWER"));
            }
            if (roleRepository.findByName("ANALYST").isEmpty()) {
                roleRepository.save(new Role("ANALYST"));
            }
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                roleRepository.save(new Role("ADMIN"));
            }
        };
    }
}