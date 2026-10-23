package pe.edu.cibertec.quirosaludpacientesservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_persona")
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Integer idPersona;

    @Column(nullable = false, length = 50)
    private String nombres;

    @Column(nullable = false, length = 50)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 50)
    private String email;
}