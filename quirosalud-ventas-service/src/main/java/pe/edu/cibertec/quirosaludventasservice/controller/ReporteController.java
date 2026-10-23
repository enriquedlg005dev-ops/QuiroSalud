package pe.edu.cibertec.quirosaludventasservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.quirosaludventasservice.service.ReporteService;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/distritos")
    public ResponseEntity<?> estadisticaDistritos() {
        return ResponseEntity.ok(reporteService.estadisticaDistritos());
    }

    @GetMapping("/ventas")
    public ResponseEntity<?> historialVentas() {
        return ResponseEntity.ok(reporteService.historialVentas());
    }

    @GetMapping("/tratamientos-vendidos")
    public ResponseEntity<?> tratamientosMasVendidos() {
        return ResponseEntity.ok(reporteService.tratamientosMasVendidos());
    }
}
