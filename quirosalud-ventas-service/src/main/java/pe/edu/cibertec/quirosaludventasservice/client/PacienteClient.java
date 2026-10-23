package pe.edu.cibertec.quirosaludventasservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.cibertec.quirosaludventasservice.dto.PacienteDTO;

@FeignClient(name = "quirosalud-pacientes-service")
public interface PacienteClient {

    @GetMapping("/api/v1/pacientes/dni/{dni}")
    PacienteDTO obtenerPorDni(@PathVariable("dni") String dni);
}
