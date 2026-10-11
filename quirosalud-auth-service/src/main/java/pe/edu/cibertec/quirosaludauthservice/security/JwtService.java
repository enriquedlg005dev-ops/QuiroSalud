package pe.edu.cibertec.quirosaludauthservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.quirosaludauthservice.model.Usuario;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final String JWT_SECRET = "mi-clave-super-secreta-123456789";
    private static final Key KEY = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

    public String extraerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public Claims obtenerClaims(String token) {
        return Jwts.parser().verifyWith((SecretKey) KEY).build().parseSignedClaims(token).getPayload();
    }

    public boolean tokenValido(String token) {
        try {
            obtenerClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String generarToken(Usuario usuario, List<GrantedAuthority> authorities) {
        return Jwts.builder()
                .id(usuario.getIdUsuario().toString())
                .subject(usuario.getEmail())
                .claim("authorities",
                        authorities.stream().map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                ).issuedAt(Date.from(Instant.now())
                ).expiration(Date.from(Instant.now().plusSeconds(3*60*60))
                ).signWith(KEY)
                .compact();
    }

    public void generarAuthSecurity(Claims claims){
        List<String> authorities = claims.get("authorities", List.class);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(claims.getSubject(),
                        null,
                        authorities.stream().map(SimpleGrantedAuthority::new).toList());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}