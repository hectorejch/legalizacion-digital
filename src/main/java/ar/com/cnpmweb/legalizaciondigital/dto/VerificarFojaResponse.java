package ar.com.cnpmweb.legalizaciondigital.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VerificarFojaResponse {
    private String codigo;
    private String mensaje;
    private String tipoFoja;
    private Integer numeroFoja;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "America/Argentina/Buenos_Aires")
    private Date fechaActuacion;
    private List<EscribanoHabilitadoDTO> escribanosHabilitados;

    // Getters y setters
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

    public List<EscribanoHabilitadoDTO> getEscribanosHabilitados() {
        return escribanosHabilitados;
    }

    public void setEscribanosHabilitados(List<EscribanoHabilitadoDTO> escribanosHabilitados) {
        this.escribanosHabilitados = escribanosHabilitados;
    }
}