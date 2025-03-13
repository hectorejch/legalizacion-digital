package ar.com.cnpmweb.legalizaciondigital.service;

import ar.com.cnpmweb.legalizaciondigital.dto.EscribanoDTO;
import ar.com.cnpmweb.legalizaciondigital.dto.MatriculaHabilitadaDTO;
import ar.com.cnpmweb.legalizaciondigital.dto.EscribanoHabilitadoDTO;
import ar.com.cnpmweb.legalizaciondigital.model.Antecedente;
import ar.com.cnpmweb.legalizaciondigital.model.Escribano;
import ar.com.cnpmweb.legalizaciondigital.model.enums.CaracterEscribano;
import ar.com.cnpmweb.legalizaciondigital.model.enums.TipoNovedad;
import ar.com.cnpmweb.legalizaciondigital.repository.EscribanoRepository;
import ar.com.cnpmweb.legalizaciondigital.repository.AntecedenteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EscribanoService {
    private static final Logger logger = LoggerFactory.getLogger(EscribanoService.class);

    @Autowired
    private EscribanoRepository escribanoRepository;

    @Autowired
    private AntecedenteRepository antecedenteRepository;

    public List<EscribanoDTO> getAllEscribanos(boolean incluirInactivos) {
        logger.debug("Obteniendo todos los escribanos con categoría = 1");

        List<Escribano> escribanos = escribanoRepository.findAllEscribanos(); // Ya está filtrado por categoría=1

        // Si solo queremos activos, filtramos por el campo calculado isActivo()
        if (!incluirInactivos) {
            escribanos = escribanos.stream()
                    .filter(Escribano::isActivo)
                    .collect(Collectors.toList());
        }

        return escribanos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Verifica si un escribano estaba habilitado en una fecha específica,
     * controlando antecedentes habilitantes e inhabilites, y licencias activas.
     * Códigos que puede retornar:
     * - MATRICULA_NO_ENCONTRADA
     * - MATRICULA_VALIDA
     * - MATRICULA_NO_HABILITADA
     * - ESCRIBANO_EN_LICENCIA
     */
    public MatriculaHabilitadaDTO verificarMatriculaHabilitadaEnFecha(Long cliId, Date fecha) {
        
        // Normalizo la fecha ingresada para fijar la hora en 00:00:00
        // de lo contrario tengo problemas al comparar con las fechas de inicio y fin
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        fecha = calendar.getTime();
        logger.info("Verificando si la matrícula {} estaba habilitada en la fecha {}", cliId, fecha);

        MatriculaHabilitadaDTO resultado = new MatriculaHabilitadaDTO();

        Optional<Escribano> escribanoOpt = escribanoRepository.findEscribanoById(cliId);

        if (!escribanoOpt.isPresent()) {
            logger.warn("No se encontró el escribano con ID: {}", cliId);
            resultado.setCodigo("MATRICULA_NO_ENCONTRADA");
            resultado.setMensaje("Escribano no encontrado con la matrícula: " + cliId);
            return resultado;
        }

        Escribano escribano = escribanoOpt.get();

        // Buscamos el antecedente habilitante para la fecha dada
        // Tiene que ser un autorizante activo en esa fecha
        Antecedente antecedenteHabilitante = encontrarAntecedenteHabilitanteEnFecha(escribano, fecha);

        // verificarAntecedentesHabilitantesEnFecha hace lo mismo que encontrarAntecedentesHabilitantesEnFecha
        // pero devuelve un booleano en lugar de un objeto Antecedente
        // boolean estabaHabilitado = verificarAntecedentesHabilitantesEnFecha(escribano, fecha);
        boolean estabaHabilitado = antecedenteHabilitante != null;

        // Verificar si estaba inhabilitado por tener sanciones éticas vigentes en esa
        // fecha
        boolean estabaInhabilitado = verificarAntecedentesInhabilitantesEnFecha(escribano, fecha);

        // Verificar si el escribano tiene una licencia activa en la fecha indicada
        boolean tieneLicenciaActiva = verificarLicenciaActivaEnFecha(escribano, fecha);

        // El escribano estaba habilitado solo si tenía antecedentes habilitantes y no
        // tenía inhabilitaciones
        boolean habilitadoFinal = estabaHabilitado && !estabaInhabilitado;

        if (habilitadoFinal) {
            if (tieneLicenciaActiva) {
                resultado.setCodigo("ESCRIBANO_EN_LICENCIA");
                resultado.setMensaje("El escribano tiene una licencia activa en la fecha indicada");
            } else {
                resultado.setCodigo("MATRICULA_VALIDA");
                resultado.setMensaje("HABILITADO");
                // Agregar la información del número de registro y carácter
                if (antecedenteHabilitante != null) {
                    resultado.setNumRegistro(antecedenteHabilitante.getNumRegistro());

                    // Obtener la descripción del carácter en lugar del código
                    CaracterEscribano caracter = antecedenteHabilitante.getCaracter();
                    resultado.setCaracter(caracter != null ? caracter.getDescripcion() : null);
                }
            }
        } else {
            resultado.setCodigo("MATRICULA_NO_HABILITADA");
            resultado.setMensaje("INHABILITADO");
        }

        logger.info("Matrícula {} en fecha {}: {}", cliId, fecha, resultado.getMensaje());
        return resultado;
    }

    private boolean verificarLicenciaActivaEnFecha(Escribano escribano, Date fecha) {
        // Buscar antecedentes de tipo LICENCIA donde la fecha esté en el rango
        for (Antecedente antecedente : escribano.getAntecedentes()) {
            // Verificar si es de tipo LICENCIA
            Integer novIdCodigo = antecedente.getNovIdCodigo();
            if (novIdCodigo != null && novIdCodigo.equals(TipoNovedad.LICENCIA.getCodigo())) {
                Date fechaAlta = antecedente.getFechaAlta();
                Date fechaBaja = antecedente.getFechaBaja();
                
                // Verificar si la fecha está dentro del rango
                if (fechaEstaDentroDeRango(fecha, fechaAlta, fechaBaja)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Antecedente encontrarAntecedenteHabilitanteEnFecha(Escribano escribano, Date fecha) {
        List<Integer> codigosNovHabilitantes = Arrays.asList(
                TipoNovedad.TITULAR.getCodigo(),
                TipoNovedad.ADSCRIPTO.getCodigo(),
                TipoNovedad.INTERINO.getCodigo());

        // Usar iteración simple para evitar problemas con las fechas nulas
        for (Antecedente antecedente : escribano.getAntecedentes()) {
            if (codigosNovHabilitantes.contains(antecedente.getNovIdCodigo())) {
                // Verificar si la fecha estaba dentro del período válido del antecedente
                Date fechaAlta = antecedente.getFechaAlta();
                Date fechaBaja = antecedente.getFechaBaja();

                if (fechaAlta != null) {
                    // La fecha de consulta debe ser igual o posterior a la fecha de alta
                    if (fecha.compareTo(fechaAlta) >= 0) {
                        // Si la fecha de baja es nula o es posterior a la fecha consultada
                        if (fechaBaja == null || fecha.compareTo(fechaBaja) <= 0) {
                            return antecedente; // Devolvemos el antecedente encontrado
                        }
                    }
                }
            }
        }

        return null;
    }

    private boolean verificarAntecedentesHabilitantesEnFecha(Escribano escribano, Date fecha) {
        List<Integer> codigosNovHabilitantes = Arrays.asList(
                TipoNovedad.TITULAR.getCodigo(),
                TipoNovedad.ADSCRIPTO.getCodigo(),
                TipoNovedad.INTERINO.getCodigo());

        // Usar iteración simple para evitar problemas con las fechas nulas
        for (Antecedente antecedente : escribano.getAntecedentes()) {
            if (codigosNovHabilitantes.contains(antecedente.getNovIdCodigo())) {
                // Verificar si la fecha estaba dentro del período válido del antecedente
                Date fechaAlta = antecedente.getFechaAlta();
                Date fechaBaja = antecedente.getFechaBaja();

                if (fechaAlta != null) {
                    // La fecha de consulta debe ser igual o posterior a la fecha de alta
                    if (fecha.compareTo(fechaAlta) >= 0) {
                        // Si la fecha de baja es nula o es posterior a la fecha consultada
                        if (fechaBaja == null || fecha.compareTo(fechaBaja) <= 0) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean verificarAntecedentesInhabilitantesEnFecha(Escribano escribano, Date fecha) {
        List<Integer> codigosNovInhabilitantes = Arrays.asList(
                TipoNovedad.SUSPENSION.getCodigo());

        // Usar iteración simple para evitar problemas con las fechas nulas
        for (Antecedente antecedente : escribano.getAntecedentes()) {
            if (codigosNovInhabilitantes.contains(antecedente.getNovIdCodigo())) {
                // Verificar si la fecha estaba dentro del período de sanción ética
                Date fechaAlta = antecedente.getFechaAlta();
                Date fechaBaja = antecedente.getFechaBaja();

                if (fechaAlta != null) {
                    // La fecha de consulta debe ser igual o posterior a la fecha de alta
                    if (fecha.compareTo(fechaAlta) >= 0) {
                        // Si la fecha de baja es nula o es posterior a la fecha consultada
                        if (fechaBaja == null || fecha.compareTo(fechaBaja) <= 0) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Busca si un escribano tiene un suplente activo en una fecha específica.
     * Versión que recibe el ID de la matrícula.
     * 
     * @param matricula La matrícula del escribano a suplir
     * @param fecha     La fecha en la que se busca la suplencia
     * @return EscribanoDTO con los datos del suplente si existe, null en caso
     *         contrario
     */
    public EscribanoDTO buscarSuplenteActivo(Long matricula, Date fecha) {
        logger.info("Buscando suplente activo para la matrícula {} en la fecha {}", matricula, fecha);

        Optional<Escribano> escribanoOpt = escribanoRepository.findEscribanoById(matricula);

        if (!escribanoOpt.isPresent()) {
            logger.warn("No se encontró el escribano con matrícula: {}", matricula);
            return null;
        }

        // Reutilizar el método que recibe una instancia de Escribano
        return buscarSuplenteActivo(escribanoOpt.get(), fecha);
    }

    /**
     * Busca si un escribano tiene un suplente activo en una fecha específica.
     * Versión que recibe directamente una instancia de Escribano.
     * 
     * @param escribano El escribano a verificar
     * @param fecha     La fecha en la que se busca la suplencia
     * @return EscribanoDTO con los datos del suplente si existe, null en caso
     *         contrario
     */
    public EscribanoDTO buscarSuplenteActivo(Escribano escribano, Date fecha) {
        logger.info("Buscando suplente activo para el escribano {} en la fecha {}",
                escribano.getCliId(), fecha);

        // Buscar antecedentes de tipo LICENCIA donde la fecha esté en el rango
        for (Antecedente antecedente : escribano.getAntecedentes()) {
            // Verificar si es de tipo LICENCIA
            Integer novIdCodigo = antecedente.getNovIdCodigo();
            if (novIdCodigo != null && novIdCodigo.equals(TipoNovedad.LICENCIA.getCodigo())) {
                Date fechaAlta = antecedente.getFechaAlta();
                Date fechaBaja = antecedente.getFechaBaja();

                // Verificar si la fecha está dentro del rango
                if (fechaEstaDentroDeRango(fecha, fechaAlta, fechaBaja)) {
                    // Verificar si tiene un suplente válido
                    Integer cliSuplente = antecedente.getCliSuplente();

                    if (cliSuplente != null && cliSuplente > 0) {
                        logger.info("Se encontró un suplente (ID: {}) para la matrícula {} en la fecha {}",
                                cliSuplente, escribano.getCliId(), fecha);

                        // Buscar los datos del suplente
                        Optional<Escribano> suplenteOpt = escribanoRepository
                                .findEscribanoById(cliSuplente.longValue());

                        if (suplenteOpt.isPresent()) {
                            // Convertir a DTO y agregar el número de registro
                            EscribanoDTO suplenteDTO = convertToDTO(suplenteOpt.get());
                            // Podemos agregar información adicional si es necesario
                            suplenteDTO.setNumeroRegistro(antecedente.getNumRegistro());
                            return suplenteDTO;
                        }
                    }
                }
            }
        }

        logger.info("No se encontró suplente activo para la matrícula {} en la fecha {}", escribano.getCliId(), fecha);
        return null;
    }

    /**
     * Busca todos los escribanos habilitados para firmar en un registro en una
     * fecha determinada.
     * 
     * @param numeroRegistro El número de registro a consultar
     * @param fecha          La fecha en la que se busca la habilitación
     * @return Lista de escribanos habilitados para firmar
     */
    public List<EscribanoHabilitadoDTO> buscarEscribanosHabilitadosPorRegistro(Integer numeroRegistro, Date fecha) {
        logger.info("Buscando escribanos habilitados para el registro {} en la fecha {}", numeroRegistro, fecha);

        // Lista de tipos de novedad que permiten firmar
        List<Integer> tiposNovedadHabilitantes = Arrays.asList(
                TipoNovedad.TITULAR.getCodigo(),
                TipoNovedad.ADSCRIPTO.getCodigo(),
                TipoNovedad.INTERINO.getCodigo(),
                TipoNovedad.LICENCIA.getCodigo());

        // Buscar antecedentes para el registro y fecha especificados
        List<Antecedente> antecedentes = antecedenteRepository.findByNumRegistroAndNovedades(
                numeroRegistro, tiposNovedadHabilitantes);

        // Set para almacenar IDs de escribanos que NO deben incluirse (los suplidos)
        Set<Long> escribanosExcluidos = new HashSet<>();

        // Lista para almacenar los escribanos habilitados (procesados al final)
        List<EscribanoHabilitadoDTO> escribanosHabilitados = new ArrayList<>();

        // Mapa temporal para almacenar la información de los escribanos por ID
        Map<Long, EscribanoHabilitadoDTO> mapaPorId = new HashMap<>();

        // Primero procesamos los antecedentes para identificar suplentes y excluidos
        for (Antecedente antecedente : antecedentes) {
            // Verificar si la fecha está dentro del rango
            if (fechaEstaDentroDeRango(fecha, antecedente.getFechaAlta(), antecedente.getFechaBaja())) {

                // Si es una licencia con suplente, excluir al escribano suplido
                if (antecedente.getNovIdCodigo().equals(TipoNovedad.LICENCIA.getCodigo())) {
                    Integer cliSuplente = antecedente.getCliSuplente();
                    if (cliSuplente != null && cliSuplente > 0) {
                        // Añadir el escribano suplido a la lista de excluidos
                        escribanosExcluidos.add(antecedente.getCliId().longValue());

                        // Buscar y añadir al suplente a la lista de habilitados
                        Optional<Escribano> suplenteOpt = escribanoRepository
                                .findEscribanoById(cliSuplente.longValue());
                        if (suplenteOpt.isPresent()) {
                            Escribano suplente = suplenteOpt.get();
                            EscribanoHabilitadoDTO dto = new EscribanoHabilitadoDTO();
                            dto.setMatricula(cliSuplente.longValue());
                            dto.setNombre(suplente.getNombre());
                            dto.setApellido(suplente.getApellido());

                            mapaPorId.put(dto.getMatricula(), dto);
                        }
                    }
                }
                // Para los demás tipos, añadir al escribano si no está en la lista de excluidos
                else {
                    Long idEscribano = antecedente.getCliId().longValue();
                    // No lo añadimos ahora, solo lo guardamos temporalmente para procesarlo al
                    // final
                    if (!mapaPorId.containsKey(idEscribano)) {
                        Optional<Escribano> escribanoOpt = escribanoRepository.findEscribanoById(idEscribano);
                        if (escribanoOpt.isPresent()) {
                            Escribano escribano = escribanoOpt.get();
                            EscribanoHabilitadoDTO dto = new EscribanoHabilitadoDTO();
                            dto.setMatricula(idEscribano);
                            dto.setNombre(escribano.getNombre());
                            dto.setApellido(escribano.getApellido());

                            mapaPorId.put(idEscribano, dto);
                        }
                    }
                }
            }
        }

        // Ahora procesamos el mapa para excluir a los escribanos suplidos
        for (Map.Entry<Long, EscribanoHabilitadoDTO> entry : mapaPorId.entrySet()) {
            if (!escribanosExcluidos.contains(entry.getKey())) {
                escribanosHabilitados.add(entry.getValue());
            }
        }

        logger.info("Se encontraron {} escribanos habilitados para el registro {} en la fecha {}",
                escribanosHabilitados.size(), numeroRegistro, fecha);

        return escribanosHabilitados;
    }

    /**
     * Verifica si una fecha está dentro de un rango dado (inclusive).
     * La fecha está en el rango si: fechaInicio <= fecha <= fechaFin
     */
    private boolean fechaEstaDentroDeRango(Date fecha, Date fechaInicio, Date fechaFin) {
        if (fecha == null || fechaInicio == null) {
            return false;
        }

        // Si la fecha de fin es nula, verificamos que la fecha sea posterior o igual a
        // la fecha inicio
        if (fechaFin == null) {
            // Utilizamos compareTo para incluir el caso de igualdad
            return fecha.compareTo(fechaInicio) >= 0;
        }
        // Verificar si la fecha está entre fechaInicio y fechaFin (inclusive)
        // fecha >= fechaInicio && fecha <= fechaFin
        return fecha.compareTo(fechaInicio) >= 0 && fecha.compareTo(fechaFin) <= 0;
    }

    /**
     * Busca si un escribano es suplente de otros en una fecha determinada.
     * 
     * @param escribanoSuplente El escribano que podría ser suplente
     * @param fecha             La fecha en la que se busca la suplencia
     * @return Lista de EscribanoHabilitadoDTO con los escribanos suplidos, vacía si
     *         no hay ninguno
     */
    public List<EscribanoHabilitadoDTO> buscarAQuienesSuple(Escribano escribanoSuplente, Date fecha) {
        logger.info("Verificando si el escribano {} es suplente de alguien en la fecha {}",
                escribanoSuplente.getCliId(), fecha);

        Long idSuplente = escribanoSuplente.getCliId();
        List<EscribanoHabilitadoDTO> escribanosSuplidos = new ArrayList<>();

        // Buscar en los antecedentes donde este escribano figure como suplente,
        // el tipo de novedad sea LICENCIA, ambas fechas estén definidas y la fecha esté
        // en el rango
        List<Antecedente> antecedentes = antecedenteRepository.findBySuplente(
                idSuplente.intValue(),
                TipoNovedad.LICENCIA.getCodigo(),
                fecha);

        // Procesar cada antecedente encontrado
        for (Antecedente antecedente : antecedentes) {
            Integer idSuplido = antecedente.getCliId();

            // Buscar los datos del escribano suplido
            Optional<Escribano> suplidoOpt = escribanoRepository.findEscribanoById(idSuplido.longValue());

            if (suplidoOpt.isPresent()) {
                logger.info("El escribano {} es suplente del escribano {} en la fecha {}",
                        idSuplente, idSuplido, fecha);

                // Convertir a EscribanoHabilitadoDTO y agregar a la lista
                Escribano suplido = suplidoOpt.get();
                EscribanoHabilitadoDTO suplidoDTO = new EscribanoHabilitadoDTO();
                suplidoDTO.setMatricula(suplido.getCliId());
                suplidoDTO.setNombre(suplido.getNombre());
                suplidoDTO.setApellido(suplido.getApellido());

                escribanosSuplidos.add(suplidoDTO);
            }
        }

        if (escribanosSuplidos.isEmpty()) {
            logger.info("El escribano {} no es suplente de nadie en la fecha {}", idSuplente, fecha);
        } else {
            logger.info("El escribano {} es suplente de {} escribanos en la fecha {}",
                    idSuplente, escribanosSuplidos.size(), fecha);
        }

        return escribanosSuplidos;
    }

    /**
     * Sobrecarga del método que recibe la matrícula en lugar del objeto Escribano.
     */
    public List<EscribanoHabilitadoDTO> buscarAQuienesSuple(Long matriculaSuplente, Date fecha) {
        logger.info("Verificando si el escribano con matrícula {} es suplente de alguien en la fecha {}",
                matriculaSuplente, fecha);

        Optional<Escribano> escribanoOpt = escribanoRepository.findEscribanoById(matriculaSuplente);

        if (!escribanoOpt.isPresent()) {
            logger.warn("No se encontró el escribano con matrícula: {}", matriculaSuplente);
            return Collections.emptyList();
        }

        return buscarAQuienesSuple(escribanoOpt.get(), fecha);
    }

    /**
     * Versión simplificada que devuelve solo el primer escribano suplido.
     * Útil cuando solo se necesita saber si es suplente de alguien.
     */
    public EscribanoHabilitadoDTO buscarSiEsSuplente(Escribano escribanoSuplente, Date fecha) {
        List<EscribanoHabilitadoDTO> suplidos = buscarAQuienesSuple(escribanoSuplente, fecha);
        return suplidos.isEmpty() ? null : suplidos.get(0);
    }

    /**
     * Sobrecarga de la versión simplificada.
     */
    public EscribanoHabilitadoDTO buscarSiEsSuplente(Long matriculaSuplente, Date fecha) {
        List<EscribanoHabilitadoDTO> suplidos = buscarAQuienesSuple(matriculaSuplente, fecha);
        return suplidos.isEmpty() ? null : suplidos.get(0);
    }

    public EscribanoDTO getEscribanoById(Long id) {
        logger.debug("Buscando escribano por ID: {} y categoría = 1", id);

        return escribanoRepository.findEscribanoById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public EscribanoDTO getEscribanoByDocumento(String documento) {
        logger.debug("Buscando escribano por documento: {}", documento);

        Optional<Escribano> escribanoOpt = escribanoRepository.findByDocumento(documento);
        return escribanoOpt.map(this::convertToDTO).orElse(null);
    }

    private EscribanoDTO convertToDTO(Escribano escribano) {
        EscribanoDTO dto = new EscribanoDTO();
        dto.setId(escribano.getCliId());
        dto.setNombre(escribano.getNombre());
        dto.setApellido(escribano.getApellido());
        dto.setTipoDocumento(escribano.getDescripcionTipoDocumento());
        dto.setDocumento(escribano.getDocumento());
        dto.setCuit(escribano.getCuit());
        dto.setTelefono(escribano.getTelefono());
        dto.setEmail(escribano.getEmail());
        dto.setDomicilio(escribano.getDomicilio());
        dto.setActivo(escribano.isActivo());
        return dto;
    }
}