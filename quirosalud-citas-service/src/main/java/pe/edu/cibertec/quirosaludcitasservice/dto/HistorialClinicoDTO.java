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
    private List<Consulta> consultas; // Aquí el doctor ve el diagnóstico y cuántas sesiones recetó
    private List<Cita> sesiones;      // Aquí ve las asistencias y notas de ajuste de cada sesión
}