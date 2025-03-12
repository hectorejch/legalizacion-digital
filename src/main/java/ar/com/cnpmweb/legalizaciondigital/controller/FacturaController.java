package ar.com.cnpmweb.legalizaciondigital.controller;

import ar.com.cnpmweb.legalizaciondigital.dto.FacturaDTO;
import ar.com.cnpmweb.legalizaciondigital.service.FacturaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {
    private static final Logger logger = LoggerFactory.getLogger(FacturaController.class);

    @Autowired
    private FacturaService facturaService;

    @GetMapping("/cliente/{cliId}")
    public ResponseEntity<List<FacturaDTO>> getFacturasByCliId(@PathVariable Integer cliId) {
        logger.info("Consultando facturas para el cliente ID: {}", cliId);
        
        List<FacturaDTO> facturas = facturaService.getFacturasByCliId(cliId);
        
        return ResponseEntity.ok(facturas);
    }
    
    @GetMapping("/cliente/{cliId}/registro/{numRegistro}")
    public ResponseEntity<List<FacturaDTO>> getFacturasByCliIdAndNumRegistro(
            @PathVariable Integer cliId,
            @PathVariable Integer numRegistro) {
        
        logger.info("Consultando facturas para el cliente ID: {} y registro: {}", cliId, numRegistro);
        
        List<FacturaDTO> facturas = facturaService.getFacturasByCliIdAndNumRegistro(cliId, numRegistro);
        
        return ResponseEntity.ok(facturas);
    }
    
    @GetMapping("/{idSucursal}/{idFactura}")
    public ResponseEntity<FacturaDTO> getFacturaById(
            @PathVariable Integer idSucursal,
            @PathVariable Integer idFactura) {
        
        logger.info("Consultando factura con ID: {}/{}", idSucursal, idFactura);
        
        FacturaDTO factura = facturaService.getFacturaById(idSucursal, idFactura);
        
        if (factura != null) {
            return ResponseEntity.ok(factura);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}