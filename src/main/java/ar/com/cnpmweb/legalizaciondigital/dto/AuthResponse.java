package ar.com.cnpmweb.legalizaciondigital.dto;

import java.util.List;

public class AuthResponse {
    private String codigo; // "1" autorizado y habilitado, "2" autorizado pero no habilitado, "3" no
                           // autorizado
    private String mensaje;
    private Long matricula;
    private String codigoSuplencia; // "1" hay suplencias disponibles, "2" no hay suplencias disponibles
    private List<EscribanoHabilitadoDTO> suplenciasDisponibles;

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

    public Long getMatricula() {
        return matricula;
    }

    public void setMatricula(Long matricula) {
        this.matricula = matricula;
    }

    public String getCodigoSuplencia() {
        return codigoSuplencia;
    }

    public void setCodigoSuplencia(String codigoSuplencia) {
        this.codigoSuplencia = codigoSuplencia;
    }

    public List<EscribanoHabilitadoDTO> getSuplenciasDisponibles() {
        return suplenciasDisponibles;
    }

    public void setSuplenciasDisponibles(List<EscribanoHabilitadoDTO> suplenciasDisponibles) {
        this.suplenciasDisponibles = suplenciasDisponibles;
    }
}