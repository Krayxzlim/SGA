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

import com.sga.model.Agenda;
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

    @PostMapping
    public Agenda create(@RequestBody Agenda agenda) {
        return agendaService.createAgenda(agenda);
    }

    @PutMapping("/{id}")
    public Agenda update(@PathVariable Long id, @RequestBody Agenda agenda) {
        return agendaService.updateAgenda(id, agenda);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        agendaService.deleteAgenda(id);
    }

}
