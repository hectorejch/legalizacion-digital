package ar.com.cnpmweb.legalizaciondigital.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "api_keys")
public class ApiKey {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "api_usuario")
    private String apiUsuario;
    
    @Column(name = "api_contrasena")
    private String apiContrasena;
    
    @Column(name = "activo")
    private boolean activo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiUsuario() {
        return apiUsuario;
    }

    public void setApiUsuario(String apiUsuario) {
        this.apiUsuario = apiUsuario;
    }

    public String getApiContrasena() {
        return apiContrasena;
    }

    public void setApiContrasena(String apiContrasena) {
        this.apiContrasena = apiContrasena;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}