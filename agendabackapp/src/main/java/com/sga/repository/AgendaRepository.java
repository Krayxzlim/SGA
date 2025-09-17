package com.sga.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sga.model.Agenda;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {}
