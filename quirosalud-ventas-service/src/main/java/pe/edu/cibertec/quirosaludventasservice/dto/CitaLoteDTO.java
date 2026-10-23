package pe.edu.cibertec.quirosaludventasservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaLoteDTO {
    private String dniPaciente;
    private String nroHp;
    private Integer numeroSesion;
    private String tipoSesion;
    private Boolean asistio;
    private Boolean pagado;
}
