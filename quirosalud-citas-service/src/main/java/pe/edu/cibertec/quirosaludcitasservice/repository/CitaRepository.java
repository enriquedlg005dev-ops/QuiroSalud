package pe.edu.cibertec.quirosaludcitasservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.quirosaludcitasservice.model.Cita;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {
    List<Cita> findByDniPacienteOrderByNumeroSesionAsc(String dniPaciente);
}
