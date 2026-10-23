package pe.edu.cibertec.quirosaludpacientesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.quirosaludpacientesservice.model.Distrito;

@Repository
public interface DistritoRepository extends JpaRepository<Distrito, String> {
}
