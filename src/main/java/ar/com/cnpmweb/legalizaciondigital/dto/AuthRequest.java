package ar.com.cnpmweb.legalizaciondigital.dto;

public class AuthRequest {
    private String usuario;
    private String contrasena; // Se recibe en MD5

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