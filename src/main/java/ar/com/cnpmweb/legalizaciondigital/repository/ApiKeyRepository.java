package ar.com.cnpmweb.legalizaciondigital.repository;

import ar.com.cnpmweb.legalizaciondigital.model.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByApiUsuarioAndApiContrasenaAndActivoTrue(String apiUsuario, String apiContrasena);
    Optional<ApiKey> findByApiUsuario(String apiUsuario);
}