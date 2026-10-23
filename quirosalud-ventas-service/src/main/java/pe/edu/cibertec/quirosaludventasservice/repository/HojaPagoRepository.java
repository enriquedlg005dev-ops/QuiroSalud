package pe.edu.cibertec.quirosaludventasservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.quirosaludventasservice.dto.IReporteDistrito;
import pe.edu.cibertec.quirosaludventasservice.dto.IReporteHistorial;
import pe.edu.cibertec.quirosaludventasservice.dto.IReporteVenta;
import pe.edu.cibertec.quirosaludventasservice.model.HojaPago;

import java.util.List;

@Repository
public interface HojaPagoRepository extends JpaRepository<HojaPago, String> {

    @Query("SELECT MAX(h.nroHp) FROM HojaPago h")
    String obtenerUltimoId();

    @Query(value = "SELECT t.nombre AS nombre, CAST(SUM(h.total_sesiones) AS SIGNED) AS cantidad, SUM(h.costo_total) AS totalDinero " +
            "FROM tbl_hoja_pago h " +
            "JOIN tbl_tratamiento t ON h.id_tratamiento = t.id_tratamiento " +
            "GROUP BY t.nombre", nativeQuery = true)
    List<IReporteVenta> reporteTratamientosMasVendidos();

    @Query(value = "SELECT h.nro_hp AS nroHp, CAST(h.fecha_creacion AS CHAR) AS fecha, COALESCE(p.dni, CAST(h.paciente_id AS CHAR)) AS dni, h.total_sesiones AS sesiones, h.costo_total AS total " +
            "FROM tbl_hoja_pago h " +
            "LEFT JOIN tbl_paciente p ON h.paciente_id = p.persona_id " +
            "ORDER BY h.nro_hp DESC", nativeQuery = true)
    List<IReporteHistorial> historialSeguro();

    @Query(value = "SELECT d.nombre_distrito AS nombreDistrito, COUNT(p.persona_id) AS cantidad " +
            "FROM tbl_distrito d " +
            "JOIN tbl_paciente p ON d.cod_distrito = p.cod_distrito " +
            "GROUP BY d.nombre_distrito", nativeQuery = true)
    List<IReporteDistrito> reportePacientesPorDistrito();
}
