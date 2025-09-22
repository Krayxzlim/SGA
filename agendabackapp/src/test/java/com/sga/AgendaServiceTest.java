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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.sga.model.Agenda;
import com.sga.model.Taller;
import com.sga.model.Tallerista;
import com.sga.repository.AgendaRepository;
import com.sga.repository.TallerRepository;
import com.sga.repository.UsuarioRepository;
import com.sga.service.AgendaService;

class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private TallerRepository tallerRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AgendaService agendaService;

    private Tallerista tallerista;
    private Taller taller;
    private Agenda agenda;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Creamos datos de prueba
        tallerista = new Tallerista();
        tallerista.setId(1L);
        tallerista.setNombre("Juan");
        tallerista.setPassword("123");

        taller = new Taller();
        taller.setId(1L);
        taller.setNombre("Taller de Arte");

        agenda = new Agenda();
        agenda.setId(1L);
        agenda.setTaller(taller);
        agenda.setFecha(LocalDate.now());
        agenda.setHora(LocalTime.of(10,0));
    }

    @Test
    void createAgenda_asTallerista_setsResponsableAndSaves() {
        // Mockeamos repositorios
        when(usuarioRepository.findById(tallerista.getId())).thenReturn(Optional.of(tallerista));
        when(tallerRepository.findById(taller.getId())).thenReturn(Optional.of(taller));
        when(agendaRepository.save(any(Agenda.class))).thenAnswer(i -> i.getArguments()[0]);

        Agenda result = agendaService.createAgenda(agenda);

        assertNotNull(result);
        assertEquals(tallerista, result.getResponsable());
        verify(agendaRepository, times(1)).save(result);
    }

    @Test
    void createAgenda_userNotFound_throwsException() {
        when(usuarioRepository.findById(tallerista.getId())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> agendaService.createAgenda(agenda));
        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void createAgenda_tallerNotFound_throwsException() {
        when(usuarioRepository.findById(tallerista.getId())).thenReturn(Optional.of(tallerista));
        when(tallerRepository.findById(taller.getId())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> agendaService.createAgenda(agenda));
        assertEquals("Taller no encontrado", ex.getMessage());
    }
}
