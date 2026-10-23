package pe.edu.cibertec.quirosaludventasservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.cibertec.quirosaludventasservice.dto.CitaLoteDTO;

import java.util.List;

@FeignClient(name = "quirosalud-citas-service")
public interface CitaClient {

    @PostMapping("/api/v1/citas/generar-lote")
    List<CitaLoteDTO> generarLoteSesiones(@RequestBody List<CitaLoteDTO> nuevasSesiones);
}
