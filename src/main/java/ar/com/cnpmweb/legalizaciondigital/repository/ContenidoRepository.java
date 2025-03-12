package ar.com.cnpmweb.legalizaciondigital.repository;

import ar.com.cnpmweb.legalizaciondigital.model.Contenido;
import ar.com.cnpmweb.legalizaciondigital.model.ContenidoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ContenidoRepository extends JpaRepository<Contenido, ContenidoId> {

    @Query("SELECT c FROM Contenido c WHERE c.idSucursal = ?1 AND c.idFactura = ?2")
    List<Contenido> findByFacturaId(Integer idSucursal, Integer idFactura);

    @Query("SELECT c FROM Contenido c WHERE c.cliId = ?1")
    List<Contenido> findByCliId(Integer cliId);

    @Query("SELECT c FROM Contenido c JOIN c.factura f WHERE " +
            "c.idProducto = :idProducto AND c.idSubProd = :idSubProd AND " +
            ":numeroFoja BETWEEN c.primerNumero AND c.ultimoNumero AND " +
            "f.tipoComprobante IN :tiposComprobante AND " +
            "f.fechaComprobante <= :fechaActuacion")
    List<Contenido> findByTipoFojaAndNumero(
            @Param("idProducto") Integer idProducto,
            @Param("idSubProd") Integer idSubProd,
            @Param("numeroFoja") Integer numeroFoja,
            @Param("tiposComprobante") List<Integer> tiposComprobante,
            @Param("fechaActuacion") Date fechaActuacion);
}