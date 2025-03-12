package ar.com.cnpmweb.legalizaciondigital.service;

import ar.com.cnpmweb.legalizaciondigital.model.ApiKey;
import ar.com.cnpmweb.legalizaciondigital.repository.ApiKeyRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class ApiKeyService {
    private static final Logger logger = LoggerFactory.getLogger(ApiKeyService.class);
    //

    //private static final long EXPIRATION_TIME = 86_400_000; // 1 día en milisegundos
    // private static final long EXPIRATION_TIME = 30_000; // 30 segundos en
    // milisegundos

    @Value("${api.jwt.secret}")
    private String secretKey;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /*
     * public String generateToken(String apiUsuario, String apiContrasena) {
     * logger.debug("Generando token para el usuario API: {}", apiUsuario);
     * 
     * Optional<ApiKey> apiKeyOpt = apiKeyRepository.findByApiUsuario(apiUsuario);
     * 
     * if (apiKeyOpt.isPresent() && apiKeyOpt.get().isActivo() &&
     * passwordEncoder.matches(apiContrasena, apiKeyOpt.get().getApiContrasena())) {
     * 
     * String token = Jwts.builder()
     * .setSubject(apiUsuario)
     * .setIssuedAt(new Date())
     * .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
     * .signWith(getSigningKey(), SignatureAlgorithm.HS512)
     * .compact();
     * 
     * logger.debug("Token generado exitosamente para: {}", apiUsuario);
     * return token;
     * }
     * 
     * logger.warn("No se pudo generar token para: {}", apiUsuario);
     * return null;
     * }
     */

    public String generateToken(String apiUsuario, String apiContrasena) {
        logger.debug("Generando token para el usuario API: {}", apiUsuario);

        Optional<ApiKey> apiKeyOpt = apiKeyRepository.findByApiUsuario(apiUsuario);

        if (apiKeyOpt.isPresent() && apiKeyOpt.get().isActivo()) {
            // Compara directamente con el hash MD5 almacenado
            String md5Hash = passwordEncoder.encode(apiContrasena);
            if (md5Hash.equals(apiKeyOpt.get().getApiContrasena())) {
            
            // Esta línea es para establecer el EXPIRATION TIME en 1 día
            /*    String token = Jwts.builder()
                        .setSubject(apiUsuario)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Cambiar a HS256
                        .compact();*/
            
            // Esta línea es para establecer el EXPIRATION TIME al final del día
                String token = Jwts.builder()
                        .setSubject(apiUsuario)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(getEndOfDay().getTime()))
                        .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Cambiar a HS256
                        .compact();

                logger.debug("Token generado exitosamente para: {}", apiUsuario);
                return token;
            }
        }

        logger.warn("No se pudo generar token para: {}", apiUsuario);
        return null;
    }

    public String validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            Date expiration = claims.getExpiration();

            if (username != null && expiration != null && expiration.after(new Date())) {
                Optional<ApiKey> apiKeyOpt = apiKeyRepository.findByApiUsuario(username);
                if (apiKeyOpt.isPresent() && apiKeyOpt.get().isActivo()) {
                    logger.debug("Token validado para: {}", username);
                    return username;
                }
            }
        } catch (Exception e) {
            logger.warn("Error al validar token: {}", e.getMessage());
        }

        logger.debug("Token inválido o expirado");
        return null;
    }

    private static Date getEndOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}