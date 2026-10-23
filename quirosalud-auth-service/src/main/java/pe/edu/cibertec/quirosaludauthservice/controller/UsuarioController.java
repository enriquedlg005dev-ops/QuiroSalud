package pe.edu.cibertec.quirosaludauthservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.quirosaludauthservice.model.Usuario;
import pe.edu.cibertec.quirosaludauthservice.service.UsuarioService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping({"", "/listado"})
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable("id") Integer id) {
        Usuario u = usuarioService.obtenerPorId(id);
        if (u == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Usuario no encontrado"));
        }
        return ResponseEntity.ok(u);
    }

    @PostMapping({"", "/registro"})
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        Usuario creado = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", "Usuario registrado y contraseña cifrada con éxito",
                "usuario", creado
        ));
    }

    @PutMapping({"/actualizar/{id}", "/{id}"})
    public ResponseEntity<?> actualizar(@PathVariable("id") Integer id, @RequestBody Usuario usuario) {
        Usuario actualizado = usuarioService.actualizarUsuario(id, usuario);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Usuario no encontrado"));
        }
        return ResponseEntity.ok(Map.of(
                "mensaje", "Usuario actualizado exitosamente",
                "usuario", actualizado
        ));
    }

    @DeleteMapping({"/eliminar/{id}", "/{id}"})
    public ResponseEntity<?> eliminar(@PathVariable("id") Integer id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado exitosamente"));
    }
}
