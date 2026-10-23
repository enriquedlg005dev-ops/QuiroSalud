package pe.edu.cibertec.quirosaludcitasservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.quirosaludcitasservice.client.PacienteClient;
import pe.edu.cibertec.quirosaludcitasservice.dto.HistorialClinicoDTO;
import pe.edu.cibertec.quirosaludcitasservice.dto.PacienteDTO;
import pe.edu.cibertec.quirosaludcitasservice.model.Cita;
import pe.edu.cibertec.quirosaludcitasservice.model.Consulta;
import pe.edu.cibertec.quirosaludcitasservice.repository.CitaRepository;
import pe.edu.cibertec.quirosaludcitasservice.repository.ConsultaRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaRepository citaRepository;
    private final ConsultaRepository consultaRepository;
    private final PacienteClient pacienteClient;

    @GetMapping("/historial/{dni}")
    public ResponseEntity<List<Cita>> buscarPorDni(@PathVariable("dni") String dni) {
        return ResponseEntity.ok(citaRepository.findByDniPacienteOrderByNumeroSesionAsc(dni));
    }

    @GetMapping("/expediente/{dni}")
    public ResponseEntity<?> obtenerExpedienteCompleto(@PathVariable("dni") String dni) {
        PacienteDTO paciente = null;
        try {
            paciente = pacienteClient.obtenerPacientePorDni(dni);
        } catch (Exception ignored) {
        }

        List<Consulta> consultas = consultaRepository.findByDniPacienteOrderByIdConsultaDesc(dni);
        List<Cita> sesiones = citaRepository.findByDniPacienteOrderByNumeroSesionAsc(dni);

        HistorialClinicoDTO expediente = new HistorialClinicoDTO(paciente, consultas, sesiones);
        return ResponseEntity.ok(expediente);
    }

    @PutMapping("/asistencia/{idCita}")
    public ResponseEntity<?> marcarAsistencia(@PathVariable("idCita") Integer idCita, @RequestBody Cita datosActualizados) {
        Cita citaBD = citaRepository.findById(idCita).orElse(null);

        if (citaBD == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Error: Sesión no encontrada"));
        }

        citaBD.setAsistio(true);
        citaBD.setNotasAjuste(datosActualizados.getNotasAjuste());
        if (datosActualizados.getPagado() != null) {
            citaBD.setPagado(datosActualizados.getPagado());
        }

        return ResponseEntity.ok(citaRepository.save(citaBD));
    }

    @PostMapping("/generar-lote")
    public ResponseEntity<List<Cita>> generarLoteSesiones(@RequestBody List<Cita> nuevasSesiones) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaRepository.saveAll(nuevasSesiones));
    }
}
