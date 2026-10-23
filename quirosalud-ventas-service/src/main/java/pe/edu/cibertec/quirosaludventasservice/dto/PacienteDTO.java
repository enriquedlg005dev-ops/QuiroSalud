package pe.edu.cibertec.quirosaludventasservice.dto;

import lombok.Data;

@Data
public class PacienteDTO {
    private Integer idPersona;
    private String dni;
    private String nombres;
    private String apellidos;
}
