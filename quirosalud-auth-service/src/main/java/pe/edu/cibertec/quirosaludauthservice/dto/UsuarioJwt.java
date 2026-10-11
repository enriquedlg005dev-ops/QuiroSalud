package pe.edu.cibertec.quirosaludauthservice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UsuarioJwt {
    private Integer idUsuario;
    private String email;
    private String token;
}