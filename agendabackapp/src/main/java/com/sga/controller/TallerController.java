package com.sga.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sga.model.Taller;
import com.sga.model.Usuario;
import com.sga.service.TallerService;
import com.sga.service.UsuarioService;

@RestController
@RequestMapping("/api/talleres")
public class TallerController {

    private final TallerService tallerService;
    private final UsuarioService usuarioService;

    public TallerController(TallerService tallerService, UsuarioService usuarioService) {
        this.tallerService = tallerService;
        this.usuarioService = usuarioService;
    }

    // Listar todos los talleres
    @GetMapping
    public List<Taller> list() {
        return tallerService.getAllTallers();
    }

    // Crear taller
    @PostMapping
    public Taller create(@RequestBody Taller taller, Principal principal) {
        Usuario currentUsuario = usuarioService.findByNombre(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Ejemplo de control de permisos por tipo de usuario
        if (!(currentUsuario instanceof com.sga.model.Tallerista || 
              currentUsuario instanceof com.sga.model.Supervisor || 
              currentUsuario instanceof com.sga.model.Administrador)) {
            throw new RuntimeException("No tienes permisos para crear talleres");
        }

        return tallerService.createTaller(taller, currentUsuario);
    }

    // Actualizar taller
    @PutMapping("/{id}")
    public Taller update(@PathVariable Long id, @RequestBody Taller taller, Principal principal) {
        Usuario currentUsuario = usuarioService.findByNombre(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!(currentUsuario instanceof com.sga.model.Tallerista || 
              currentUsuario instanceof com.sga.model.Supervisor || 
              currentUsuario instanceof com.sga.model.Administrador)) {
            throw new RuntimeException("No tienes permisos para actualizar talleres");
        }

        return tallerService.updateTaller(id, taller, currentUsuario);
    }

    // Eliminar taller
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Principal principal) {
        Usuario currentUsuario = usuarioService.findByNombre(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!(currentUsuario instanceof com.sga.model.Tallerista || 
              currentUsuario instanceof com.sga.model.Supervisor || 
              currentUsuario instanceof com.sga.model.Administrador)) {
            throw new RuntimeException("No tienes permisos para eliminar talleres");
        }

        tallerService.deleteTaller(id, currentUsuario);
    }
}
