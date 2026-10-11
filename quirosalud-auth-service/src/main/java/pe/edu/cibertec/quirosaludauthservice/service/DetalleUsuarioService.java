package pe.edu.cibertec.quirosaludauthservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.quirosaludauthservice.model.Usuario;
import pe.edu.cibertec.quirosaludauthservice.repository.UsuarioRepository;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DetalleUsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Buscamos por el email (que es el usuario de tu proyecto)
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        return getUserDetails(usuario, getAuthorities(usuario));
    }

    // 2. ADAPTACIÓN: En vez de un Set de Roles, usamos tu campo "cargo"
    public List<GrantedAuthority> getAuthorities(Usuario usuario) {
        // Le agregamos el prefijo "ROLE_" que exige Spring Security
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getCargo()));
    }

    // 3. ADAPTACIÓN: Le pasamos el email y tu campo "clave"
    public UserDetails getUserDetails(Usuario usuario, List<GrantedAuthority> authorities) {
        return new User(
                usuario.getEmail(),
                usuario.getClave(),
                true, // Asumimos que la cuenta está activa
                true, true, true,
                authorities);
    }
}