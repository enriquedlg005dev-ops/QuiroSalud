package pe.edu.cibertec.quirosaludpacientesservice.dto;

import pe.edu.cibertec.quirosaludpacientesservice.model.Paciente;
import java.util.Date;

public record PacienteResponse(String dni, String nombres, String apellidos,
        Date fechaNacimiento, String sexo, String estadoCivil, String ocupacion,
        String telefono, String email, String direccion, String codDistrito) {
    public static PacienteResponse from(Paciente p) {
        String cod = p.getDistrito() != null ? p.getDistrito().getCodDistrito() : null;
        return new PacienteResponse(p.getDni(), p.getNombres(), p.getApellidos(),
                p.getFechaNacimiento(), p.getSexo(), p.getEstadoCivil(), p.getOcupacion(),
                p.getTelefono(), p.getEmail(), p.getDireccion(), cod);
    }
}
