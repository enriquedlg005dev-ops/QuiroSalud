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

    @Column(nullable = false, length = 20)
    private String cargo;

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public Integer getIdUsuario() {
        return super.getIdPersona();
    }


    public void setIdUsuario(Integer id) {
        super.setIdPersona(id);
    }
}