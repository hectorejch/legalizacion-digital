package ar.com.cnpmweb.legalizaciondigital.dto;

public class EscribanoSimpleDTO {
    private Long matricula;
    private String nombre;
    private String apellido;
    
    // Constructores
    public EscribanoSimpleDTO() {
    }
    
    public EscribanoSimpleDTO(Long matricula, String nombre, String apellido) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.apellido = apellido;
    }
    
    // Getters y setters
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
    
    // Método de conveniencia para obtener el nombre completo
    public String getNombreCompleto() {
        return apellido + ", " + nombre;
    }
}