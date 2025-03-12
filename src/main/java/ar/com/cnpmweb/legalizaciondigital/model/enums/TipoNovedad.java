package ar.com.cnpmweb.legalizaciondigital.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum TipoNovedad {
    TITULAR(6, "Titular"),
    ADSCRIPTO(7, "Adscripto"),
    INTERINO(18, "Interino"),
    LICENCIA(17, "Licencia"),
    MATRICULADO(1, "Matriculado"),
    SUSPENSION(10, "Suspension"),
    ETICA_18A(19, "Tribunal de Etica - Art 18 Inc a"),
    ETICA_18B(20, "Tribunal de Etica - Art 18 Inc b"),
    ETICA_18C(21, "Tribunal de Etica - Art 18 Inc c"),
    ETICA_18D(22, "Tribunal de Etica - Art 18 Inc d"),
    ETICA_18E(23, "Tribunal de Etica - Art 18 Inc e"),
    ETICA_18F(24, "Tribunal de Etica - Art 18 Inc f"),
    ETICA_19(25, "Tribunal de Etica - Art 19"),
    NO_DEFINIDO(0, "NO DEFINIDO"),;


    private final int codigo;
    private final String descripcion;
    private static final Map<Integer, TipoNovedad> CODIGO_MAP = new HashMap<>();

    static {
        for (TipoNovedad novedad : values()) {
            CODIGO_MAP.put(novedad.codigo, novedad);
        }
    }

    TipoNovedad(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoNovedad porCodigo(Integer codigo) {
        if (codigo == null) {
            return NO_DEFINIDO;
        }
        return CODIGO_MAP.getOrDefault(codigo, NO_DEFINIDO);
    }
}