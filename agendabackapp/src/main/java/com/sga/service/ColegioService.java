package com.sga.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sga.model.Colegio;
import com.sga.repository.ColegioRepository;

@Service
public class ColegioService {
    private final ColegioRepository colegioRepository;

    public ColegioService(ColegioRepository colegioRepository) {
        this.colegioRepository = colegioRepository;
    }

    public Colegio createSchool(Colegio school) {
        return colegioRepository.save(school);
    }

    public List<Colegio> getAllSchools() {
        return colegioRepository.findAll();
    }

    public Colegio updateSchool(Long id, Colegio updated) {
        return colegioRepository.findById(id).map(s -> {
            s.setNombre(updated.getNombre());
            s.setDireccion(updated.getDireccion());
            s.setContacto(updated.getContacto());
            return colegioRepository.save(s);
        }).orElseThrow(() -> new RuntimeException("Colegio no encontrado"));
    }

    public void deleteSchool(Long id) {
        colegioRepository.deleteById(id);
    }
}
