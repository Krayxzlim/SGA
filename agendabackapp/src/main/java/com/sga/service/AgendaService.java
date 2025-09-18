package com.sga.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sga.model.Agenda;
import com.sga.repository.AgendaRepository;
import com.sga.repository.TallerRepository;
import com.sga.repository.UsuarioRepository;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final TallerRepository tallerRepository;

    public AgendaService(AgendaRepository agendaRepository, TallerRepository tallerRepository, UsuarioRepository usuarioRepository) {
        this.agendaRepository = agendaRepository;
        this.tallerRepository = tallerRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Agenda createAgenda(Agenda agenda) {
        tallerRepository.findById(agenda.getTaller().getId())
            .orElseThrow(() -> new RuntimeException("Taller no encontrado"));

        return agendaRepository.save(agenda);
    }

    // Listar agenda
    public List<Agenda> getAllAgendas() {
        return agendaRepository.findAll();
    }

    // Actualizar agendamiento
    public Agenda updateAgenda(Long id, Agenda updated) {
        return agendaRepository.findById(id).map(s -> {
            s.setFecha(updated.getFecha());
            s.setHora(updated.getHora());
            s.setTaller(updated.getTaller());
            s.setResponsable(updated.getResponsable());
            return agendaRepository.save(s);
        }).orElseThrow(() -> new RuntimeException("Agendamiento no encontrado"));
    }

    public void deleteAgenda(Long id) {
        Agenda s = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamiento no encontrado"));
        agendaRepository.deleteById(id);
    }
}
