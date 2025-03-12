package ar.com.cnpmweb.legalizaciondigital.service;

import ar.com.cnpmweb.legalizaciondigital.model.ApiKey;
import ar.com.cnpmweb.legalizaciondigital.repository.ApiKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private ApiKeyRepository apiKeyRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Cargando detalles de usuario para: {}", username);
        
        Optional<ApiKey> apiKeyOpt = apiKeyRepository.findByApiUsuario(username);
        
        if (apiKeyOpt.isPresent() && apiKeyOpt.get().isActivo()) {
            logger.debug("Usuario encontrado y activo: {}", username);
            return new User(
                apiKeyOpt.get().getApiUsuario(),
                apiKeyOpt.get().getApiContrasena(),
                new ArrayList<>()
            );
        }
        
        logger.warn("Usuario no encontrado o inactivo: {}", username);
        throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }
}