package pe.edu.cibertec.quirosaludventasservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VentaRequest {
    @NotBlank(message = "El DNI del paciente es obligatorio")
    private String dniPaciente;

    @NotNull(message = "El tratamiento es obligatorio")
    private Integer idTratamiento;

    @NotNull(message = "El número de sesiones es obligatorio")
    @Min(value = 1, message = "Debe registrar al menos 1 sesión")
    private Integer totalSesiones;

    @NotNull(message = "El costo total es obligatorio")
    @Min(value = 0, message = "El costo no puede ser negativo")
    private Double costoTotal;

    private Integer idUsuario;
}
