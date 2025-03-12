package ar.com.cnpmweb.legalizaciondigital.model;

import java.io.Serializable;
import java.util.Objects;

public class ContenidoId implements Serializable {
    private Integer idSucursal;
    private Integer idFactura;
    private Integer idRenglon;
    
    // Constructor por defecto requerido por JPA
    public ContenidoId() {
    }
    
    public ContenidoId(Integer idSucursal, Integer idFactura, Integer idRenglon) {
        this.idSucursal = idSucursal;
        this.idFactura = idFactura;
        this.idRenglon = idRenglon;
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
    
    // Implementación necesaria de equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContenidoId that = (ContenidoId) o;
        return Objects.equals(idSucursal, that.idSucursal) && 
               Objects.equals(idFactura, that.idFactura) &&
               Objects.equals(idRenglon, that.idRenglon);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idSucursal, idFactura, idRenglon);
    }
}