package com.sga.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sga.model.AsignacionTallerista;

public interface AsignacionTalleristaRepository extends JpaRepository<AsignacionTallerista, Long> {
    List<AsignacionTallerista> findByAgendaId(Long agendaId);
    void deleteByAgendaId(Long agendaId);
}
