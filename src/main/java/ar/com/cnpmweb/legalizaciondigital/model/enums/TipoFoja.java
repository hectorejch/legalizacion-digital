package ar.com.cnpmweb.legalizaciondigital.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum TipoFoja {
    A_LEGALIZACION(28, 1, "Legalización", "A"),
    U_LEGALIZACION_URGENTE(28, 10, "Legalización Urgente", "U"),
    B_ACTUACION_NOTARIAL(4, 2, "Actuación Notarial", "B"),
    B_PROTOCOLO_NOTARIAL(2, 2, "Protocolo Notarial", "BP"),
    C_CONCUERDA(15, 1, "Concuerda", "C"),
    C_CONCUERDA_OFICIO(15, 2, "Concuerda Oficio", "CO"),
    D_CERTIFICACION_FOTOCOPIA(16, 1, "Certificación de fotocopias", "D"),
    E_CERTIFICACION_FIRMA(17, 1, "Certificación de firmas", "E");

    private final Integer idProducto;
    private final Integer idSubProducto;
    private final String descripcion;
    private final String codigo;
    
    // Mapas para búsqueda rápida
    private static final Map<String, TipoFoja> CODIGO_MAP = new HashMap<>();
    private static final Map<String, TipoFoja> PRODUCTO_SUBPRODUCTO_MAP = new HashMap<>();
    
    static {
        for (TipoFoja tipoFoja : values()) {
            CODIGO_MAP.put(tipoFoja.codigo, tipoFoja);
            PRODUCTO_SUBPRODUCTO_MAP.put(tipoFoja.idProducto + "_" + tipoFoja.idSubProducto, tipoFoja);
        }
    }

    TipoFoja(Integer idProducto, Integer idSubProducto, String descripcion, String codigo) {
        this.idProducto = idProducto;
        this.idSubProducto = idSubProducto;
        this.descripcion = descripcion;
        this.codigo = codigo;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public Integer getIdSubProducto() {
        return idSubProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCodigo() {
        return codigo;
    }
    
    // Método para buscar por código
    public static TipoFoja porCodigo(String codigo) {
        return CODIGO_MAP.getOrDefault(codigo, null);
    }
    
    // Método para buscar por producto y subproducto
    public static TipoFoja porProductoYSubProducto(Integer idProducto, Integer idSubProducto) {
        return PRODUCTO_SUBPRODUCTO_MAP.getOrDefault(idProducto + "_" + idSubProducto, null);
    }
}