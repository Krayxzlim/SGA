package com.sga.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sga.model.Usuario;
import com.sga.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Crear usuario (recibe cualquier subclase de Usuario)
    public Usuario createUsuario(Usuario usuario) {
        if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
            // si no está ya en BCrypt, la codificamos
            if (!isEncoded(usuario.getPassword())) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        }
        return usuarioRepository.save(usuario);
    }

    // Listar todos los usuarios
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // Buscar por id
    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Actualizar usuario
    public Usuario updateUsuario(Long id, Usuario updated) {
        return usuarioRepository.findById(id).map(u -> {
            u.setNombre(updated.getNombre());
            u.setEmail(updated.getEmail());

            // Si enviaron password y no está vacía, actualizamos (codificando si es necesario)
            if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
                if (!isEncoded(updated.getPassword())) {
                    u.setPassword(passwordEncoder.encode(updated.getPassword()));
                } else {
                    u.setPassword(updated.getPassword());
                }
            }

            // No cambiamos la subclase (rol) en este método por simplicidad
            return usuarioRepository.save(u);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Eliminar usuario
    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Validación de rol según subclase
    public boolean hasRole(Usuario usuario, Class<? extends Usuario> roleClass) {
        return roleClass.isInstance(usuario);
    }

    // Helpers
    private boolean isEncoded(String password) {
        if (password == null) return false;
        // BCrypt hashes suelen empezar con $2a$, $2b$, $2y$
        return password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$");
    }
}
