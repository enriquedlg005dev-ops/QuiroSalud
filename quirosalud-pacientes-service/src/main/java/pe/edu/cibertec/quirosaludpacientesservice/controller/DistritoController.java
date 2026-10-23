package pe.edu.cibertec.quirosaludpacientesservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.quirosaludpacientesservice.model.Distrito;
import pe.edu.cibertec.quirosaludpacientesservice.repository.DistritoRepository;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/distritos")
public class DistritoController {

    private final DistritoRepository distritoRepository;

    @GetMapping({"", "/listado"})
    public ResponseEntity<List<Distrito>> listarDistritos() {
        return ResponseEntity.ok(distritoRepository.findAll());
    }
}
