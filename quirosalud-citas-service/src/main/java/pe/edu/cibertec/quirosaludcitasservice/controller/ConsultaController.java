package pe.edu.cibertec.quirosaludcitasservice.controller;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.quirosaludcitasservice.client.PacienteClient;
import pe.edu.cibertec.quirosaludcitasservice.dto.PacienteDTO;
import pe.edu.cibertec.quirosaludcitasservice.model.Consulta;
import pe.edu.cibertec.quirosaludcitasservice.repository.ConsultaRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaRepository consultaRepository;
    private final PacienteClient pacienteClient;

    @GetMapping("/pendientes")
    public ResponseEntity<List<Consulta>> listarPendientes() {
        List<Consulta> pendientes = consultaRepository.findByEstado("PENDIENTE");
        // Enriquecemos con Feign Client si tiene dniPaciente guardado
        for (Consulta c : pendientes) {
            if (c.getDniPaciente() != null) {
                try {
                    c.setPaciente(pacienteClient.obtenerPacientePorDni(c.getDniPaciente()));
                } catch (Exception ignored) {}
            }
        }
        return ResponseEntity.ok(pendientes);
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Consulta consulta) {
        try {
            // Feign Client valida el DNI en quirosalud-pacientes-service (8082)
            PacienteDTO paciente = pacienteClient.obtenerPacientePorDni(consulta.getDniPaciente());
            consulta.setPacienteId(paciente.getIdPersona());
            consulta.setEstado("PENDIENTE");

            Consulta guardada = consultaRepository.save(consulta);
            guardada.setPaciente(paciente);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);

        } catch (FeignException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "El DNI no existe en la base de datos"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("mensaje", "Error al validar paciente: " + e.getMessage()));
        }
    }

    @PutMapping("/atender/{id}")
    public ResponseEntity<?> atenderConsulta(@PathVariable("id") Integer id, @RequestBody Consulta datosDoctor) {
        Consulta consultaBD = consultaRepository.findById(id).orElse(null);
        if (consultaBD == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Consulta no encontrada"));
        }
        consultaBD.setDiagnostico(datosDoctor.getDiagnostico());
        consultaBD.setTratamientoSugerido(datosDoctor.getTratamientoSugerido());
        consultaBD.setSesionesSugeridas(datosDoctor.getSesionesSugeridas() != null ? datosDoctor.getSesionesSugeridas() : 0);
        consultaBD.setEstado("ATENDIDO");

        return ResponseEntity.ok(consultaRepository.save(consultaBD));
    }
}