
package ar.com.cnpmweb.legalizaciondigital.service;

import ar.com.cnpmweb.legalizaciondigital.dto.ContenidoDTO;
import ar.com.cnpmweb.legalizaciondigital.dto.FacturaDTO;
import ar.com.cnpmweb.legalizaciondigital.model.Contenido;
import ar.com.cnpmweb.legalizaciondigital.model.Factura;
import ar.com.cnpmweb.legalizaciondigital.model.enums.TipoFoja;
import ar.com.cnpmweb.legalizaciondigital.repository.FacturaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacturaService {
    private static final Logger logger = LoggerFactory.getLogger(FacturaService.class);

    @Autowired
    private FacturaRepository facturaRepository;

    public List<FacturaDTO> getFacturasByCliId(Integer cliId) {
        logger.info("Buscando facturas para el cliente ID: {}", cliId);

        List<Factura> facturas = facturaRepository.findByCliId(cliId);

        return facturas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<FacturaDTO> getFacturasByCliIdAndNumRegistro(Integer cliId, Integer numRegistro) {
        logger.info("Buscando facturas para el cliente ID: {} y registro: {}", cliId, numRegistro);

        List<Factura> facturas = facturaRepository.findByCliIdAndNumRegistro(cliId, numRegistro);

        return facturas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public FacturaDTO getFacturaById(Integer idSucursal, Integer idFactura) {
        logger.info("Buscando factura con ID: {}/{}", idSucursal, idFactura);

        Optional<Factura> facturaOpt = facturaRepository.findByIdWithContenidos(idSucursal, idFactura);

        return facturaOpt.map(this::convertToDTO).orElse(null);
    }

    private FacturaDTO convertToDTO(Factura factura) {
        FacturaDTO dto = new FacturaDTO();
        dto.setIdFactura(factura.getIdFactura());
        dto.setIdSucursal(factura.getIdSucursal());
        dto.setCliId(factura.getCliId());
        dto.setCliApellido(factura.getCliApellido());
        dto.setCliNombre(factura.getCliNombre());
        dto.setTipoComprobante(factura.getTipoComprobante());
        dto.setFechaComprobante(factura.getFechaComprobante());
        dto.setNumRegistro(factura.getNumRegistro());

        // Convertir los contenidos si están disponibles
        if (factura.getContenidos() != null && !factura.getContenidos().isEmpty()) {
            dto.setContenidos(factura.getContenidos().stream()
                    .map(this::convertContenidoToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private ContenidoDTO convertContenidoToDTO(Contenido contenido) {
        ContenidoDTO dto = new ContenidoDTO();
        dto.setIdSucursal(contenido.getIdSucursal());
        dto.setIdFactura(contenido.getIdFactura());
        dto.setIdRenglon(contenido.getIdRenglon());
        dto.setIdProducto(contenido.getIdProducto());
        dto.setIdSubProd(contenido.getIdSubProd());
        dto.setPrimerNumero(contenido.getPrimerNumero());
        dto.setUltimoNumero(contenido.getUltimoNumero());
        dto.setPrimerNumeroReg(contenido.getPrimerNumeroReg());
        dto.setUltimoNumeroReg(contenido.getUltimoNumeroReg());
        dto.setCliId(contenido.getCliId());
        dto.setNumRegistro(contenido.getNumRegistro());
        TipoFoja tipoFoja = contenido.getTipoFoja();
        if (tipoFoja != null) {
            dto.setTipoFojaDescripcion(tipoFoja.getDescripcion());
            dto.setTipoFojaCodigo(tipoFoja.getCodigo());
        }
        return dto;
    }
}