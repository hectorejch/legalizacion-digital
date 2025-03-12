package ar.com.cnpmweb.legalizaciondigital.controller;

import ar.com.cnpmweb.legalizaciondigital.dto.EscribanoDTO;
import ar.com.cnpmweb.legalizaciondigital.dto.EscribanoHabilitadoDTO;
import ar.com.cnpmweb.legalizaciondigital.dto.MatriculaHabilitadaDTO;
import ar.com.cnpmweb.legalizaciondigital.service.EscribanoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/escribanos")
public class EscribanoController {
    private static final Logger logger = LoggerFactory.getLogger(EscribanoController.class);

    @Autowired
    private EscribanoService escribanoService;

    @GetMapping
    public ResponseEntity<List<EscribanoDTO>> getAllEscribanos(
            @RequestParam(required = false, defaultValue = "false") boolean incluirInactivos) {
        logger.info("Consulta de todos los escribanos. Incluir inactivos: {}", incluirInactivos);

        List<EscribanoDTO> escribanos = escribanoService.getAllEscribanos(incluirInactivos);

        logger.info("Se encontraron {} escribanos", escribanos.size());
        return ResponseEntity.ok(escribanos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EscribanoDTO> getEscribanoById(@PathVariable Long id) {
        logger.info("Consulta del escribano con ID: {}", id);

        EscribanoDTO escribano = escribanoService.getEscribanoById(id);

        if (escribano != null) {
            logger.info("Escribano encontrado: {}", escribano.getNombreCompleto());
            return ResponseEntity.ok(escribano);
        } else {
            logger.warn("No se encontró el escribano con ID: {}", id);
            //return ResponseEntity.notFound().build();
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/documento/{documento}")
    public ResponseEntity<EscribanoDTO> getEscribanoByDocumento(@PathVariable String documento) {
        logger.info("Consulta del escribano con documento: {}", documento);

        EscribanoDTO escribano = escribanoService.getEscribanoByDocumento(documento);

        if (escribano != null) {
            logger.info("Escribano encontrado: {}", escribano.getNombreCompleto());
            return ResponseEntity.ok(escribano);
        } else {
            logger.warn("No se encontró el escribano con documento: {}", documento);
            return ResponseEntity.noContent().build();
            //return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/habilitado-fecha")
    public ResponseEntity<MatriculaHabilitadaDTO> verificarMatriculaHabilitadaEnFecha(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) {

        logger.info("Verificando si la matrícula {} estaba habilitada en la fecha {}", id, fecha);

        MatriculaHabilitadaDTO resultado = escribanoService.verificarMatriculaHabilitadaEnFecha(id, fecha);

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{matricula}/suplente")
    public ResponseEntity<EscribanoDTO> buscarSuplenteActivo(
            @PathVariable Long matricula,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) {

        logger.info("Consultando suplente activo para la matrícula {} en la fecha {}", matricula, fecha);

        EscribanoDTO suplente = escribanoService.buscarSuplenteActivo(matricula, fecha);

        if (suplente != null) {
            return ResponseEntity.ok(suplente);
        } else {
            return ResponseEntity.noContent().build();
            //return ResponseEntity.notFound().build();
        }
    }

    /**
     * Consulta los escribanos habilitados para firmar en un registro en una fecha
     * determinada.
     * 
     * @param numeroRegistro
     * @param fecha
     * @return
     */
    @GetMapping("/registro/{numeroRegistro}/habilitados")
    public ResponseEntity<List<EscribanoHabilitadoDTO>> buscarEscribanosHabilitadosPorRegistro(
            @PathVariable Integer numeroRegistro,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) {

        logger.info("Consultando escribanos habilitados para el registro {} en la fecha {}", numeroRegistro, fecha);

        List<EscribanoHabilitadoDTO> escribanos = escribanoService
                .buscarEscribanosHabilitadosPorRegistro(numeroRegistro, fecha);

        return ResponseEntity.ok(escribanos);
    }

    /**
     * Busca a qué escribanos está supliendo un escribano en una fecha determinada.
     * 
     * @param matricula Matrícula del escribano suplente
     * @param fecha     Fecha en la que se busca la suplencia
     * @return Lista de escribanos suplidos
     */
    @GetMapping("/{matricula}/supliendo")
    public ResponseEntity<List<EscribanoHabilitadoDTO>> buscarAQuienesSuple(
            @PathVariable Long matricula,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) {

        logger.info("Consultando a quiénes suple el escribano con matrícula {} en la fecha {}", matricula, fecha);

        List<EscribanoHabilitadoDTO> suplidos = escribanoService.buscarAQuienesSuple(matricula, fecha);

        if (suplidos.isEmpty()) {
            logger.info("El escribano con matrícula {} no está supliendo a nadie en la fecha {}", matricula, fecha);
            //return ResponseEntity.ok(Collections.emptyList());
            return ResponseEntity.noContent().build();
        } else {
            logger.info("El escribano con matrícula {} está supliendo a {} escribanos en la fecha {}",
                    matricula, suplidos.size(), fecha);
            return ResponseEntity.ok(suplidos);
        }
    }

}