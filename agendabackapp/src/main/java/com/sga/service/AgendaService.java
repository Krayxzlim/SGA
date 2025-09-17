package com.sga.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sga.model.Agenda;
import com.sga.model.Tallerista;
import com.sga.model.Usuario;
import com.sga.repository.AgendaRepository;
import com.sga.repository.TallerRepository;
import com.sga.repository.UsuarioRepository;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final TallerRepository tallerRepository;
    private final UsuarioRepository usuarioRepository;

    public AgendaService(AgendaRepository agendaRepository, TallerRepository tallerRepository, UsuarioRepository usuarioRepository) {
        this.agendaRepository = agendaRepository;
        this.tallerRepository = tallerRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Agenda createAgenda(Agenda agenda, Usuario currentUser) {
        usuarioRepository.findById(currentUser.getId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        tallerRepository.findById(agenda.getTaller().getId())
            .orElseThrow(() -> new RuntimeException("Taller no encontrado"));

        if (currentUser instanceof Tallerista tallerista) {
            agenda.setResponsable(tallerista);
        }
        return agendaRepository.save(agenda);
    }

    // Listar agenda
    public List<Agenda> getAllAgendas() {
        return agendaRepository.findAll();
    }

    // Actualizar agendamiento
    public Agenda updateAgenda(Long id, Agenda updated, Usuario currentUser) {
        return agendaRepository.findById(id).map(s -> {
            if (currentUser instanceof Tallerista && !s.getResponsable().getId().equals(currentUser.getId())) {
                throw new RuntimeException("No puedes modificar talleres de otros");
            }
            s.setFecha(updated.getFecha());
            s.setHora(updated.getHora());
            s.setTaller(updated.getTaller());
            return agendaRepository.save(s);
        }).orElseThrow(() -> new RuntimeException("Agendamiento no encontrado"));
    }

    // Eliminar agendamiento
    public void deleteAgenda(Long id, Usuario currentUser) {
        Agenda s = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamiento no encontrado"));

        if (currentUser instanceof Tallerista && !s.getResponsable().getId().equals(currentUser.getId())) {
            throw new RuntimeException("No puedes eliminar talleres de otros");
        }
        agendaRepository.deleteById(id);
    }
}
