package pe.edu.cibertec.quirosaludcitasservice.model;

import jakarta.persistence.*;
import lombok.*;
import pe.edu.cibertec.quirosaludcitasservice.dto.PacienteDTO;
import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_consulta")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta")
    private Integer idConsulta;

    @Column(name = "paciente_id", nullable = false)
    private Integer pacienteId;

    @Column(name = "dni_paciente", length = 8)
    private String dniPaciente;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    private Time hora;

    @Column(length = 255)
    private String motivo;

    @Column(columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "tratamiento_sugerido", length = 100)
    private String tratamientoSugerido;

    @Column(name = "sesiones_sugeridas")
    private Integer sesionesSugeridas = 0;

    @Column(length = 20)
    private String estado = "PENDIENTE";

    @Transient
    private PacienteDTO paciente;
}
