package org.davide.ggbproyect.service;

import org.davide.ggbproyect.models.*;
import org.davide.ggbproyect.models.enums.EstadoComanda;
import org.davide.ggbproyect.models.enums.EstadoFactura;
import org.davide.ggbproyect.models.enums.EstadoPago;
import org.davide.ggbproyect.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final SesionesMesaRepository sesionesMesaRepository;
    private final ComandaRepository comandaRepository;
    private final LineasComandaRepository lineasComandaRepository;
    private final PagosMesaRepository pagosMesaRepository;
    private final LudotecaSesionesRepository ludotecaSesionesRepository;
    private final EmailService emailService;

    public FacturaService(FacturaRepository facturaRepository,
                          SesionesMesaRepository sesionesMesaRepository,
                          ComandaRepository comandaRepository,
                          LineasComandaRepository lineasComandaRepository,
                          PagosMesaRepository pagosMesaRepository,
                          LudotecaSesionesRepository ludotecaSesionesRepository,
                          EmailService emailService) {
        this.facturaRepository = facturaRepository;
        this.sesionesMesaRepository = sesionesMesaRepository;
        this.comandaRepository = comandaRepository;
        this.lineasComandaRepository = lineasComandaRepository;
        this.pagosMesaRepository = pagosMesaRepository;
        this.ludotecaSesionesRepository = ludotecaSesionesRepository;
        this.emailService = emailService;
    }

    @Transactional(readOnly = true)
    public Page<FacturaDTO> getAll(Pageable pageable) {
        return facturaRepository.findAll(pageable)
                .map(FacturaDTO::new);
    }

    @Transactional(readOnly = true)
    public FacturaDTO getById(Integer id) {
        return facturaRepository.findById(id)
                .map(FacturaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Factura con id " + id + " no encontrada"));
    }

    @Transactional(readOnly = true)
    public FacturaDTO getBySesionId(Integer idSesion) {
        return facturaRepository.findByIdSesionId(idSesion)
                .map(FacturaDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Factura para sesion " + idSesion + " no encontrada"));
    }

    @Transactional(readOnly = true)
    public List<FacturaDTO> getByClienteId(Integer idCliente) {
        return facturaRepository.findByIdClienteId(idCliente).stream()
                .map(FacturaDTO::new)
                .toList();
    }

    /**
     * Genera factura automaticamente al cerrar una sesion.
     * Calcula desglose IVA hacia atras (precios en BD incluyen IVA).
     */
    public FacturaDTO generarFactura(Integer idSesion) {
        SesionesMesa sesion = sesionesMesaRepository.findById(idSesion)
                .orElseThrow(() -> new EntityNotFoundException("Sesion con id " + idSesion + " no encontrada"));

        if (facturaRepository.findByIdSesionId(idSesion).isPresent()) {
            throw new IllegalStateException("Ya existe una factura para la sesion " + idSesion);
        }

        List<Comanda> comandas = comandaRepository.findByIdSesionId(idSesion);

        BigDecimal baseImponible10 = BigDecimal.ZERO;
        BigDecimal baseImponible21 = BigDecimal.ZERO;

        for (Comanda comanda : comandas) {
            if (comanda.getEstado() == EstadoComanda.CANCELADA) continue;
            List<LineasComanda> lineas = lineasComandaRepository.findByIdComandaId(comanda.getId());
            for (LineasComanda linea : lineas) {
                BigDecimal totalLinea = linea.getPrecioUnitarioHistorico()
                        .multiply(BigDecimal.valueOf(linea.getCantidad()));
                Producto producto = linea.getIdProducto();
                if (producto.getTipoIva() != null && producto.getTipoIva() == 10) {
                    BigDecimal base = totalLinea.divide(BigDecimal.valueOf(1.10), 2, RoundingMode.HALF_UP);
                    baseImponible10 = baseImponible10.add(base);
                } else {
                    BigDecimal base = totalLinea.divide(BigDecimal.valueOf(1.21), 2, RoundingMode.HALF_UP);
                    baseImponible21 = baseImponible21.add(base);
                }
            }
        }

        BigDecimal cuotaIva10 = baseImponible10.multiply(BigDecimal.valueOf(0.10)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal cuotaIva21 = baseImponible21.multiply(BigDecimal.valueOf(0.21)).setScale(2, RoundingMode.HALF_UP);

        BigDecimal importeLudoteca = BigDecimal.ZERO;
        LudotecaSesiones ludoteca = ludotecaSesionesRepository.findByIdSesionId(idSesion).orElse(null);
        if (ludoteca != null && ludoteca.getImporteTotal() != null) {
            importeLudoteca = ludoteca.getImporteTotal();
            BigDecimal baseLudoteca = importeLudoteca.divide(BigDecimal.valueOf(1.21), 2, RoundingMode.HALF_UP);
            baseImponible21 = baseImponible21.add(baseLudoteca);
            cuotaIva21 = cuotaIva21.add(baseLudoteca.multiply(BigDecimal.valueOf(0.21)).setScale(2, RoundingMode.HALF_UP));
        }

        BigDecimal total = baseImponible10.add(cuotaIva10).add(baseImponible21).add(cuotaIva21);

        BigDecimal totalPagado = pagosMesaRepository.findByIdSesionId(idSesion).stream()
                .filter(p -> p.getEstado() == EstadoPago.PAGADO)
                .map(p -> p.getImporte() != null ? p.getImporte() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Factura factura = new Factura();
        factura.setNumeroFactura(generarNumeroFactura());
        factura.setIdSesion(sesion);
        factura.setIdCliente(sesion.getIdCliente());
        factura.setFechaEmision(Instant.now());
        factura.setBaseImponible10(baseImponible10);
        factura.setCuotaIva10(cuotaIva10);
        factura.setBaseImponible21(baseImponible21);
        factura.setCuotaIva21(cuotaIva21);
        factura.setImporteLudoteca(importeLudoteca);
        factura.setTotal(total);
        factura.setTotalPagado(totalPagado);
        factura.setEstado(totalPagado.compareTo(total) >= 0 ? EstadoFactura.PAGADA : EstadoFactura.EMITIDA);

        return new FacturaDTO(facturaRepository.save(factura));
    }

    public void enviarPorEmail(Integer id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Factura con id " + id + " no encontrada"));
        if (factura.getIdCliente() == null) {
            throw new IllegalStateException("La factura no tiene cliente asociado");
        }
        Cliente cliente = factura.getIdCliente();
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00");
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                .withZone(java.time.ZoneId.of("Europe/Madrid"));

        emailService.enviarFactura(
                cliente.getEmail(),
                cliente.getNombre(),
                factura.getNumeroFactura(),
                dtf.format(factura.getFechaEmision()),
                df.format(factura.getBaseImponible10()),
                df.format(factura.getCuotaIva10()),
                df.format(factura.getBaseImponible21()),
                df.format(factura.getCuotaIva21()),
                df.format(factura.getImporteLudoteca() != null ? factura.getImporteLudoteca() : BigDecimal.ZERO),
                df.format(factura.getTotal()),
                df.format(factura.getTotalPagado()),
                factura.getEstado().name()
        );
    }

    private String generarNumeroFactura() {
        int year = LocalDate.now().getYear();
        Integer maxId = facturaRepository.findMaxId().orElse(0);
        return String.format("F-%d-%05d", year, maxId + 1);
    }
}
