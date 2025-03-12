package ar.com.cnpmweb.legalizaciondigital.model;

import java.io.Serializable;
import java.util.Objects;

public class AntecedenteId implements Serializable {
    private Integer antId;
    private Integer cliId;
    
    // Constructor por defecto requerido por JPA
    public AntecedenteId() {
    }
    
    public AntecedenteId(Integer antId, Integer cliId) {
        this.antId = antId;
        this.cliId = cliId;
    }
    
    // Getters y setters
    public Integer getAntId() {
        return antId;
    }
    
    public void setAntId(Integer antId) {
        this.antId = antId;
    }
    
    public Integer getCliId() {
        return cliId;
    }
    
    public void setCliId(Integer cliId) {
        this.cliId = cliId;
    }
    
    // Implementación necesaria de equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AntecedenteId that = (AntecedenteId) o;
        return Objects.equals(antId, that.antId) && 
               Objects.equals(cliId, that.cliId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(antId, cliId);
    }
}