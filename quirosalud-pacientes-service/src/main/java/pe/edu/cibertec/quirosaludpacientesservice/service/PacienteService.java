package pe.edu.cibertec.quirosaludpacientesservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.quirosaludpacientesservice.model.Paciente;
import pe.edu.cibertec.quirosaludpacientesservice.repository.PacienteRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    public Paciente guardar(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public Paciente obtenerPorId(Integer id) {
        return pacienteRepository.findById(id).orElse(null);
    }

    public Paciente obtenerPorDni(String dni) {
        return pacienteRepository.findByDni(dni);
    }

    @Transactional
    public void eliminar(Integer id) {
        pacienteRepository.deleteById(id);
    }
}
