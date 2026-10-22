package pe.edu.cibertec.quirosaludpacientesservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.quirosaludpacientesservice.model.Paciente;
import pe.edu.cibertec.quirosaludpacientesservice.service.PacienteService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/pacientes")

public class PacienteController {

    private final PacienteService pacienteService;

    /*public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }*/


    // Endpoint para listar todos los pacientes (GET)
    @GetMapping
    public ResponseEntity<List<Paciente>> listarPacientes() {
        List<Paciente> pacientes = pacienteService.listarTodos();
        return new ResponseEntity<>(pacientes, HttpStatus.OK); // Retorna 200 OK
    }

    // Endpoint para registrar un nuevo paciente (POST)
    @PostMapping
    public ResponseEntity<Paciente> registrarPaciente(@RequestBody Paciente paciente) {
        Paciente nuevoPaciente = pacienteService.guardar(paciente);
        return new ResponseEntity<>(nuevoPaciente, HttpStatus.CREATED); // Retorna 201 Created
    }
}