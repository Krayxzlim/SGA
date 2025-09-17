package com.sga.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sga.model.Colegio;
import com.sga.model.Taller;
import com.sga.model.Usuario;
import com.sga.repository.ColegioRepository;
import com.sga.repository.TallerRepository;

@Service
public class TallerService {

    private final TallerRepository tallerRepository;
    private final ColegioRepository colegioRepository;

    public TallerService(TallerRepository tallerRepository, ColegioRepository colegioRepository) {
        this.tallerRepository = tallerRepository;
        this.colegioRepository = colegioRepository;
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
            w.setTematica(updated.getTematica());
            w.setDescripcion(updated.getDescripcion());

            if (updated.getColegio() != null) {
                Colegio colegio = colegioRepository.findById(updated.getColegio().getId())
                        .orElseThrow(() -> new RuntimeException("Colegio no encontrado"));
                w.setColegio(colegio);
            }

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
