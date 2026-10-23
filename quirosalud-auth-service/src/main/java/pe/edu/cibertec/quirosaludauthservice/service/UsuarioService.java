package pe.edu.cibertec.quirosaludauthservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.quirosaludauthservice.model.Usuario;
import pe.edu.cibertec.quirosaludauthservice.repository.UsuarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        String claveCifrada = passwordEncoder.encode(usuario.getClave());
        usuario.setClave(claveCifrada);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizarUsuario(Integer id, Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(id).orElse(null);
        if (usuarioExistente != null) {
            usuarioExistente.setNombres(usuario.getNombres());
            usuarioExistente.setApellidos(usuario.getApellidos());
            usuarioExistente.setEmail(usuario.getEmail());
            usuarioExistente.setCargo(usuario.getCargo());

            if (usuario.getClave() != null && !usuario.getClave().isBlank()) {
                usuarioExistente.setClave(passwordEncoder.encode(usuario.getClave()));
            }
            return usuarioRepository.save(usuarioExistente);
        }
        return null;
    }

    @Transactional
    public void eliminarUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }
}
