package com.sga.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sga.model.Usuario;
import com.sga.repository.UsuarioRepository;

@Configuration
public class DataLoader {
    
    @SuppressWarnings("unused")
    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepo,
                                   PasswordEncoder encoder) {
        return args -> {

            if (usuarioRepo.findByEmail("admin@sga.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Administrador");
                admin.setEmail("admin@sga.com");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRol("ROLE_ADMINISTRADOR");
                usuarioRepo.save(admin);
            }

            if (usuarioRepo.findByEmail("taller@sga.com").isEmpty()) {
                Usuario taller = new Usuario();
                taller.setNombre("Tallerista");
                taller.setEmail("taller@sga.com");
                taller.setPassword(encoder.encode("taller123"));
                taller.setRol("ROLE_TALLERISTA");
                usuarioRepo.save(taller);
            }

            if (usuarioRepo.findByEmail("usuario@sga.com").isEmpty()) {
                Usuario user = new Usuario();
                user.setNombre("Usuario Común");
                user.setEmail("usuario@sga.com");
                user.setPassword(encoder.encode("usuario123"));
                user.setRol("ROLE_USUARIO");
                usuarioRepo.save(user);
            }
        };
    }
}
