package pe.edu.cibertec.quirosaludcitasservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_cita")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Integer idCita;

    @Column(name = "dni_paciente", length = 8, nullable = false)
    private String dniPaciente;

    @Column(name = "nro_hp", length = 10)
    private String nroHp;

    @Column(name = "numero_sesion")
    private Integer numeroSesion;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    private Time hora;

    @Column(name = "tipo_sesion", length = 1, nullable = false)
    private String tipoSesion = "T";

    @Column(name = "notas_ajuste", length = 255)
    private String notasAjuste;

    @Column(columnDefinition = "boolean default false")
    private Boolean asistio = false;

    @Column(columnDefinition = "boolean default false")
    private Boolean pagado = false;
}
