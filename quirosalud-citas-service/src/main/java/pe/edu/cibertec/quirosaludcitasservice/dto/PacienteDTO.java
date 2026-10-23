package pe.edu.cibertec.quirosaludcitasservice.dto;

import lombok.Data;

@Data
public class PacienteDTO {
    private Integer idPersona;
    private String dni;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String email;
    private String motivoConsulta;
}