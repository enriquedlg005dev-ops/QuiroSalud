package pe.edu.cibertec.quirosaludauthservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Login {
    @NotBlank @Email private String email;
    @NotBlank private String clave;
}
