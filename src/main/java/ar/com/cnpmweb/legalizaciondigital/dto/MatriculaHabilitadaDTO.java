package ar.com.cnpmweb.legalizaciondigital.dto;

public class MatriculaHabilitadaDTO {
    private String codigo;
    private String mensaje;
    private Integer numRegistro;
    private String caracter;

    public MatriculaHabilitadaDTO() {
        // Constructor por defecto
    }

    // Constructor con parámetros
    public MatriculaHabilitadaDTO(String codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    // Getters y Setters

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Integer getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(Integer numRegistro) {
        this.numRegistro = numRegistro;
    }

    public String getCaracter() {
        return caracter;
    }

    public void setCaracter(String caracter) {
        this.caracter = caracter;
    }
}