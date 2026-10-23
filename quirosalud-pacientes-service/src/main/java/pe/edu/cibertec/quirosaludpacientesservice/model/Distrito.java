package pe.edu.cibertec.quirosaludpacientesservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_distrito")
@Data
public class Distrito {

    @Id
    @Column(name = "cod_distrito", length = 5)
    private String codDistrito;

    @Column(name = "nombre_distrito", length = 50)
    private String nombreDistrito;
}