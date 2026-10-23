package pe.edu.cibertec.quirosaludventasservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.quirosaludventasservice.model.Tratamiento;
import pe.edu.cibertec.quirosaludventasservice.service.TratamientoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tratamientos")
@RequiredArgsConstructor
public class TratamientoController {

    private final TratamientoService tratamientoService;

    @GetMapping({"", "/listado"})
    public ResponseEntity<List<Tratamiento>> listarTratamientos() {
        return ResponseEntity.ok(tratamientoService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerTratamiento(@PathVariable("id") Integer id) {
        Tratamiento t = tratamientoService.obtenerPorId(id);
        if (t == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Tratamiento no encontrado"));
        }
        return ResponseEntity.ok(t);
    }

    @PostMapping({"", "/registro"})
    public ResponseEntity<Tratamiento> registrarTratamiento(@RequestBody Tratamiento tratamiento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tratamientoService.guardar(tratamiento));
    }

    @PutMapping({"/actualizar/{id}", "/{id}"})
    public ResponseEntity<?> actualizarTratamiento(@PathVariable("id") Integer id, @RequestBody Tratamiento datos) {
        Tratamiento existente = tratamientoService.obtenerPorId(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Tratamiento no encontrado"));
        }
        datos.setIdTratamiento(id);
        return ResponseEntity.ok(tratamientoService.guardar(datos));
    }

    @DeleteMapping({"/eliminar/{id}", "/{id}"})
    public ResponseEntity<?> eliminarTratamiento(@PathVariable("id") Integer id) {
        tratamientoService.eliminarLogico(id);
        return ResponseEntity.ok(Map.of("mensaje", "Tratamiento eliminado correctamente"));
    }
}
