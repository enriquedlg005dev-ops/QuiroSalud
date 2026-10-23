package pe.edu.cibertec.quirosaludpacientesservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.cibertec.quirosaludpacientesservice.dto.*;
import pe.edu.cibertec.quirosaludpacientesservice.model.*;
import pe.edu.cibertec.quirosaludpacientesservice.repository.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final PacienteRepository pacienteRepository;
    private final HistoriaRepository historiaRepository;

    @Transactional(readOnly = true)
    public List<PacienteResponse> listarTodos() {
        return pacienteRepository.findAll().stream().map(PacienteResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscar(String dni) { return PacienteResponse.from(encontrar(dni)); }

    @Transactional(readOnly = true)
    public HistoriaResponse obtenerHistoria(String dni) {
        encontrar(dni);
        return historiaRepository.findById(dni).map(HistoriaResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Historia no registrada"));
    }

    @Transactional
    public PacienteResponse guardar(PacienteRequest datos, Authentication auth) {
        if (pacienteRepository.existsById(datos.dni())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "DNI ya registrado");
        }
        validarPermisoHistoria(datos, auth);
        Paciente p = copiar(new Paciente(), datos);
        pacienteRepository.save(p);
        guardarHistoria(datos.historia(), p);
        return PacienteResponse.from(p);
    }

    @Transactional
    public PacienteResponse actualizar(String dni, PacienteRequest datos, Authentication auth) {
        if (!dni.equals(datos.dni())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El DNI no puede cambiarse");
        }
        validarPermisoHistoria(datos, auth);
        Paciente p = copiar(encontrar(dni), datos);
        guardarHistoria(datos.historia(), p);
        return PacienteResponse.from(p);
    }

    @Transactional
    public void eliminar(String dni) {
        Paciente p = encontrar(dni);
        historiaRepository.deleteById(dni);
        pacienteRepository.delete(p);
    }

    private Paciente encontrar(String dni) {
        return pacienteRepository.findById(dni)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));
    }

    private void validarPermisoHistoria(PacienteRequest datos, Authentication auth) {
        if (datos.historia() != null && auth.getAuthorities().stream().noneMatch(a ->
                a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_MEDICO"))) {
            throw new AccessDeniedException("No autorizado para editar historia clínica");
        }
    }

    private Paciente copiar(Paciente p, PacienteRequest d) {
        p.setDni(d.dni()); p.setNombres(d.nombres().trim()); p.setApellidos(d.apellidos().trim());
        p.setFechaNacimiento(d.fechaNacimiento()); p.setSexo(d.sexo()); p.setEstadoCivil(d.estadoCivil());
        p.setOcupacion(d.ocupacion()); p.setNumHijos(d.numHijos()); p.setTelefono(d.telefono());
        p.setEmail(d.email() == null || d.email().isBlank() ? null : d.email().trim());
        p.setDireccion(d.direccion().trim()); p.setCodDistrito(d.codDistrito());
        return p;
    }

    private void guardarHistoria(HistoriaRequest datos, Paciente paciente) {
        if (datos == null) return;
        HistoriaClinica h = historiaRepository.findById(paciente.getDni()).orElseGet(HistoriaClinica::new);
        h.setPaciente(paciente);
        h.setMotivoConsulta(datos.motivoConsulta()); h.setEscalaDolor(datos.escalaDolor());
        h.setAntecedenteTrauma(datos.antecedenteTrauma()); h.setAntecedenteCirugia(datos.antecedenteCirugia());
        h.setTieneAsma(datos.tieneAsma()); h.setTieneDiabetes(datos.tieneDiabetes());
        h.setTieneAlergias(datos.tieneAlergias()); h.setObservaciones(datos.observaciones());
        historiaRepository.save(h);
    }
}
