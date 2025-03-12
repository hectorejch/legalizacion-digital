package ar.com.cnpmweb.legalizaciondigital.model;

import java.io.Serializable;
import java.util.Objects;

public class FacturaId implements Serializable {
    private Integer idFactura;
    private Integer idSucursal;
    
    // Constructor por defecto requerido por JPA
    public FacturaId() {
    }
    
    public FacturaId(Integer idSucursal, Integer idFactura) {
        this.idSucursal = idSucursal;
        this.idFactura = idFactura;
    }
    
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
    
    // Implementación necesaria de equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FacturaId that = (FacturaId) o;
        return Objects.equals(idFactura, that.idFactura) && 
               Objects.equals(idSucursal, that.idSucursal);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idFactura, idSucursal);
    }
}