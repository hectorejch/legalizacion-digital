package ar.com.cnpmweb.legalizaciondigital.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "web_users")
public class Usuario {
    
    @Id
    @Column(name = "idCliente")
    private Long id;
    
    @Column(name = "user_name")
    private String usuario;
    
    @Column(name = "user_pass")
    private String contrasena; // Almacenado en MD5
    
    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}