package pe.edu.cibertec.quirosaludpacientesservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pe.edu.cibertec.quirosaludpacientesservice.model.Paciente;
import pe.edu.cibertec.quirosaludpacientesservice.repository.PacienteRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PacienteServiceTest {

    private PacienteRepository pacienteRepository;
    private PacienteService pacienteService;

    @BeforeEach
    void setUp() {
        pacienteRepository = mock(PacienteRepository.class);
        pacienteService = new PacienteService(pacienteRepository);
    }

    @Test
    void testListarTodos() {
        Paciente p1 = new Paciente();
        p1.setDni("12345678");
        when(pacienteRepository.findAll()).thenReturn(List.of(p1));

        List<Paciente> resultado = pacienteService.listarTodos();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("12345678", resultado.get(0).getDni());
        verify(pacienteRepository, times(1)).findAll();
    }

    @Test
    void testGuardarPaciente() {
        Paciente p = new Paciente();
        p.setDni("87654321");
        when(pacienteRepository.save(p)).thenReturn(p);

        Paciente guardado = pacienteService.guardar(p);
        assertNotNull(guardado);
        assertEquals("87654321", guardado.getDni());
        verify(pacienteRepository, times(1)).save(p);
    }

    @Test
    void testObtenerPorDni() {
        Paciente p = new Paciente();
        p.setDni("77889900");
        when(pacienteRepository.findByDni("77889900")).thenReturn(p);

        Paciente encontrado = pacienteService.obtenerPorDni("77889900");
        assertNotNull(encontrado);
        assertEquals("77889900", encontrado.getDni());
    }

    @Test
    void testEliminarPorId() {
        doNothing().when(pacienteRepository).deleteById(1);
        pacienteService.eliminar(1);
        verify(pacienteRepository, times(1)).deleteById(1);
    }
}
