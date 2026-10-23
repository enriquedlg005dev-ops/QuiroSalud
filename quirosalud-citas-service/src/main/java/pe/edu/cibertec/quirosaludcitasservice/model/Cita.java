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

    // DNI directo para no depender de un triple JOIN con HojaPago
    @Column(name = "dni_paciente", length = 8, nullable = false)
    private String dniPaciente;

    // Referencia lógica al comprobante/Hoja de Pago (ej. "HP001")
    @Column(name = "nro_hp", length = 10)
    private String nroHp;

    // Ej: Sesión 1, Sesión 2...
    @Column(name = "numero_sesion")
    private Integer numeroSesion;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    private Time hora;

    @Column(name = "tipo_sesion", length = 1, nullable = false)
    private String tipoSesion = "T"; // T = Tratamiento

    @Column(name = "notas_ajuste", length = 255)
    private String notasAjuste;

    @Column(columnDefinition = "boolean default false")
    private Boolean asistio = false;

    // Nuevo: Permite saber si esta sesión ya fue pagada (útil cuando pagan por sesión y no por adelantado)
    @Column(columnDefinition = "boolean default false")
    private Boolean pagado = false;
}