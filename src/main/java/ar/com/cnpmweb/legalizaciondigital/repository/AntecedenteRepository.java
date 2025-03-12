package ar.com.cnpmweb.legalizaciondigital.repository;

import ar.com.cnpmweb.legalizaciondigital.model.Antecedente;
import ar.com.cnpmweb.legalizaciondigital.model.AntecedenteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AntecedenteRepository extends JpaRepository<Antecedente, AntecedenteId> {
    
    @Query("SELECT a FROM Antecedente a WHERE a.numRegistro = ?1 AND a.novIdCodigo IN ?2")
    List<Antecedente> findByNumRegistroAndNovedades(Integer numRegistro, List<Integer> tiposNovedad);

    @Query("SELECT a FROM Antecedente a WHERE " +
           "a.cliSuplente = :cliSuplente AND " +
           "a.novIdCodigo = :tipoNovedad AND " +
           "a.fechaAlta IS NOT NULL AND a.fechaBaja IS NOT NULL AND " +
           ":fecha BETWEEN a.fechaAlta AND a.fechaBaja")
    List<Antecedente> findBySuplente(
            @Param("cliSuplente") Integer cliSuplente, 
            @Param("tipoNovedad") Integer tipoNovedad,
            @Param("fecha") Date fecha);
}