package ar.com.cnpmweb.legalizaciondigital.controller;

import ar.com.cnpmweb.legalizaciondigital.dto.ApiCredentialsRequest;
import ar.com.cnpmweb.legalizaciondigital.dto.TokenResponse;
import ar.com.cnpmweb.legalizaciondigital.service.ApiKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
public class TokenController {
    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    private ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<TokenResponse> getToken(@RequestBody ApiCredentialsRequest request) {
        logger.info("Solicitud de token para el usuario API: {}", request.getApiUsuario());
        
        String token = apiKeyService.generateToken(request.getApiUsuario(), request.getApiContrasena());
        
        TokenResponse response = new TokenResponse();
        if (token != null) {
            logger.info("Token generado exitosamente para: {}", request.getApiUsuario());
            response.setToken(token);
            response.setExito(true);
            response.setMensaje("Token generado exitosamente");
        } else {
            logger.warn("Credenciales inválidas para: {}", request.getApiUsuario());
            response.setExito(false);
            response.setMensaje("Credenciales de API inválidas");
        }
        
        return ResponseEntity.ok(response);
    }
}