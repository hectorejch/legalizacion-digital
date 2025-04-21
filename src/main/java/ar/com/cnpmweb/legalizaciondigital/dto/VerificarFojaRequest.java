package ar.com.cnpmweb.legalizaciondigital.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VerificarFojaRequest {
    private String tipoFoja;
    private Integer numeroFoja;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "America/Argentina/Buenos_Aires")
    private Date fechaActuacion;
    
    // Getters y setters
    public String getTipoFoja() {
        return tipoFoja;
    }
    
    public void setTipoFoja(String tipoFoja) {
        this.tipoFoja = tipoFoja;
    }
    
    public Integer getNumeroFoja() {
        return numeroFoja;
    }
    
    public void setNumeroFoja(Integer numeroFoja) {
        this.numeroFoja = numeroFoja;
    }
    
    public Date getFechaActuacion() {
        return fechaActuacion;
    }
    
    public void setFechaActuacion(Date fechaActuacion) {
        this.fechaActuacion = fechaActuacion;
    }
}