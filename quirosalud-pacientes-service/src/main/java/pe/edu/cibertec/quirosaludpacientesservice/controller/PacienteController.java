package pe.edu.cibertec.quirosaludpacientesservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.quirosaludpacientesservice.dto.*;
import pe.edu.cibertec.quirosaludpacientesservice.service.PacienteService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pacientes")
public class PacienteController {
    private final PacienteService pacienteService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ASISTENTE')")
    public List<PacienteResponse> listar() { return pacienteService.listarTodos(); }

    @GetMapping("/{dni}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ASISTENTE')")
    public PacienteResponse buscar(@PathVariable String dni) { return pacienteService.buscar(dni); }

    @GetMapping("/{dni}/historia")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO')")
    public HistoriaResponse historia(@PathVariable String dni) { return pacienteService.obtenerHistoria(dni); }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ASISTENTE')")
    public ResponseEntity<PacienteResponse> crear(@Valid @RequestBody PacienteRequest datos, Authentication usuario) {
        PacienteResponse creado = pacienteService.guardar(datos, usuario);
        return ResponseEntity.created(URI.create("/api/v1/pacientes/" + creado.dni())).body(creado);
    }

    @PutMapping("/{dni}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ASISTENTE')")
    public PacienteResponse editar(@PathVariable String dni, @Valid @RequestBody PacienteRequest datos,
                                   Authentication usuario) {
        return pacienteService.actualizar(dni, datos, usuario);
    }

    @DeleteMapping("/{dni}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String dni) { pacienteService.eliminar(dni); }
}
