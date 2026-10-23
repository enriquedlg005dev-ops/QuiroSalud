package pe.edu.cibertec.quirosaludpacientesservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record PacienteRequest(
    @NotBlank @Pattern(regexp = "[0-9]{8}") String dni,
    @NotBlank @Size(max = 50) String nombres,
    @NotBlank @Size(max = 50) String apellidos,
    @NotNull @Past LocalDate fechaNacimiento,
    @NotBlank @Pattern(regexp = "[MF]") String sexo,
    @NotBlank @Pattern(regexp = "[SCVDT]") String estadoCivil,
    @Size(max = 50) String ocupacion,
    @Min(0) Integer numHijos,
    @Pattern(regexp = "[0-9]{0,15}") String telefono,
    @Email @Size(max = 50) String email,
    @NotBlank @Size(max = 100) String direccion,
    @NotBlank @Size(max = 5) String codDistrito,
    @Valid HistoriaRequest historia
) {}
