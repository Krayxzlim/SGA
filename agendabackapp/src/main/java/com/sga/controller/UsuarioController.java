package com.sga.controller;

import com.sga.model.Usuario;
import com.sga.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Usuarios")
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
