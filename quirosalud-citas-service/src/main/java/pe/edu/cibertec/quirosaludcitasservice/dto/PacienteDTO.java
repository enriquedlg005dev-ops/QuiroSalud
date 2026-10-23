package pe.edu.cibertec.quirosaludcitasservice.dto;

import lombok.Data;
import java.util.Date;

@Data
public class PacienteDTO {
    private Integer idPersona;
    private String dni;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String email;
    private Date fechaNacimiento;
    private String sexo;
    private String estadoCivil;
    private String ocupacion;
    private String direccion;
    private String motivoConsulta;
    private String zonasDolor;
    private Integer escalaDolor;
    private Boolean tieneAsma;
    private Boolean tieneDiabetes;
    private Boolean tieneAlergias;
}
