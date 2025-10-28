package com.sga.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sga.exception.HorarioSolapadoException;
import com.sga.model.Agenda;
import com.sga.model.Colegio;
import com.sga.model.Taller;
import com.sga.repository.AgendaRepository;
import com.sga.repository.AsignacionTalleristaRepository;
import com.sga.repository.ColegioRepository;
import com.sga.repository.TallerRepository;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final TallerRepository tallerRepository;
    private final ColegioRepository colegioRepository;
    private final AsignacionTalleristaRepository asignacionTalleristaRepository;

    public AgendaService(AgendaRepository agendaRepository,
                         TallerRepository tallerRepository,
                         ColegioRepository colegioRepository,
                         AsignacionTalleristaRepository asignacionTalleristaRepository) {
        this.agendaRepository = agendaRepository;
        this.tallerRepository = tallerRepository;
        this.colegioRepository = colegioRepository;
        this.asignacionTalleristaRepository = asignacionTalleristaRepository;
    }

    public Agenda createAgenda(Agenda agenda) {
        if (agenda.getColegio() == null) {
            throw new RuntimeException("Colegio no encontrado");
        }

        Colegio colegio = colegioRepository.findById(agenda.getColegio().getId())
                .orElseThrow(() -> new RuntimeException("Colegio no encontrado"));
        agenda.setColegio(colegio);

        Taller taller = tallerRepository.findById(agenda.getTaller().getId())
                .orElseThrow(() -> new RuntimeException("Taller no encontrado"));
        agenda.setTaller(taller);

        validarSolapamiento(agenda);

        return agendaRepository.save(agenda);
    }

    public List<Agenda> getAllAgendas() {
        return agendaRepository.findAll();
    }

    public Agenda updateAgenda(Long id, Agenda updated) {
        return agendaRepository.findById(id).map(a -> {
            a.setFecha(updated.getFecha());
            a.setHora(updated.getHora());
            a.setTaller(updated.getTaller());
            a.setColegio(updated.getColegio());

            validarSolapamiento(a);

            return agendaRepository.save(a);
        }).orElseThrow(() -> new RuntimeException("Agendamiento no encontrado"));
    }

    @Transactional
    public void deleteAgenda(Long id) {
        // Primero borrar las asignaciones relacionadas
        asignacionTalleristaRepository.deleteByAgendaId(id);

        // Luego eliminar la agenda
        agendaRepository.deleteById(id);
    }

    // ================= VALIDACIÓN DE SOLAPAMIENTO =================
    private void validarSolapamiento(Agenda agendaNueva) {
        LocalTime horaNueva = agendaNueva.getHora();
        LocalTime finNueva = horaNueva.plusHours(1);

        List<Agenda> agendasExistentes = agendaRepository.findAll();

        for (Agenda otraAgenda : agendasExistentes) {
            // Ignorar la misma agenda si es actualización
            if (agendaNueva.getId() != null && agendaNueva.getId().equals(otraAgenda.getId())) {
                continue;
            }

            // Comparar agendas del mismo día
            if (otraAgenda.getFecha().equals(agendaNueva.getFecha())) {
                LocalTime horaExistente = otraAgenda.getHora();
                LocalTime finExistente = horaExistente.plusHours(1);

                boolean solapan = !horaNueva.isAfter(finExistente) && !finNueva.isBefore(horaExistente);
                if (solapan) {
                    throw new HorarioSolapadoException(
                        "El horario " + horaNueva + " - " + finNueva +
                        " se solapa con el taller '" + otraAgenda.getTaller().getNombre() + 
                        "' en el colegio '" + otraAgenda.getColegio().getNombre() + 
                        "' (Horario existente: " + horaExistente + " - " + finExistente + ")"
                    );
                }
            }
        }
    }
}
