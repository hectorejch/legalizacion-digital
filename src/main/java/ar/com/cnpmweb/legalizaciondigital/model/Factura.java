package ar.com.cnpmweb.legalizaciondigital.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facturas")
@IdClass(FacturaId.class)
public class Factura {
    
    @Id
    @Column(name = "IdFactura")
    private Integer idFactura;
    
    @Id
    @Column(name = "IdSucursal")
    private Integer idSucursal;
    
    @Column(name = "CliId")
    private Integer cliId;
    
    @Column(name = "CliApellido")
    private String cliApellido;
    
    @Column(name = "CliNombre")
    private String cliNombre;
    
    @Column(name = "TipoComprobante")
    private Integer tipoComprobante;
    
    @Column(name = "FechaComprobante")
    @Temporal(TemporalType.DATE)
    private Date fechaComprobante;
    
    @Column(name = "NumRegistro")
    private Integer numRegistro;
    
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contenido> contenidos = new ArrayList<>();
    
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

    public List<Contenido> getContenidos() {
        return contenidos;
    }

    public void setContenidos(List<Contenido> contenidos) {
        this.contenidos = contenidos;
    }
}