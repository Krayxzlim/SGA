package com.sga.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sga.model.Agenda;
import com.sga.model.AsignacionTallerista;
import com.sga.model.Usuario;
import com.sga.repository.AgendaRepository;
import com.sga.repository.AsignacionTalleristaRepository;
import com.sga.repository.UsuarioRepository;

@Service
public class AsignacionTalleristaService {

    private final AsignacionTalleristaRepository repo;
    private final AgendaRepository agendaRepo;
    private final UsuarioRepository usuarioRepo;

    public AsignacionTalleristaService(
            AsignacionTalleristaRepository repo,
            AgendaRepository agendaRepo,
            UsuarioRepository usuarioRepo
    ) {
        this.repo = repo;
        this.agendaRepo = agendaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    public List<AsignacionTallerista> listarPorAgenda(Long agendaId) {
        return repo.findByAgendaId(agendaId);
    }

    public List<Usuario> listarTalleristasPorAgenda(Long agendaId) {
        return listarPorAgenda(agendaId).stream()
                .map(AsignacionTallerista::getTallerista)
                .collect(Collectors.toList());
    }

    public AsignacionTallerista asignarTallerista(Long agendaId, Long talleristaId) {
        Agenda agenda = agendaRepo.findById(agendaId)
                .orElseThrow(() -> new RuntimeException("Agenda no encontrada"));

        Usuario usuario = usuarioRepo.findById(talleristaId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Evitar duplicados
        boolean yaAsignado = repo.findByAgendaId(agendaId).stream()
                .anyMatch(a -> a.getTallerista().getId().equals(talleristaId));
        if (yaAsignado) return null;

        AsignacionTallerista asignacion = new AsignacionTallerista();
        asignacion.setAgenda(agenda);
        asignacion.setTallerista(usuario); // Usuario con rol "ROL_TALLERISTA"
        return repo.save(asignacion);
    }

    public void eliminarAsignacion(Long id) {
        repo.deleteById(id);
    }

    public void eliminarAsignacionesPorAgenda(Long agendaId) {
        List<AsignacionTallerista> asignaciones = listarPorAgenda(agendaId);
        repo.deleteAll(asignaciones);
    }
}
