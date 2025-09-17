package com.sga.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sga.model.Usuario;
import com.sga.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService UsuarioService;

    public UsuarioController(UsuarioService UsuarioService) {
        this.UsuarioService = UsuarioService;
    }

    @PostMapping
    public Usuario create(@RequestBody Usuario Usuario) {
        return UsuarioService.createUsuario(Usuario);
    }

    @GetMapping
    public List<Usuario> list() {
        return UsuarioService.getAllUsuarios();
    }

    @GetMapping("/{id}")
    public Usuario get(@PathVariable Long id) {
        return UsuarioService.getUsuarioById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public Usuario update(@PathVariable Long id, @RequestBody Usuario Usuario) {
        return UsuarioService.updateUsuario(id, Usuario);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        UsuarioService.deleteUsuario(id);
    }
}
