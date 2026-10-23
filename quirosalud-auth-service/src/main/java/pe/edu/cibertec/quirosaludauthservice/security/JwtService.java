package pe.edu.cibertec.quirosaludauthservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.quirosaludauthservice.model.Usuario;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final SecretKey key;

    public JwtService(@Value("${security.jwt.secret-base64}") String encodedKey) {
        if (encodedKey == null || encodedKey.isBlank()) {
            throw new IllegalStateException("Configure JWT_SECRET_BASE64 (mínimo 32 bytes aleatorios).");
        }
        byte[] bytes = Base64.getDecoder().decode(encodedKey);
        if (bytes.length < 32) {
            throw new IllegalStateException("JWT_SECRET_BASE64 debe tener al menos 32 bytes decodificados.");
        }
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    public String extraerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return header != null && header.startsWith("Bearer ") ? header.substring(7) : null;
    }

    public Claims obtenerClaims(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public boolean tokenValido(String token) {
        try {
            Claims c = obtenerClaims(token);
            return "quirosalud-auth".equals(c.getIssuer()) && c.getExpiration() != null;
        } catch (RuntimeException exception) {
            return false;
        }
    }

    public String generarToken(Usuario usuario, List<GrantedAuthority> authorities) {
        Instant now = Instant.now();
        return Jwts.builder()
                .id(usuario.getIdUsuario().toString())
                .issuer("quirosalud-auth")
                .subject(usuario.getEmail())
                .claim("authorities", authorities.stream().map(GrantedAuthority::getAuthority).toList())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(3 * 60 * 60)))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public void generarAuthSecurity(Claims claims) {
        Object raw = claims.get("authorities");
        List<String> roles = raw instanceof List<?> list
                ? list.stream().filter(String.class::isInstance).map(String.class::cast).toList()
                : List.of();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                claims.getSubject(), null, roles.stream().map(SimpleGrantedAuthority::new).toList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
