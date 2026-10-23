package pe.edu.cibertec.quirosaludpacientesservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

@Entity
@Table(name = "tbl_paciente")
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "persona_id")
public class Paciente extends Persona {

    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    private String sexo;

    @Column(name = "estado_civil")
    private String estadoCivil;

    private String ocupacion;
    private String telefono;
    private String direccion;

    @Column(name = "motivo_consulta")
    private String motivoConsulta;

    @Column(name = "zonas_dolor")
    private String zonasDolor;

    @Column(name = "escala_dolor")
    private Integer escalaDolor;

    @Column(name = "tiene_asma")
    private Boolean tieneAsma;

    @Column(name = "tiene_diabetes")
    private Boolean tieneDiabetes;

    @Column(name = "tiene_alergias")
    private Boolean tieneAlergias;

    @ManyToOne
    @JoinColumn(name = "cod_distrito")
    private Distrito distrito;
}