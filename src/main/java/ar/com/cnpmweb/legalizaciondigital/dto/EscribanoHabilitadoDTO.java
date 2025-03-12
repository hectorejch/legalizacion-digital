package ar.com.cnpmweb.legalizaciondigital.dto;

public class EscribanoHabilitadoDTO {
    private Long matricula;
    private String nombre;
    private String apellido;
    
    public Long getMatricula() {
        return matricula;
    }
    
    public void setMatricula(Long matricula) {
        this.matricula = matricula;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String getNombreCompleto() {
        return apellido + ", " + nombre;
    }
}