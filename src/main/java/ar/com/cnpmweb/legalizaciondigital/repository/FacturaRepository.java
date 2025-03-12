package ar.com.cnpmweb.legalizaciondigital.repository;

import ar.com.cnpmweb.legalizaciondigital.model.Factura;
import ar.com.cnpmweb.legalizaciondigital.model.FacturaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, FacturaId> {
    
    @Query("SELECT f FROM Factura f WHERE f.cliId = ?1")
    List<Factura> findByCliId(Integer cliId);
    
    @Query("SELECT f FROM Factura f WHERE f.cliId = ?1 AND f.numRegistro = ?2")
    List<Factura> findByCliIdAndNumRegistro(Integer cliId, Integer numRegistro);
    
    @Query("SELECT f FROM Factura f JOIN FETCH f.contenidos WHERE f.idSucursal = ?1 AND f.idFactura = ?2")
    Optional<Factura> findByIdWithContenidos(Integer idSucursal, Integer idFactura);
}