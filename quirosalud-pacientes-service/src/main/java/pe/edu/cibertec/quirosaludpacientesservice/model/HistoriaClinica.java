package pe.edu.cibertec.quirosaludpacientesservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "tb_historia_clinica")
public class HistoriaClinica {
    @Id @Column(name = "dni", length = 8) private String dni;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId @JoinColumn(name = "dni") private Paciente paciente;
    @Column(name = "motivo_consulta", length = 500) private String motivoConsulta;
    @Column(name = "escala_dolor") private Integer escalaDolor;
    @Column(name = "antecedente_trauma", length = 500) private String antecedenteTrauma;
    @Column(name = "antecedente_cirugia", length = 500) private String antecedenteCirugia;
    @Column(name = "tiene_asma", nullable = false) private boolean tieneAsma;
    @Column(name = "tiene_diabetes", nullable = false) private boolean tieneDiabetes;
    @Column(name = "tiene_alergias", nullable = false) private boolean tieneAlergias;
    @Column(length = 1000) private String observaciones;
}
