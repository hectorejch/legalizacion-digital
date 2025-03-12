package ar.com.cnpmweb.legalizaciondigital.dto;

import java.util.Date;
import java.util.List;

public class FacturaDTO {
    private Integer idFactura;
    private Integer idSucursal;
    private Integer cliId;
    private String cliApellido;
    private String cliNombre;
    private Integer tipoComprobante;
    private Date fechaComprobante;
    private Integer numRegistro;
    private List<ContenidoDTO> contenidos;
    
    // Getters y setters
    public Integer getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    public Integer getCliId() {
        return cliId;
    }

    public void setCliId(Integer cliId) {
        this.cliId = cliId;
    }

    public String getCliApellido() {
        return cliApellido;
    }

    public void setCliApellido(String cliApellido) {
        this.cliApellido = cliApellido;
    }

    public String getCliNombre() {
        return cliNombre;
    }

    public void setCliNombre(String cliNombre) {
        this.cliNombre = cliNombre;
    }

    public Integer getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(Integer tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public Date getFechaComprobante() {
        return fechaComprobante;
    }

    public void setFechaComprobante(Date fechaComprobante) {
        this.fechaComprobante = fechaComprobante;
    }

    public Integer getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(Integer numRegistro) {
        this.numRegistro = numRegistro;
    }

    public List<ContenidoDTO> getContenidos() {
        return contenidos;
    }

    public void setContenidos(List<ContenidoDTO> contenidos) {
        this.contenidos = contenidos;
    }
}