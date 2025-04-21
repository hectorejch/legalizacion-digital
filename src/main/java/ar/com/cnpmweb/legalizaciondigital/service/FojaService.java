package ar.com.cnpmweb.legalizaciondigital.service;

import ar.com.cnpmweb.legalizaciondigital.dto.VerificarFojaRequest;
import ar.com.cnpmweb.legalizaciondigital.dto.VerificarFojaResponse;
import ar.com.cnpmweb.legalizaciondigital.dto.EscribanoHabilitadoDTO;
import ar.com.cnpmweb.legalizaciondigital.model.Contenido;
import ar.com.cnpmweb.legalizaciondigital.model.enums.TipoFoja;
import ar.com.cnpmweb.legalizaciondigital.repository.ContenidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FojaService {
    private static final Logger logger = LoggerFactory.getLogger(FojaService.class);

    @Autowired
    private ContenidoRepository contenidoRepository;

    @Autowired
    private EscribanoService escribanoService;

    /**
     * Verifica una foja y busca los escribanos habilitados para firmarla.
     */
    public VerificarFojaResponse verificarFoja(VerificarFojaRequest request) {
        // Normalizo la fecha de actuación para que sea a las 00:00:00
        // de lo contrario me resta 3 horas por el huso horario y me cambia de día
        Date fechaNormalizada = normalizarFecha(request.getFechaActuacion());

        logger.info("Verificando foja tipo: {}, numero: {}, fecha: {}",
                request.getTipoFoja(), request.getNumeroFoja(), fechaNormalizada);

        VerificarFojaResponse response = new VerificarFojaResponse();

        // Establecer los datos originales en la respuesta
        response.setTipoFoja(request.getTipoFoja());
        response.setNumeroFoja(request.getNumeroFoja());
        response.setFechaActuacion(fechaNormalizada);

        // Validar tipo de foja
        String tipoFoja = request.getTipoFoja();
        if (!Arrays.asList("B", "C", "D", "E").contains(tipoFoja)) {
            response.setCodigo("ERROR_TIPO_FOJA");
            response.setMensaje("Tipo de foja no válido. Debe ser B, C, D o E.");
            return response;
        }

        // Obtener productos y subproductos según el tipo de foja
        List<Map<String, Integer>> productosSubproductos = obtenerProductosSubproductos(tipoFoja);

        // Lista para almacenar todos los contenidos encontrados
        List<Contenido> contenidosEncontrados = new ArrayList<>();

        // Tipos de comprobante válidos
        List<Integer> tiposComprobante = Arrays.asList(1, 2);

        // Buscar contenidos que coincidan con los criterios
        // Se hace un for porque en el caso de CONCUERDA hay dos subproductos distintos
        for (Map<String, Integer> ps : productosSubproductos) {
            Integer idProducto = ps.get("idProducto");
            Integer idSubProducto = ps.get("idSubProducto");

            List<Contenido> contenidos = contenidoRepository.findByTipoFojaAndNumero(
                    idProducto,
                    idSubProducto,
                    request.getNumeroFoja(),
                    tiposComprobante,
                    fechaNormalizada); // Uso fecha normalizada

            contenidosEncontrados.addAll(contenidos);
        }

        // Verificar si no se encontraron contenidos
        if (contenidosEncontrados.isEmpty()) {
            response.setCodigo("ERROR_FOJA_NO_ENCONTRADA");
            response.setMensaje("No se encontró ninguna foja que coincida con los criterios especificados.");
            return response;
        }

        // Verificar si se encontró más de un contenido
        if (contenidosEncontrados.size() > 1) {
            response.setCodigo("ERROR_MULTIPLES_FOJAS");
            response.setMensaje("Se encontraron múltiples fojas que coinciden con los criterios especificados.");
            return response;
        }

        // Obtener el contenido encontrado y su número de registro
        Contenido contenido = contenidosEncontrados.get(0);
        Integer numRegistro = contenido.getNumRegistro();

        // Buscar escribanos habilitados para el registro en la fecha de actuación
        List<EscribanoHabilitadoDTO> escribanosHabilitados = escribanoService
                .buscarEscribanosHabilitadosPorRegistro(numRegistro, fechaNormalizada);

        // Verificar si no hay escribanos habilitados
        if (escribanosHabilitados.isEmpty()) {
            response.setCodigo("ERROR_SIN_ESCRIBANOS_HABILITADOS");
            response.setMensaje("No hay escribanos habilitados para firmar esta foja en la fecha indicada.");
            return response;
        }

        // Todo está correcto, establecer la respuesta final
        response.setCodigo("OK");
        response.setMensaje("Verificación exitosa");
        response.setEscribanosHabilitados(escribanosHabilitados);

        return response;
    }

// Método helper para normalizar la fecha (eliminar hora/minutos/segundos)
private Date normalizarFecha(Date fecha) {
    if (fecha == null) {
        return null;
    }
    
    // Usamos Calendar para manipular la fecha
    Calendar cal = Calendar.getInstance();
    cal.setTime(fecha);
    
    // Establecer hora, minutos, segundos y milisegundos a cero
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    
    return cal.getTime();
}


    /**
     * Obtiene los pares de idProducto e idSubProducto correspondientes al tipo de
     * foja.
     */
    private List<Map<String, Integer>> obtenerProductosSubproductos(String tipoFojaCodigo) {
        List<Map<String, Integer>> resultado = new ArrayList<>();

        // Convertir el código de tipo de foja a mayúsculas para estandarizar
        tipoFojaCodigo = tipoFojaCodigo.toUpperCase();

        switch (tipoFojaCodigo) {
            case "B":
                // B_ACTUACION_NOTARIAL
                TipoFoja tipoFojaB = TipoFoja.B_ACTUACION_NOTARIAL;
                Map<String, Integer> productoB = new HashMap<>();
                productoB.put("idProducto", tipoFojaB.getIdProducto());
                productoB.put("idSubProducto", tipoFojaB.getIdSubProducto());
                resultado.add(productoB);
                break;

            case "C":
                // Buscar todos los tipos de foja que empiezan con C_
                for (TipoFoja tipoFoja : TipoFoja.values()) {
                    if (tipoFoja.getCodigo().startsWith("C")) {
                        Map<String, Integer> producto = new HashMap<>();
                        producto.put("idProducto", tipoFoja.getIdProducto());
                        producto.put("idSubProducto", tipoFoja.getIdSubProducto());
                        resultado.add(producto);
                    }
                }
                break;

            case "D":
                // D_CERTIFICACION_FOTOCOPIA
                TipoFoja tipoFojaD = TipoFoja.D_CERTIFICACION_FOTOCOPIA;
                Map<String, Integer> productoD = new HashMap<>();
                productoD.put("idProducto", tipoFojaD.getIdProducto());
                productoD.put("idSubProducto", tipoFojaD.getIdSubProducto());
                resultado.add(productoD);
                break;

            case "E":
                // E_CERTIFICACION_FIRMA
                TipoFoja tipoFojaE = TipoFoja.E_CERTIFICACION_FIRMA;
                Map<String, Integer> productoE = new HashMap<>();
                productoE.put("idProducto", tipoFojaE.getIdProducto());
                productoE.put("idSubProducto", tipoFojaE.getIdSubProducto());
                resultado.add(productoE);
                break;
        }

        return resultado;
    }
}