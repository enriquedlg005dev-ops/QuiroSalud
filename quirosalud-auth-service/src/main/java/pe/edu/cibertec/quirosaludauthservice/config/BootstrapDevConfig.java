package pe.edu.cibertec.quirosaludauthservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.cibertec.quirosaludauthservice.model.Usuario;
import pe.edu.cibertec.quirosaludauthservice.repository.UsuarioRepository;

@Configuration
@Profile("dev")
public class BootstrapDevConfig {
    @Bean
    @ConditionalOnProperty(name = "app.bootstrap.enabled", havingValue = "true")
    CommandLineRunner crearAdminDePrueba(UsuarioRepository repository, PasswordEncoder encoder,
            @Value("${app.bootstrap.email}") String email,
            @Value("${app.bootstrap.password}") String password,
            @Value("${app.bootstrap.name}") String nombres,
            @Value("${app.bootstrap.lastname}") String apellidos) {
        return args -> {
            if (email.isBlank() || password.length() < 12) {
                throw new IllegalStateException("Bootstrap dev: configure email y password de al menos 12 caracteres.");
            }
            if (!repository.existsByEmail(email)) {
                Usuario admin = new Usuario();
                admin.setEmail(email);
                admin.setNombres(nombres);
                admin.setApellidos(apellidos);
                admin.setCargo("ADMIN");
                admin.setClave(encoder.encode(password));
                repository.save(admin);
            }
        };
    }
}
