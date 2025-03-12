package ar.com.cnpmweb.legalizaciondigital.repository;

import ar.com.cnpmweb.legalizaciondigital.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsuario(String usuario);
}