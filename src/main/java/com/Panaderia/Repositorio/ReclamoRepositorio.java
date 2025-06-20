package com.Panaderia.Repositorio;

import com.Panaderia.Modelo.Reclamo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReclamoRepositorio extends JpaRepository<Reclamo, Integer> {
    List<Reclamo> findByEstadoReclamo(String Estado);
    /*@Query("SELECT p FROM Reclamo p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Reclamo> buscarPorTexto(String texto);*/
}
