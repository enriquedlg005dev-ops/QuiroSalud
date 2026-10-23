package pe.edu.cibertec.quirosaludventasservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.quirosaludventasservice.dto.IReporteDistrito;
import pe.edu.cibertec.quirosaludventasservice.dto.IReporteHistorial;
import pe.edu.cibertec.quirosaludventasservice.dto.IReporteVenta;
import pe.edu.cibertec.quirosaludventasservice.repository.HojaPagoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final HojaPagoRepository hojaPagoRepository;

    public List<IReporteHistorial> historialVentas() {
        return hojaPagoRepository.historialSeguro();
    }

    public List<IReporteVenta> tratamientosMasVendidos() {
        return hojaPagoRepository.reporteTratamientosMasVendidos();
    }

    public List<IReporteDistrito> estadisticaDistritos() {
        return hojaPagoRepository.reportePacientesPorDistrito();
    }
}
