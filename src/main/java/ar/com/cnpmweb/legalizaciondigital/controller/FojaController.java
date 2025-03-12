package ar.com.cnpmweb.legalizaciondigital.controller;

import ar.com.cnpmweb.legalizaciondigital.service.FojaService;
import ar.com.cnpmweb.legalizaciondigital.dto.VerificarFojaRequest;
import ar.com.cnpmweb.legalizaciondigital.dto.VerificarFojaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fojas")
public class FojaController {
    private static final Logger logger = LoggerFactory.getLogger(FojaController.class);

    @Autowired
    private FojaService fojaService;
    
    @PostMapping("/verificar")
    public ResponseEntity<VerificarFojaResponse> verificarFoja(@RequestBody VerificarFojaRequest request) {
        logger.info("Recibida solicitud para verificar foja tipo: {}, número: {}, fecha: {}", 
                   request.getTipoFoja(), request.getNumeroFoja(), request.getFechaActuacion());
        
        VerificarFojaResponse response = fojaService.verificarFoja(request);
        
        HttpStatus status = response.getCodigo().equals("OK") ? 
                HttpStatus.OK : HttpStatus.BAD_REQUEST;
        
        return new ResponseEntity<>(response, status);
    }
}