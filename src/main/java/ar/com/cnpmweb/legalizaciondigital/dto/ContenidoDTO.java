package ar.com.cnpmweb.legalizaciondigital.dto;

public class ContenidoDTO {
    private Integer idSucursal;
    private Integer idFactura;
    private Integer idRenglon;
    private Integer idProducto;
    private Integer idSubProd;
    private Integer primerNumero;
    private Integer ultimoNumero;
    private Integer primerNumeroReg;
    private Integer ultimoNumeroReg;
    private Integer cliId;
    private Integer numRegistro;
    private String tipoFojaDescripcion;
    private String tipoFojaCodigo;
    
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

    public String getTipoFojaDescripcion() {
        return tipoFojaDescripcion;
    }
    
    public void setTipoFojaDescripcion(String tipoFojaDescripcion) {
        this.tipoFojaDescripcion = tipoFojaDescripcion;
    }
    
    public String getTipoFojaCodigo() {
        return tipoFojaCodigo;
    }
    
    public void setTipoFojaCodigo(String tipoFojaCodigo) {
        this.tipoFojaCodigo = tipoFojaCodigo;
    }
}