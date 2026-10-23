package pe.edu.cibertec.quirosaludcitasservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.quirosaludcitasservice.model.Consulta;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {
    List<Consulta> findByEstado(String estado);
    List<Consulta> findByDniPacienteOrderByIdConsultaDesc(String dniPaciente);
}