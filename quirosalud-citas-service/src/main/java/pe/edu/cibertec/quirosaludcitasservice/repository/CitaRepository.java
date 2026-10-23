package pe.edu.cibertec.quirosaludcitasservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.quirosaludcitasservice.model.Cita;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {
    // ¡Adiós al @Query lento con triple JOIN! Ahora filtramos directo por el DNI
    List<Cita> findByDniPacienteOrderByNumeroSesionAsc(String dniPaciente);
}