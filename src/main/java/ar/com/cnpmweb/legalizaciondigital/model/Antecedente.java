package ar.com.cnpmweb.legalizaciondigital.model;

import ar.com.cnpmweb.legalizaciondigital.model.enums.CaracterEscribano;
import ar.com.cnpmweb.legalizaciondigital.model.enums.TipoNovedad;
import ar.com.cnpmweb.legalizaciondigital.repository.EscribanoRepository;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cliantecedentes")
@IdClass(AntecedenteId.class) // Clase para la clave primaria compuesta
public class Antecedente {

    @Id
    @Column(name = "AntId")
    private Integer antId;

    @Id
    @Column(name = "CliId")
    private Integer cliId;

    @Column(name = "NovId")
    private Integer novIdCodigo;

    @Transient
    private TipoNovedad tipoNovedad;

    @Column(name = "CliNumRegistro")
    private Integer numRegistro;

    @Column(name = "CliCaracter")
    private Integer caracterCodigo;

    @Transient
    private CaracterEscribano caracter;

    @Column(name = "CliFecAlta")
    @Temporal(TemporalType.DATE)
    private Date fechaAlta;

    @Column(name = "CliFecBaja")
    @Temporal(TemporalType.DATE)
    private Date fechaBaja;

    @Column(name = "CliSuplente")
    private Integer cliSuplente;

    // Relación con la entidad Escribano (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CliId", insertable = false, updatable = false)
    private Escribano escribano;

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

    public Integer getNovIdCodigo() {
        return novIdCodigo;
    }

    public void setNovIdCodigo(Integer novIdCodigo) {
        this.novIdCodigo = novIdCodigo;
        this.tipoNovedad = null; // Reset para que se recalcule
    }

    public TipoNovedad getTipoNovedad() {
        if (tipoNovedad == null && novIdCodigo != null) {
            tipoNovedad = TipoNovedad.porCodigo(novIdCodigo);
        }
        return tipoNovedad;
    }

    public void setTipoNovedad(TipoNovedad tipoNovedad) {
        this.tipoNovedad = tipoNovedad;
        if (tipoNovedad != null) {
            this.novIdCodigo = tipoNovedad.getCodigo();
        }
    }

    public Integer getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(Integer numRegistro) {
        this.numRegistro = numRegistro;
    }

    public Integer getCaracterCodigo() {
        return caracterCodigo;
    }

    public void setCaracterCodigo(Integer caracterCodigo) {
        this.caracterCodigo = caracterCodigo;
        this.caracter = null; // Reset para que se recalcule
    }

    public CaracterEscribano getCaracter() {
        if (caracter == null && caracterCodigo != null) {
            caracter = CaracterEscribano.porCodigo(caracterCodigo);
        }
        return caracter;
    }

    public void setCaracter(CaracterEscribano caracter) {
        this.caracter = caracter;
        if (caracter != null) {
            this.caracterCodigo = caracter.getCodigo();
        }
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Escribano getEscribano() {
        return escribano;
    }

    public void setEscribano(Escribano escribano) {
        this.escribano = escribano;
    }

    public Integer getCliSuplente() {
        return cliSuplente;
    }

    public void setCliSuplente(Integer cliSuplente) {
        this.cliSuplente = cliSuplente;
    }

    @Transient
    public Escribano getEscribanoSuplente(EscribanoRepository escribanoRepository) {
        if (cliSuplente != null && cliSuplente > 0) {
            return escribanoRepository.findEscribanoById(cliSuplente.longValue()).orElse(null);
        }
        return null;
    }
}