package ar.com.cnpmweb.legalizaciondigital.service;

import ar.com.cnpmweb.legalizaciondigital.dto.EscribanoHabilitadoDTO;
import ar.com.cnpmweb.legalizaciondigital.dto.MatriculaHabilitadaDTO;
import ar.com.cnpmweb.legalizaciondigital.model.Usuario;
import ar.com.cnpmweb.legalizaciondigital.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

/*     @Autowired
    private EscribanoRepository escribanoRepository; */

    @Autowired
    private EscribanoService escribanoService;

    public AuthResult verifyCredentials(String usuario, String contrasenaMD5) {
        logger.debug("Verificando credenciales para el usuario: {}", usuario);

        AuthResult result = new AuthResult();

        Usuario foundUser = usuarioRepository.findByUsuario(usuario);

        if (foundUser == null || !foundUser.getContrasena().equals(contrasenaMD5)) {
            logger.debug("Credenciales inválidas para el usuario: {}", usuario);
            result.setAutenticado(false);
            result.setHabilitado(false);
            result.setMensaje("Usuario y/o contraseña incorrectas");
            return result;
        }

        // Usuario autenticado correctamente
        logger.debug("Credenciales válidas para el usuario: {}", usuario);
        result.setAutenticado(true);
        result.setMatricula(foundUser.getId());

        // Verificar si el escribano está habilitado usando el mismo método que ya
        // existe
        Date fechaActual = new Date();
        MatriculaHabilitadaDTO verificacion = escribanoService.verificarMatriculaHabilitadaEnFecha(foundUser.getId(),
                fechaActual);

        // Establecer si está habilitado basado en el código de verificación
        // boolean habilitado = "MATRICULA_VALIDA".equals(verificacion.getCodigo());
        result.setHabilitado("MATRICULA_VALIDA".equals(verificacion.getCodigo()));
        result.setLicenciaActiva("ESCRIBANO_EN_LICENCIA".equals(verificacion.getCodigo()));
        result.setCodigo(verificacion.getCodigo());

        // Mensaje según el estado de habilitación
        if (result.isHabilitado()) {
            // Si tiene licencia activa, no buscar suplencias
            if (result.isLicenciaActiva()) {
                result.setMensaje("Usuario con licencia activa");
                result.setSuplenciasDisponibles(Collections.emptyList());
            } else {
                result.setMensaje("Usuario autorizado para operar");
                // Buscar si es suplente de algún escribano
                List<EscribanoHabilitadoDTO> suplencias = escribanoService.buscarAQuienesSuple(foundUser.getId(),
                        fechaActual);
                result.setSuplenciasDisponibles(suplencias);
            }
        } else {
            result.setMensaje("Este usuario no está autorizado para operar");
            result.setSuplenciasDisponibles(Collections.emptyList());
        }

        return result;
    }

    /* public AuthResult verifyCredentialsOld(String usuario, String contrasenaMD5) {
        logger.debug("Verificando credenciales para el usuario: {}", usuario);

        AuthResult result = new AuthResult();

        Usuario foundUser = usuarioRepository.findByUsuario(usuario);

        if (foundUser == null || !foundUser.getContrasena().equals(contrasenaMD5)) {
            logger.debug("Credenciales inválidas para el usuario: {}", usuario);
            result.setAutenticado(false);
            result.setHabilitado(false);
            result.setMensaje("Usuario y/o contraseña incorrectas");
            return result;
        }

        // Usuario autenticado correctamente, verificar si está habilitado
        logger.debug("Credenciales válidas para el usuario: {}", usuario);
        result.setAutenticado(true);
        result.setMatricula(foundUser.getId());

        // Buscar el escribano asociado
        Optional<Escribano> escribanoOpt = escribanoRepository.findEscribanoById(foundUser.getId());

        if (!escribanoOpt.isPresent()) {
            // Usuario sin escribano asociado
            result.setHabilitado(false);
            result.setMensaje("Usuario sin escribano asociado: " + foundUser.getId());
            return result;
        }

        Escribano escribano = escribanoOpt.get();

        // Verificar si está habilitado por tener un antecedente activo
        boolean estaHabilitado = verificarAntecedentesHabilitantes(escribano);

        // Verificar si está inhabilitado por tener sanciones éticas vigentes
        boolean estaInhabilitado = verificarAntecedentesInhabilitantes(escribano);

        // El escribano está habilitado solo si tiene antecedentes habilitantes y no
        // tiene inhabilitaciones
        boolean habilitadoFinal = estaHabilitado && !estaInhabilitado;

        result.setHabilitado(habilitadoFinal);
        result.setMensaje(
                habilitadoFinal ? "Usuario autorizado para operar" : "Este usuario no está autorizado para operar");

        // Si el escribano está habilitado, verificar si puede ejercer como suplente
        if (habilitadoFinal) {
            Date fechaActual = new Date();
            List<EscribanoHabilitadoDTO> suplencias = escribanoService.buscarAQuienesSuple(escribano.getCliId(),
                    fechaActual);
            result.setSuplenciasDisponibles(suplencias);
        } else {
            result.setSuplenciasDisponibles(Collections.emptyList());
        }

        return result;
    } */

/*     private boolean verificarAntecedentesHabilitantes(Escribano escribano) {
        // Códigos de novedades habilitantes
        List<Integer> codigosNovHabilitantes = Arrays.asList(
                TipoNovedad.TITULAR.getCodigo(),
                TipoNovedad.ADSCRIPTO.getCodigo(),
                TipoNovedad.INTERINO.getCodigo());

        Date fechaNula = crearFechaNula();

        return escribano.getAntecedentes().stream()
                .anyMatch(antecedente -> codigosNovHabilitantes.contains(antecedente.getNovIdCodigo()) &&
                        (antecedente.getFechaBaja() == null || antecedente.getFechaBaja().equals(fechaNula)));
    }

    private boolean verificarAntecedentesInhabilitantes(Escribano escribano) {
        // Códigos de novedades éticas que inhabilitan
        List<Integer> codigosNovInhabilitantes = Arrays.asList(
                TipoNovedad.SUSPENSION.getCodigo());

        Date fechaActual = new Date();

        return escribano.getAntecedentes().stream()
                .anyMatch(antecedente -> codigosNovInhabilitantes.contains(antecedente.getNovIdCodigo()) &&
                        fechaEstaDentroDeRango(fechaActual, antecedente.getFechaAlta(), antecedente.getFechaBaja()));
    } */

/*     private boolean fechaEstaDentroDeRango(Date fecha, Date fechaInicio, Date fechaFin) {
        if (fecha == null || fechaInicio == null) {
            return false;
        }

        if (fechaFin == null || fechaFin.equals(crearFechaNula())) {
            return !fecha.before(fechaInicio);
        }

        return !fecha.before(fechaInicio) && !fecha.after(fechaFin);
    } */
/* 
    private Date crearFechaNula() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 1);
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            return cal.getTime();
        } catch (Exception e) {
            return null;
        }
    } */

    // Clase interna para devolver el resultado completo
    public static class AuthResult {
        private boolean autenticado=false;
        private boolean habilitado=false;
        private boolean licenciaActiva=false;
        private String codigo;
        private String mensaje;
        private Long matricula;
        private List<EscribanoHabilitadoDTO> suplenciasDisponibles;

        public boolean isAutenticado() {
            return autenticado;
        }

        public void setAutenticado(boolean autenticado) {
            this.autenticado = autenticado;
        }

        public boolean isHabilitado() {
            return habilitado;
        }

        public void setHabilitado(boolean habilitado) {
            this.habilitado = habilitado;
        }

        public boolean isLicenciaActiva() {
            return licenciaActiva;
        }

        public void setLicenciaActiva(boolean licenciaActiva) {
            this.licenciaActiva = licenciaActiva;
        }

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

        public List<EscribanoHabilitadoDTO> getSuplenciasDisponibles() {
            return suplenciasDisponibles;
        }

        public void setSuplenciasDisponibles(List<EscribanoHabilitadoDTO> suplenciasDisponibles) {
            this.suplenciasDisponibles = suplenciasDisponibles;
        }

        public boolean tieneSuplenciasDisponibles() {
            return suplenciasDisponibles != null && !suplenciasDisponibles.isEmpty();
        }
    }
}