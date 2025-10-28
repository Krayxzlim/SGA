package com.sga.service;

import java.time.LocalTime;
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


        boolean yaAsignado = repo.findByAgendaId(agendaId).stream()
                .anyMatch(a -> a.getTallerista().getId().equals(talleristaId));
        if (yaAsignado)
            throw new RuntimeException("El tallerista ya está asignado a esta agenda");

        List<AsignacionTallerista> asignacionesTallerista = repo.findAll().stream()
                .filter(a -> a.getTallerista().getId().equals(talleristaId))
                .collect(Collectors.toList());

        for (AsignacionTallerista a : asignacionesTallerista) {
            Agenda otraAgenda = a.getAgenda();

            if (otraAgenda == null) continue;

            // Comparar fecha
            if (otraAgenda.getFecha().equals(agenda.getFecha())) {
                LocalTime horaNueva = agenda.getHora();
                LocalTime horaExistente = otraAgenda.getHora();
                // Comparar solapamiento de 1 hora
                LocalTime finNueva = horaNueva.plusHours(1);
                LocalTime finExistente = horaExistente.plusHours(1);

                boolean seSolapan = !(finNueva.isBefore(horaExistente) || finExistente.isBefore(horaNueva));

                if (seSolapan) {
                    throw new RuntimeException("El tallerista ya está asignado a otro taller en este horario.");
                }
            }
        }

        AsignacionTallerista asignacion = new AsignacionTallerista();
        asignacion.setAgenda(agenda);
        asignacion.setTallerista(usuario);
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
