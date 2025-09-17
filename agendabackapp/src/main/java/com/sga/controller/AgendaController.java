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

import com.sga.model.Agenda;
import com.sga.model.Usuario;
import com.sga.service.AgendaService;
import com.sga.service.UsuarioService;

@RestController
@RequestMapping("/api/agendas")
public class AgendaController {

    private final AgendaService agendaService;
    private final UsuarioService usuarioService;

    public AgendaController(AgendaService agendaService, UsuarioService usuarioService) {
        this.agendaService = agendaService;
        this.usuarioService = usuarioService;
    }

    // Listar todos
    @GetMapping
    public List<Agenda> list() {
        return agendaService.getAllAgendas(); // ⚡ usar instancia, no la clase
    }

    // Crear agendamiento
    @PostMapping
    public Agenda create(@RequestBody Agenda agenda, Principal principal) {
        Usuario currentUsuario = usuarioService.findByNombre(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return agendaService.createAgenda(agenda, currentUsuario);
    }

    // Actualizar agendamiento
    @PutMapping("/{id}")
    public Agenda update(@PathVariable Long id, @RequestBody Agenda agenda, Principal principal) {
        Usuario currentUsuario = usuarioService.findByNombre(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return agendaService.updateAgenda(id, agenda, currentUsuario);
    }

    // Eliminar agendamiento
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Principal principal) {
        Usuario currentUsuario = usuarioService.findByNombre(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        agendaService.deleteAgenda(id, currentUsuario);
    }
}
