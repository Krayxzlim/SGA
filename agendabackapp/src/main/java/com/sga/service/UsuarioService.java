package com.sga.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sga.model.Usuario;
import com.sga.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Crear usuario (recibe cualquier subclase de Usuario)
    public Usuario createUsuario(Usuario usuario) {
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

    // Actualizar usuario
    public Usuario updateUsuario(Long id, Usuario updated) {
        return usuarioRepository.findById(id).map(u -> {
            u.setNombre(updated.getNombre());
            u.setEmail(updated.getEmail());
            u.setPassword(updated.getPassword());
            // No cambiamos la subclase (rol) porque eso implica crear otra entidad
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
}
