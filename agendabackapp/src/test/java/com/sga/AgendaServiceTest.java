package com.sga;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.sga.model.Agenda;
import com.sga.model.Colegio;
import com.sga.model.Taller;
import com.sga.repository.AgendaRepository;
import com.sga.repository.ColegioRepository;
import com.sga.repository.TallerRepository;
import com.sga.service.AgendaService;

class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private TallerRepository tallerRepository;

    @Mock
    private ColegioRepository colegioRepository;

    @InjectMocks
    private AgendaService agendaService;

    private Colegio colegio;
    private Taller taller;
    private Agenda agenda;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Datos de prueba
        colegio = new Colegio();
        colegio.setId(1L);
        colegio.setNombre("Colegio San Martín");

        taller = new Taller();
        taller.setId(1L);
        taller.setNombre("Taller de Arte");

        agenda = new Agenda();
        agenda.setId(1L);
        agenda.setTaller(taller);
        agenda.setColegio(colegio);
        agenda.setFecha(LocalDate.now());
        agenda.setHora(LocalTime.of(10, 0));
    }

    @Test
    void createAgenda_setsColegioAndSaves() {
        when(colegioRepository.findById(colegio.getId())).thenReturn(Optional.of(colegio));
        when(tallerRepository.findById(taller.getId())).thenReturn(Optional.of(taller));
        when(agendaRepository.save(any(Agenda.class))).thenAnswer(invocation -> {
            Agenda saved = invocation.getArgument(0);
            saved.setId(99L);
            return saved;
        });

        Agenda result = agendaService.createAgenda(agenda);

        assertNotNull(result);
        assertEquals(99L, result.getId());
        assertEquals(colegio, result.getColegio());
        assertEquals(taller, result.getTaller());
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    void createAgenda_colegioNotFound_throwsException() {
        when(colegioRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> agendaService.createAgenda(agenda));

        assertEquals("Colegio no encontrado", exception.getMessage());
        verify(agendaRepository, never()).save(any());
    }

    @Test
    void createAgenda_tallerNotFound_throwsException() {
        when(colegioRepository.findById(anyLong())).thenReturn(Optional.of(colegio));
        when(tallerRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> agendaService.createAgenda(agenda));

        assertEquals("Taller no encontrado", exception.getMessage());
        verify(agendaRepository, never()).save(any());
    }
}
