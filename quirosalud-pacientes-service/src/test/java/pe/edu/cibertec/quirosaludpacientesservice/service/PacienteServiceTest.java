package pe.edu.cibertec.quirosaludpacientesservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.cibertec.quirosaludpacientesservice.dto.*;
import pe.edu.cibertec.quirosaludpacientesservice.model.*;
import pe.edu.cibertec.quirosaludpacientesservice.repository.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PacienteServiceTest {
    private PacienteRepository pacientes;
    private HistoriaRepository historias;
    private PacienteService servicio;
    private final Authentication admin = auth("ROLE_ADMIN");
    private final Authentication asistente = auth("ROLE_ASISTENTE");

    @BeforeEach void configurar() {
        pacientes = mock(PacienteRepository.class);
        historias = mock(HistoriaRepository.class);
        servicio = new PacienteService(pacientes, historias);
    }

    private static Authentication auth(String rol) {
        return new UsernamePasswordAuthenticationToken("demo@local", null, List.of(new SimpleGrantedAuthority(rol)));
    }
    private PacienteRequest datos(HistoriaRequest historia) {
        return new PacienteRequest("12345678", "Ada", "Lovelace", LocalDate.of(1990,1,1),
                "F", "S", null, 0, "987654321", null, "Av. Ejemplo 123", "D01", historia);
    }

    @Test void registraSinHistoriaComoAsistente() {
        var respuesta = servicio.guardar(datos(null), asistente);
        assertEquals("12345678", respuesta.dni());
        verify(pacientes).save(any(Paciente.class));
        verify(historias, never()).save(any());
    }

    @Test void bloqueaEscrituraClinicaParaAsistente() {
        var historia = new HistoriaRequest("Dolor", 6, null, null, false, false, false, null);
        assertThrows(AccessDeniedException.class, () -> servicio.guardar(datos(historia), asistente));
        verify(pacientes, never()).save(any());
    }

    @Test void rechazaDniDuplicadoCon409() {
        when(pacientes.existsById("12345678")).thenReturn(true);
        var error = assertThrows(ResponseStatusException.class, () -> servicio.guardar(datos(null), admin));
        assertEquals(409, error.getStatusCode().value());
    }

    @Test void actualizaPacienteYNoModificaDni() {
        Paciente original = new Paciente(); original.setDni("12345678");
        when(pacientes.findById("12345678")).thenReturn(Optional.of(original));
        var actualizado = servicio.actualizar("12345678", datos(null), admin);
        assertEquals("Ada", actualizado.nombres());
        assertEquals("12345678", actualizado.dni());
        assertThrows(ResponseStatusException.class, () -> servicio.actualizar("87654321", datos(null), admin));
    }

    @Test void eliminaHistoriaAntesDelPaciente() {
        Paciente actual = new Paciente(); actual.setDni("12345678");
        when(pacientes.findById("12345678")).thenReturn(Optional.of(actual));
        servicio.eliminar("12345678");
        var orden = inOrder(historias, pacientes);
        orden.verify(historias).deleteById("12345678");
        orden.verify(pacientes).delete(actual);
    }
}
