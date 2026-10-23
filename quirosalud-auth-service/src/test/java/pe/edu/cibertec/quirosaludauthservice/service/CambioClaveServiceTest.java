package pe.edu.cibertec.quirosaludauthservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.cibertec.quirosaludauthservice.dto.CambioClaveRequest;
import pe.edu.cibertec.quirosaludauthservice.model.Usuario;
import pe.edu.cibertec.quirosaludauthservice.repository.UsuarioRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CambioClaveServiceTest {
    @Mock UsuarioRepository usuarioRepository;
    @Mock PasswordEncoder encoder;
    @InjectMocks CambioClaveService service;
    private Usuario usuario;

    @BeforeEach
    void configurar() {
        usuario = new Usuario();
        usuario.setEmail("admin@quirosalud.local");
        usuario.setClave("hash-anterior");
    }

    @Test
    void cambiaClaveCuandoActualEsCorrecta() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(encoder.matches("anterior-segura", "hash-anterior")).thenReturn(true);
        when(encoder.matches("NuevaClaveSegura2026!", "hash-anterior")).thenReturn(false);
        when(encoder.encode("NuevaClaveSegura2026!")).thenReturn("nuevo-hash-bcrypt");

        service.cambiarClave(usuario.getEmail(), new CambioClaveRequest("anterior-segura", "NuevaClaveSegura2026!"));

        assertEquals("nuevo-hash-bcrypt", usuario.getClave());
        verify(encoder).encode("NuevaClaveSegura2026!");
    }

    @Test
    void rechazaClaveActualIncorrecta() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(encoder.matches("equivocada", "hash-anterior")).thenReturn(false);

        assertThrows(ResponseStatusException.class, () ->
                service.cambiarClave(usuario.getEmail(), new CambioClaveRequest("equivocada", "NuevaClaveSegura2026!")));
        verify(encoder, never()).encode(anyString());
        assertEquals("hash-anterior", usuario.getClave());
    }

    @Test
    void rechazaLaMismaClave() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(encoder.matches("anterior-segura", "hash-anterior")).thenReturn(true);
        when(encoder.matches("misma-clave-segura", "hash-anterior")).thenReturn(true);

        assertThrows(ResponseStatusException.class, () ->
                service.cambiarClave(usuario.getEmail(), new CambioClaveRequest("anterior-segura", "misma-clave-segura")));
        verify(encoder, never()).encode(anyString());
    }
}
