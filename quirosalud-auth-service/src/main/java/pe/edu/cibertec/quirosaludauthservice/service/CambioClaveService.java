package pe.edu.cibertec.quirosaludauthservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.cibertec.quirosaludauthservice.dto.CambioClaveRequest;
import pe.edu.cibertec.quirosaludauthservice.model.Usuario;
import pe.edu.cibertec.quirosaludauthservice.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class CambioClaveService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void cambiarClave(String emailAutenticado, CambioClaveRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesión no válida"));

        if (!passwordEncoder.matches(request.claveActual(), usuario.getClave())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña actual no es correcta");
        }
        if (passwordEncoder.matches(request.claveNueva(), usuario.getClave())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La nueva contraseña debe ser diferente");
        }
        usuario.setClave(passwordEncoder.encode(request.claveNueva()));
        // La entidad JPA queda actualizada al confirmar la transacción.
    }
}
