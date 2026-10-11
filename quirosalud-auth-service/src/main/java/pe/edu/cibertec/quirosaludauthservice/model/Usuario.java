package pe.edu.cibertec.quirosaludauthservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name="tbl_usuario")
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name="persona_id")
public class Usuario extends Persona {

    @Column(nullable = false, length = 255)
    private String clave;

    // ROLE_MEDICO, ROLE_ASISTENTE, ROLE_ADMIN
    @Column(nullable = false, length = 20)
    private String cargo;

    // Añadir al final de Usuario.java
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    // Creamos un puente para que cuando Angular pida el "idUsuario",
    // Java le entregue el "idPersona" del padre.
    public Integer getIdUsuario() {
        return super.getIdPersona();
    }

    // Cuando el Controlador intente guardar el "idUsuario",
    // se lo pasamos al "idPersona" de la clase padre.
    public void setIdUsuario(Integer id) {
        super.setIdPersona(id);
    }
}