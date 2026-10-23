package pe.edu.cibertec.quirosaludventasservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.quirosaludventasservice.model.Tratamiento;
import pe.edu.cibertec.quirosaludventasservice.repository.TratamientoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TratamientoService {

    private final TratamientoRepository tratamientoRepository;

    public List<Tratamiento> listarActivos() {
        return tratamientoRepository.findByEstado(1);
    }

    public List<Tratamiento> listarTodos() {
        return tratamientoRepository.findAll();
    }

    public Tratamiento obtenerPorId(Integer id) {
        return tratamientoRepository.findById(id).orElse(null);
    }

    public Tratamiento guardar(Tratamiento t) {
        if (t.getEstado() == null) {
            t.setEstado(1);
        }
        return tratamientoRepository.save(t);
    }

    public void eliminarLogico(Integer id) {
        Tratamiento t = obtenerPorId(id);
        if (t != null) {
            t.setEstado(0);
            tratamientoRepository.save(t);
        }
    }
}
