package pe.edu.cibertec.quirosaludventasservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.quirosaludventasservice.dto.VentaRequest;
import pe.edu.cibertec.quirosaludventasservice.service.VentaService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @PostMapping({"", "/registro"})
    public ResponseEntity<?> registrarVenta(@Valid @RequestBody VentaRequest request) {
        String nroGenerado = ventaService.registrarVentaGenerandoCitas(request);
        String mensaje = "¡Venta exitosa! Se generó la hoja: " + nroGenerado + " con sus citas respectivas.";
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", mensaje,
                "nroHp", nroGenerado
        ));
    }
}
