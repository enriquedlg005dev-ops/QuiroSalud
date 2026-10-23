package pe.edu.cibertec.quirosaludventasservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.cibertec.quirosaludventasservice.model.Tratamiento;

import java.util.List;

@Repository
public interface TratamientoRepository extends JpaRepository<Tratamiento, Integer> {
    List<Tratamiento> findByEstado(Integer estado);
}
