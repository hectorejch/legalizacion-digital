package ar.com.cnpmweb.legalizaciondigital.model;

import ar.com.cnpmweb.legalizaciondigital.model.enums.TipoFoja;
import jakarta.persistence.*;

@Entity
@Table(name = "contenido")
@IdClass(ContenidoId.class)
public class Contenido {
    
    @Id
    @Column(name = "IdSucursal")
    private Integer idSucursal;
    
    @Id
    @Column(name = "IdFactura")
    private Integer idFactura;
    
    @Id
    @Column(name = "IdRenglon")
    private Integer idRenglon;
    
    @Column(name = "IdProducto")
    private Integer idProducto;
    
    @Column(name = "IdSubProd")
    private Integer idSubProd;
    
    @Column(name = "PrimerNumero")
    private Integer primerNumero;
    
    @Column(name = "UltimoNumero")
    private Integer ultimoNumero;
    
    @Column(name = "PrimerNumeroReg")
    private Integer primerNumeroReg;
    
    @Column(name = "UltimoNumeroReg")
    private Integer ultimoNumeroReg;
    
    @Column(name = "CliId")
    private Integer cliId;
    
    @Column(name = "NumRegistro")
    private Integer numRegistro;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "IdSucursal", referencedColumnName = "IdSucursal", insertable = false, updatable = false),
        @JoinColumn(name = "IdFactura", referencedColumnName = "IdFactura", insertable = false, updatable = false)
    })
    private Factura factura;

    @Transient
    public TipoFoja getTipoFoja() {
        return TipoFoja.porProductoYSubProducto(this.idProducto, this.idSubProd);
    }
    
    // Getters y setters
    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    public Integer getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public Integer getIdRenglon() {
        return idRenglon;
    }

    public void setIdRenglon(Integer idRenglon) {
        this.idRenglon = idRenglon;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getIdSubProd() {
        return idSubProd;
    }

    public void setIdSubProd(Integer idSubProd) {
        this.idSubProd = idSubProd;
    }

    public Integer getPrimerNumero() {
        return primerNumero;
    }

    public void setPrimerNumero(Integer primerNumero) {
        this.primerNumero = primerNumero;
    }

    public Integer getUltimoNumero() {
        return ultimoNumero;
    }

    public void setUltimoNumero(Integer ultimoNumero) {
        this.ultimoNumero = ultimoNumero;
    }

    public Integer getPrimerNumeroReg() {
        return primerNumeroReg;
    }

    public void setPrimerNumeroReg(Integer primerNumeroReg) {
        this.primerNumeroReg = primerNumeroReg;
    }

    public Integer getUltimoNumeroReg() {
        return ultimoNumeroReg;
    }

    public void setUltimoNumeroReg(Integer ultimoNumeroReg) {
        this.ultimoNumeroReg = ultimoNumeroReg;
    }

    public Integer getCliId() {
        return cliId;
    }

    public void setCliId(Integer cliId) {
        this.cliId = cliId;
    }

    public Integer getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(Integer numRegistro) {
        this.numRegistro = numRegistro;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }
}