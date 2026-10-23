package pe.edu.cibertec.quirosaludauthservice.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pe.edu.cibertec.quirosaludauthservice.model.Usuario;
import java.util.Base64;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    @Test void rechazaClaveInsuficiente() {
        assertThrows(IllegalStateException.class, () -> new JwtService(Base64.getEncoder().encodeToString(new byte[8])));
    }

    @Test void tokenFirmadoContieneRolYEmisor() {
        JwtService servicio = new JwtService(Base64.getEncoder().encodeToString(new byte[32]));
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(10); usuario.setEmail("admin@ejemplo.test");
        String jwt = servicio.generarToken(usuario, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertTrue(servicio.tokenValido(jwt));
        assertEquals("quirosalud-auth", servicio.obtenerClaims(jwt).getIssuer());
    }
}
