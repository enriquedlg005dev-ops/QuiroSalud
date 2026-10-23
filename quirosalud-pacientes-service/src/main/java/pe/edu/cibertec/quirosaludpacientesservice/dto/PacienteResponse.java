package pe.edu.cibertec.quirosaludpacientesservice.dto;

import pe.edu.cibertec.quirosaludpacientesservice.model.Paciente;
import java.time.LocalDate;

public record PacienteResponse(String dni, String nombres, String apellidos,
        LocalDate fechaNacimiento, String sexo, String estadoCivil, String ocupacion,
        Integer numHijos, String telefono, String email, String direccion, String codDistrito) {
    public static PacienteResponse from(Paciente p) {
        return new PacienteResponse(p.getDni(), p.getNombres(), p.getApellidos(),
                p.getFechaNacimiento(), p.getSexo(), p.getEstadoCivil(), p.getOcupacion(),
                p.getNumHijos(), p.getTelefono(), p.getEmail(), p.getDireccion(), p.getCodDistrito());
    }
}
