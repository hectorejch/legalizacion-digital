package ar.com.cnpmweb.legalizaciondigital.dto;

public class ApiCredentialsRequest {
    private String apiUsuario;
    private String apiContrasena;

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
}