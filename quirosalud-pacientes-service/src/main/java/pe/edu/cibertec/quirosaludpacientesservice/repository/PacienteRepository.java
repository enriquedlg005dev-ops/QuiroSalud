package pe.edu.cibertec.quirosaludpacientesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.quirosaludpacientesservice.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, String> {
}