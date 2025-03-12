package ar.com.cnpmweb.legalizaciondigital.repository;

import ar.com.cnpmweb.legalizaciondigital.model.Escribano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EscribanoRepository extends JpaRepository<Escribano, Long> {
    // Sobrescribir los métodos de búsqueda para filtrar por categoría=1
    
    @Query("SELECT e FROM Escribano e WHERE e.categoria = 1")
    List<Escribano> findAllEscribanos();
    
    @Query("SELECT e FROM Escribano e WHERE e.cliId = ?1 AND e.categoria = 1")
    Optional<Escribano> findEscribanoById(Long id);
    
    @Query("SELECT e FROM Escribano e WHERE e.documento = ?1 AND e.categoria = 1")
    Optional<Escribano> findByDocumento(String documento);
    
    // Para mantener la funcionalidad de findAllByActivoTrue pero con el filtro de categoría
    @Query("SELECT e FROM Escribano e WHERE e.categoria = 1")
    List<Escribano> findAllByActivoTrue();
}