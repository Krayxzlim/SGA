package com.sga.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sga.model.AsignacionTallerista;
import com.sga.service.AsignacionTalleristaService;

@RestController
@RequestMapping("/api/asignaciones")
public class AsignacionTalleristaController {

    private final AsignacionTalleristaService service;

    public AsignacionTalleristaController(AsignacionTalleristaService service) {
        this.service = service;
    }

    @GetMapping("/agenda/{agendaId}")
    public List<AsignacionTallerista> listarPorAgenda(@PathVariable Long agendaId) {
        return service.listarPorAgenda(agendaId);
    }

    @PostMapping
    public AsignacionTallerista asignar(@RequestParam Long agendaId, @RequestParam Long talleristaId) {
        return service.asignarTallerista(agendaId, talleristaId);
    }

    @DeleteMapping("/agenda/{agendaId}")
    public void eliminarPorAgenda(@PathVariable Long agendaId) {
        List<AsignacionTallerista> asignaciones = service.listarPorAgenda(agendaId);
        asignaciones.forEach(a -> service.eliminarAsignacion(a.getId()));
    }
}
