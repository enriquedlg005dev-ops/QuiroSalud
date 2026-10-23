package pe.edu.cibertec.quirosaludpacientesservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.quirosaludpacientesservice.model.Paciente;
import pe.edu.cibertec.quirosaludpacientesservice.service.PacienteService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    @GetMapping({"", "/listado"})
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEDICO', 'ROLE_ASISTENTE')")
    public ResponseEntity<List<Paciente>> listarPacientes() {
        return ResponseEntity.ok(pacienteService.listarTodos());
    }

    @GetMapping({"/dni/{dni}", "/{dni}"})
    public ResponseEntity<Paciente> obtenerPacientePorDni(@PathVariable("dni") String dni) {
        Paciente paciente = pacienteService.obtenerPorDni(dni);
        if (paciente != null) {
            return ResponseEntity.ok(paciente);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping({"", "/registro"})
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEDICO', 'ROLE_ASISTENTE')")
    public ResponseEntity<Paciente> registrarPaciente(@RequestBody Paciente paciente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.guardar(paciente));
    }

    @PutMapping({"/actualizar/{id}", "/{id}"})
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEDICO', 'ROLE_ASISTENTE')")
    public ResponseEntity<?> actualizarPaciente(@PathVariable("id") Integer id, @RequestBody Paciente datos) {
        Paciente existente = pacienteService.obtenerPorId(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Paciente no encontrado"));
        }
        datos.setIdPersona(id);
        return ResponseEntity.ok(pacienteService.guardar(datos));
    }

    @DeleteMapping({"/eliminar/{id}", "/{id}"})
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEDICO', 'ROLE_ASISTENTE')")
    public ResponseEntity<?> eliminarPaciente(@PathVariable("id") Integer id) {
        try {
            pacienteService.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Paciente eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("mensaje", "Error al eliminar paciente: " + e.getMessage()));
        }
    }
}
