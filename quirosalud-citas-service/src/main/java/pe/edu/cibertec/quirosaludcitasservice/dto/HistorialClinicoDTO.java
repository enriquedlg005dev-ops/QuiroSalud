package pe.edu.cibertec.quirosaludcitasservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.cibertec.quirosaludcitasservice.model.Cita;
import pe.edu.cibertec.quirosaludcitasservice.model.Consulta;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialClinicoDTO {
    private PacienteDTO paciente;
    private List<Consulta> consultas;
    private List<Cita> sesiones;
}
