package pe.edu.cibertec.quirosaludventasservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "tbl_hoja_pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HojaPago {

    @Id
    @Column(name = "nro_hp", length = 10)
    private String nroHp;

    @Column(name = "paciente_id", nullable = false)
    private Integer pacienteId;

    @Column(name = "usuario_id", nullable = false)
    private Integer usuarioId;

    @Column(name = "id_tratamiento", nullable = false)
    private Integer idTratamiento;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion = new Date();

    @Column(name = "total_sesiones", nullable = false)
    private Integer totalSesiones;

    @Column(name = "costo_total", nullable = false)
    private Double costoTotal;

    @Column(length = 10)
    private String estado = "ACTIVO";

    @Transient
    private String dniPaciente;
}
