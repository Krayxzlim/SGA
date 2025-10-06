package com.sga.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sga.model.Taller;
import com.sga.model.Usuario;
import com.sga.repository.TallerRepository;

@Service
public class TallerService {

    private final TallerRepository tallerRepository;

    public TallerService(TallerRepository tallerRepository) {
        this.tallerRepository = tallerRepository;
    }

    // Crear taller con usuario para control de permisos
    public Taller createTaller(Taller taller, Usuario usuario) {
        return tallerRepository.save(taller);
    }

    // Listar todos los talleres
    public List<Taller> getAllTallers() {
        return tallerRepository.findAll();
    }

    // Actualizar taller con usuario para control de permisos
    public Taller updateTaller(Long id, Taller updated, Usuario usuario) {
        return tallerRepository.findById(id).map(w -> {
            w.setNombre(updated.getNombre());
            w.setDescripcion(updated.getDescripcion());

            return tallerRepository.save(w);
        }).orElseThrow(() -> new RuntimeException("Taller no encontrado"));
    }

    // Eliminar taller con usuario para control de permisos
    public void deleteTaller(Long id, Usuario usuario) {
        // Aquí también podrías validar permisos según el tipo de usuario
        if (!tallerRepository.existsById(id)) {
            throw new RuntimeException("Taller no encontrado");
        }
        tallerRepository.deleteById(id);
    }
}
