package ar.com.cnpmweb.legalizaciondigital.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum CaracterEscribano {
    TITULAR(1, "Titular"),
    ADSCRIPTO_1(2, "Adscripto 1"),
    ADSCRIPTO_2(3, "Adscripto 2"),
    MATRICULADO(4, "Matriculado"),
    REGENTE(5, "Regente"),
    JUBILADO(6, "Jubilado"),
    FALLECIDO(7, "Fallecido"),
    BAJA(8, "Baja"),
    INTERINO(9, "Interino"),
    SUPLENTE(10, "Suplente"),
    REGENTE_INTERINO(11, "Regente Interino"),
    NO_DEFINIDO(0, "No Definido");

    private final int codigo;
    private final String descripcion;
    private static final Map<Integer, CaracterEscribano> CODIGO_MAP = new HashMap<>();

    static {
        for (CaracterEscribano caracter : values()) {
            CODIGO_MAP.put(caracter.codigo, caracter);
        }
    }

    CaracterEscribano(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static CaracterEscribano porCodigo(Integer codigo) {
        if (codigo == null) {
            return NO_DEFINIDO;
        }
        return CODIGO_MAP.getOrDefault(codigo, NO_DEFINIDO);
    }
}