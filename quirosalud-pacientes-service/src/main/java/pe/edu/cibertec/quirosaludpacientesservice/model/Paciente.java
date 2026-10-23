package pe.edu.cibertec.quirosaludpacientesservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter @Setter
@Entity @Table(name = "tb_paciente")
public class Paciente {
    @Id @Column(name = "dni", length = 8, nullable = false) private String dni;
    @Column(length = 50, nullable = false) private String nombres;
    @Column(length = 50, nullable = false) private String apellidos;
    @Column(name = "fecha_nac", nullable = false) private LocalDate fechaNacimiento;
    @Column(length = 1, nullable = false) private String sexo;
    @Column(name = "estado_civil", length = 1, nullable = false) private String estadoCivil;
    @Column(length = 50) private String ocupacion;
    @Column(name = "num_hijos") private Integer numHijos;
    @Column(length = 15) private String telefono;
    @Column(length = 50) private String email;
    @Column(length = 100, nullable = false) private String direccion;
    @Column(name = "cod_distrito", length = 5, nullable = false) private String codDistrito;
}
