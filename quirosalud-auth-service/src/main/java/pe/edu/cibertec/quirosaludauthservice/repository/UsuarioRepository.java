package pe.edu.cibertec.quirosaludauthservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.quirosaludauthservice.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Buscará al usuario por su email para el Login
    Optional<Usuario> findByEmail(String email);

    // Para validar si el correo ya existe al registrar
    boolean existsByEmail(String email);
}