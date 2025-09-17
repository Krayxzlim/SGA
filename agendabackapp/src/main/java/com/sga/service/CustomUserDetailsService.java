package com.sga.service;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sga.model.Usuario;
import com.sga.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Mapear rol según subclase
        String role = usuario.getClass().getSimpleName().toUpperCase(); // ADMINISTRADOR, SUPERVISOR, TALLERISTA
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

        return new User(usuario.getEmail(), usuario.getPassword(), authorities);
    }
}
