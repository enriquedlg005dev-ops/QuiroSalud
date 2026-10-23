package pe.edu.cibertec.quirosaludauthservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.quirosaludauthservice.dto.CambioClaveRequest;
import pe.edu.cibertec.quirosaludauthservice.dto.ErrorMessage;
import pe.edu.cibertec.quirosaludauthservice.dto.GenericResponse;
import pe.edu.cibertec.quirosaludauthservice.dto.Login;
import pe.edu.cibertec.quirosaludauthservice.dto.UsuarioJwt;
import pe.edu.cibertec.quirosaludauthservice.model.Usuario;
import pe.edu.cibertec.quirosaludauthservice.repository.UsuarioRepository;
import pe.edu.cibertec.quirosaludauthservice.security.JwtService;
import pe.edu.cibertec.quirosaludauthservice.service.CambioClaveService;
import pe.edu.cibertec.quirosaludauthservice.service.DetalleUsuarioService;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final DetalleUsuarioService detalleUsuarioService;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final UsuarioRepository usuarioRepository;
    private final CambioClaveService cambioClaveService;

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<UsuarioJwt>> login(@Valid @RequestBody Login login) {
        GenericResponse<UsuarioJwt> response;
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getClave()));
            Usuario usuario = usuarioRepository.findByEmail(login.getEmail()).orElseThrow();
            String token = jwtService.generarToken(usuario, detalleUsuarioService.getAuthorities(usuario));

            response = GenericResponse.<UsuarioJwt>builder()
                    .response(UsuarioJwt.builder()
                            .idUsuario(usuario.getIdUsuario())
                            .email(usuario.getEmail())
                            .nombres(usuario.getNombres())
                            .cargo(usuario.getCargo())
                            .token(token).build())
                    .build();
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            response = GenericResponse.<UsuarioJwt>builder()
                    .error(ErrorMessage.builder()
                            .dateError(LocalDate.now())
                            .message("Usuario y/o password incorrecto")
                            .build())
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/cambiar-clave")
    public ResponseEntity<Void> cambiarClave(
            @Valid @RequestBody CambioClaveRequest request,
            Authentication authentication) {
        cambioClaveService.cambiarClave(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }
}
