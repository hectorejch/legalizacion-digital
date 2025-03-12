package ar.com.cnpmweb.legalizaciondigital.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum TipoDocumento {
    DNI(96, "DNI"),
    LE(89, "Libreta de Enrolamiento"),
    LC(90, "Libreta Cívica"),
    CI(0, "Cédula de Identidad"),
    PASAPORTE(94, "Pasaporte"),
    OTRO(99, "Otro");

    private final int codigo;
    private final String descripcion;
    private static final Map<Integer, TipoDocumento> CODIGO_MAP = new HashMap<>();

    static {
        for (TipoDocumento tipo : values()) {
            CODIGO_MAP.put(tipo.codigo, tipo);
        }
    }

    TipoDocumento(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoDocumento porCodigo(Integer codigo) {
        if (codigo == null) {
            return OTRO;
        }
        return CODIGO_MAP.getOrDefault(codigo, OTRO);
    }
}