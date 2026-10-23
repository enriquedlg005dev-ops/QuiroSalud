package pe.edu.cibertec.quirosaludventasservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.quirosaludventasservice.client.CitaClient;
import pe.edu.cibertec.quirosaludventasservice.client.PacienteClient;
import pe.edu.cibertec.quirosaludventasservice.dto.CitaLoteDTO;
import pe.edu.cibertec.quirosaludventasservice.dto.PacienteDTO;
import pe.edu.cibertec.quirosaludventasservice.dto.VentaRequest;
import pe.edu.cibertec.quirosaludventasservice.model.HojaPago;
import pe.edu.cibertec.quirosaludventasservice.repository.HojaPagoRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VentaService {

    private final HojaPagoRepository hojaPagoRepository;
    private final CitaClient citaClient;
    private final PacienteClient pacienteClient;

    @Transactional
    public String registrarVentaGenerandoCitas(VentaRequest request) {

        Integer pacienteId = null;
        try {
            PacienteDTO paciente = pacienteClient.obtenerPorDni(request.getDniPaciente());
            if (paciente != null && paciente.getIdPersona() != null) {
                pacienteId = paciente.getIdPersona();
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener el paciente: {}", e.getMessage());
        }

        if (pacienteId == null) {
            try {
                pacienteId = Integer.parseInt(request.getDniPaciente());
            } catch (NumberFormatException ignored) {
                pacienteId = 1;
            }
        }

        Integer usuarioId = request.getIdUsuario() != null && request.getIdUsuario() > 0
                ? request.getIdUsuario()
                : 1;

        String ultimoId = hojaPagoRepository.obtenerUltimoId();
        String nuevoId = "HP001";
        if (ultimoId != null && ultimoId.length() >= 3) {
            try {
                int correlativo = Integer.parseInt(ultimoId.substring(2)) + 1;
                nuevoId = String.format("HP%03d", correlativo);
            } catch (Exception e) {
                nuevoId = "HP" + (hojaPagoRepository.count() + 1);
            }
        }

        HojaPago hoja = new HojaPago();
        hoja.setNroHp(nuevoId);
        hoja.setPacienteId(pacienteId);
        hoja.setUsuarioId(usuarioId);
        hoja.setIdTratamiento(request.getIdTratamiento());
        hoja.setFechaCreacion(new Date());
        hoja.setTotalSesiones(request.getTotalSesiones());
        hoja.setCostoTotal(request.getCostoTotal());
        hoja.setEstado("ACTIVO");

        hojaPagoRepository.save(hoja);

        List<CitaLoteDTO> citas = new ArrayList<>();
        for (int i = 1; i <= request.getTotalSesiones(); i++) {
            citas.add(CitaLoteDTO.builder()
                    .dniPaciente(request.getDniPaciente())
                    .nroHp(nuevoId)
                    .numeroSesion(i)
                    .tipoSesion("T")
                    .asistio(false)
                    .pagado(true)
                    .build());
        }

        try {
            citaClient.generarLoteSesiones(citas);
            log.info("Lote de {} citas generado exitosamente para la hoja {}", citas.size(), nuevoId);
        } catch (Exception e) {
            log.error("Error al comunicar con quirosalud-citas-service: {}", e.getMessage());
        }

        return nuevoId;
    }
}
