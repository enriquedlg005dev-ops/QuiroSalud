package pe.edu.cibertec.quirosaludauthservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CambioClaveRequest(
        @NotBlank String claveActual,
        @NotBlank @Size(min = 10, max = 128) String claveNueva
) {}
