package ar.com.cnpmweb.legalizaciondigital.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ar.com.cnpmweb.legalizaciondigital.model.enums.TipoDocumento;
import ar.com.cnpmweb.legalizaciondigital.model.enums.TipoNovedad;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "clientes")
public class Escribano {
    
    @Id
    @Column(name = "CliId")
    private Long cliId;
    
    @Column(name = "CliNombre")
    private String nombre;
    
    @Column(name = "CliApellido")
    private String apellido;
    
    @Column(name = "DocId")
    private Integer codigoTipoDocumento;
    
    // Campo transitorio que no se mapea a la base de datos
    @Transient
    private TipoDocumento tipoDocumento;
    
    @Column(name = "CliNumDocum")
    private String documento;
    
    @Column(name = "CliCuit")
    private String cuit;
    
    @Column(name = "CliCodArea1")
    private String codigoArea;
    
    @Column(name = "CliTelefono1")
    private String numeroTelefono;
    
    @Column(name = "CliEmail1")
    private String email;
    
    @Column(name = "CliCalle")
    private String calle;
    
    @Column(name = "CliNumDom")
    private String numeroCalle;
    
    @Column(name = "CliCategoria")
    private Integer categoria;

    //@Column(name = "activo")
    //private boolean activo;
    // Ver si es necesario agregar fetch=FetchType.EAGER
    @OneToMany(mappedBy = "escribano", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Antecedente> antecedentes = new ArrayList<>();

    public Long getCliId() {
        return cliId;
    }

    public void setCliId(Long cliId) {
        this.cliId = cliId;
    }

    public String getNombre() {
        return nombre != null ? nombre.trim() : null;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido != null ? apellido.trim() : null;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    // Método para obtener el tipo de documento como ENUM
    public TipoDocumento getTipoDocumento() {
        if (this.tipoDocumento == null && this.codigoTipoDocumento != null) {
            this.tipoDocumento = TipoDocumento.porCodigo(this.codigoTipoDocumento);
        }
        return this.tipoDocumento;
    }
    
    // Método para obtener la descripción del tipo de documento
    public String getDescripcionTipoDocumento() {
        TipoDocumento tipo = getTipoDocumento();
        return tipo != null ? tipo.getDescripcion() : null;
    }
    
    // Setter que convierte automáticamente
    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
        this.codigoTipoDocumento = tipoDocumento != null ? tipoDocumento.getCodigo() : null;
    }
    
    public void setCodigoTipoDocumento(Integer codigoTipoDocumento) {
        this.codigoTipoDocumento = codigoTipoDocumento;
        this.tipoDocumento = null; // Reset para que se recalcule la próxima vez
    }

    public String getDocumento() {
        return documento.trim();
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCuit() {
        return cuit.trim();
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getTelefono() {
        if (codigoArea != null && numeroTelefono != null) {
            return (codigoArea.trim() + "-" + numeroTelefono.trim()).trim();
        } else if (numeroTelefono != null) {
            return numeroTelefono.trim();
        }
        return null;
    }

    public void setTelefono(String telefono) {
        this.numeroTelefono = telefono;
    }

    public String getEmail() {
        return email.trim();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDomicilio() {
        if (calle != null && numeroCalle != null) {
            return (calle.trim() + " " + numeroCalle.trim()).trim();
        } else if (calle != null) {
            return calle.trim();
        }
        return null;
    }

    public void setDomicilio(String domicilio) {
        this.calle = domicilio;
    }

    public Integer getCategoria() {
        return categoria;
    }
    
    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    public List<Antecedente> getAntecedentes() {
        return antecedentes;
    }
    
    public void setAntecedentes(List<Antecedente> antecedentes) {
        this.antecedentes = antecedentes;
    }

    // Getter para activo como campo calculado
    @Transient
    public boolean isActivo() {
        if (antecedentes == null || antecedentes.isEmpty()) {
            return false;
        }
        
        // Códigos de novedades que indican que un escribano está activo
        List<Integer> codigosNovActivos = Arrays.asList(
            TipoNovedad.TITULAR.getCodigo(),
            TipoNovedad.ADSCRIPTO.getCodigo(),
            TipoNovedad.INTERINO.getCodigo()
        );
        
        Date fechaNula = null;
        try {
            // Crear una fecha que represente '0000-00-00'
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            fechaNula = sdf.parse("0000-00-00");
        } catch (ParseException e) {
            // En caso de error, usar la fecha mínima posible
            Calendar cal = Calendar.getInstance();
            cal.set(1, 1, 1);
            fechaNula = cal.getTime();
        }
        
        // La fecha nula final para comparar
        final Date finalFechaNula = fechaNula;
        
        // Buscar si existe algún antecedente que cumpla los criterios
        return antecedentes.stream()
            .anyMatch(antecedente -> 
                codigosNovActivos.contains(antecedente.getNovIdCodigo()) &&
                (antecedente.getFechaBaja() == null || 
                 antecedente.getFechaBaja().equals(finalFechaNula))
            );
    }

}