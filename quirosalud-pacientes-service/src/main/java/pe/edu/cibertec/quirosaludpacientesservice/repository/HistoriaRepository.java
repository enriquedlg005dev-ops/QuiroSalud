package pe.edu.cibertec.quirosaludpacientesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.quirosaludpacientesservice.model.HistoriaClinica;

public interface HistoriaRepository extends JpaRepository<HistoriaClinica, String> { }
