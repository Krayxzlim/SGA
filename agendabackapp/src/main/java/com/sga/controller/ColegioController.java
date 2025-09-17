package com.sga.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sga.model.Colegio;
import com.sga.repository.ColegioRepository;

@RestController
@RequestMapping("/api/colegios")
public class ColegioController {

    @Autowired ColegioRepository repo;

    @GetMapping
    public List<Colegio> listar() { return repo.findAll(); }

    @PostMapping
    @PreAuthorize("hasAnyRole('TALLERISTA','SUPERVISOR','ADMIN')")
    public Colegio crear(@RequestBody Colegio c) {
        return repo.save(c);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TALLERISTA','SUPERVISOR','ADMIN')")
    public ResponseEntity<Colegio> editar(@PathVariable Long id, @RequestBody Colegio input) {
        return repo.findById(id).map(colegio -> {
            colegio.setNombre(input.getNombre());
            colegio.setDireccion(input.getDireccion());
            colegio.setContacto(input.getContacto());
            repo.save(colegio);
            return ResponseEntity.ok(colegio);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TALLERISTA','SUPERVISOR','ADMIN')")
    public ResponseEntity<?> borrar(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
