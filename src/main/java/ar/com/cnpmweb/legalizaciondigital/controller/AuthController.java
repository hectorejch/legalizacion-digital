package ar.com.cnpmweb.legalizaciondigital.controller;

import ar.com.cnpmweb.legalizaciondigital.dto.AuthRequest;
import ar.com.cnpmweb.legalizaciondigital.dto.AuthResponse;
import ar.com.cnpmweb.legalizaciondigital.service.AuthService;
import ar.com.cnpmweb.legalizaciondigital.service.AuthService.AuthResult;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PutMapping
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        logger.info("Recibida solicitud de autenticación para el usuario: {}", request.getUsuario());
        
        AuthResult result = authService.verifyCredentials(request.getUsuario(), request.getContrasena());
        
        AuthResponse response = new AuthResponse();
        
        if (result.isAutenticado()) {
            // El usuario existe y la contraseña coincide
/*             if (result.isHabilitado()) {
                // Está habilitado
                response.setCodigo("1");
            } else {
                // No está habilitado
                response.setCodigo("2");
            } */
            response.setCodigo(result.getCodigo());
            response.setMensaje(result.getMensaje());
            response.setMatricula(result.getMatricula());

            if (result.tieneSuplenciasDisponibles()) {
                response.setCodigoSuplencia("1"); // Hay suplencias disponibles
                response.setSuplenciasDisponibles(result.getSuplenciasDisponibles());
            } else {
                response.setCodigoSuplencia("2"); // No hay suplencias disponibles
                response.setSuplenciasDisponibles(Collections.emptyList());
            }
            
            logger.info("Autenticación: {}, Habilitación: {}, Suplencias disponibles: {} para el usuario: {}", 
            result.isAutenticado(), result.isHabilitado(), 
            result.tieneSuplenciasDisponibles(), request.getUsuario());
        } else {
            // Usuario o contraseña incorrectos
            response.setCodigo("3");
            response.setMensaje(result.getMensaje());
            response.setCodigoSuplencia("2"); // No hay suplencias disponibles
            response.setSuplenciasDisponibles(Collections.emptyList());
            
            logger.warn("Autenticación fallida para el usuario: {}", request.getUsuario());
        }
        
        return ResponseEntity.ok(response);
    }
}