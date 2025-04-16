    package com.example.compensaciones.domain.service.impl;

    import com.example.compensaciones.application.dto.CompensacionRequestDto;
    import com.example.compensaciones.application.dto.CompensacionResponseDto;
    import com.example.compensaciones.client.dto.CompensacionPaqueteDto;
    import com.example.compensaciones.domain.exception.BancoDestinoNoEncontradoException;
    import com.example.compensaciones.domain.exception.CompensacionNoEncontradaException;
    import com.example.compensaciones.domain.model.BancoDestino;
    import com.example.compensaciones.domain.model.Compensacion;
    import com.example.compensaciones.domain.repository.BancoDestinoRepository;
    import com.example.compensaciones.domain.repository.CompensacionRepository;
    import com.example.compensaciones.domain.service.CompensacionService;
    import com.example.compensaciones.domain.service.FxService;
    import org.springframework.cache.annotation.CacheEvict;
    import org.springframework.cache.annotation.Cacheable;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.scheduling.annotation.Scheduled;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.web.client.RestTemplate;

    import java.math.BigDecimal;
    import java.math.RoundingMode;
    import java.util.List;
    import java.util.Map;
    import java.util.Optional;
    import java.util.stream.Collectors;

    /**
     * Implementación del servicio de compensaciones.
     * Gestiona el registro, ejecución y consulta de transacciones monetarias,
     * además del envío de paquetes agrupados por país destino.
     */
    @Slf4j
    @Service
    @RequiredArgsConstructor
    public class CompensacionServiceImpl implements CompensacionService {

        private final CompensacionRepository repository;
        private final FxService fxService;
        private final RestTemplate restTemplate;
        private final BancoDestinoRepository bancoDestinoRepository;


        /**
         * Registra una nueva compensación calculando el tipo de cambio y monto convertido.
         *
         * @param requestDto datos de entrada de la transacción.
         * @return DTO con los datos registrados.
         */

        @Override
        @Transactional
        public CompensacionResponseDto registrarCompensacion(CompensacionRequestDto requestDto) {
            BigDecimal tipoCambio = fxService.obtenerTipoCambio(requestDto.getMonedaOrigen(), requestDto.getMonedaDestino()).setScale(6, RoundingMode.HALF_UP);;
            BigDecimal montoConvertido = requestDto.getMonto().multiply(tipoCambio).setScale(2, RoundingMode.HALF_UP);;

            Compensacion entity = Compensacion.builder()
                    .monto(requestDto.getMonto())
                    .monedaOrigen(requestDto.getMonedaOrigen())
                    .monedaDestino(requestDto.getMonedaDestino())
                    .tipoCambio(tipoCambio)
                    .montoConvertido(montoConvertido)
                    .ejecutado(false)
                    .build();
            log.info("[REGISTRO] TXID={} MONTO={} ORIGEN={} DESTINO={} FX={}",
                    entity.getMonto(),
                    entity.getMonedaOrigen(),
                    entity.getMonedaDestino(),
                    entity.getTipoCambio()
            );
            Compensacion saved = repository.save(entity);
            return mapToDto(saved);
        }


        /**
         * Obtiene una compensación por su ID.
         *
         * @param id identificador de la compensación.
         * @return DTO con la información de la compensación.
         * @throws CompensacionNoEncontradaException si no existe.
         */
        @Override
        public CompensacionResponseDto  obtenerCompensacionPorId(Long id) {
            Compensacion entity = repository.findById(id)
                    .orElseThrow(() -> new CompensacionNoEncontradaException(id));
            return mapToDto(entity);
        }

        /**
         * Ejecuta todas las compensaciones pendientes agrupadas por moneda destino,
         * las envía al banco correspondiente, y las marca como ejecutadas.
         */

        @Override
        @Transactional
        public void ejecutarCompensacion() {
            List<Compensacion> noEjecutadas = repository.findByEjecutadoFalse();

            if (noEjecutadas.isEmpty()) {
                log.info("No hay compensaciones pendientes para ejecutar.");
            }
            Map<String, List<Compensacion>> agrupadas = noEjecutadas.stream()
                    .collect(Collectors.groupingBy(Compensacion::getMonedaDestino));

            try {
                agrupadas.forEach((moneda, grupo) -> {


                    BancoDestino banco = bancoDestinoRepository.findByMoneda(moneda)
                            .orElseThrow(() -> new BancoDestinoNoEncontradoException(moneda));

                    String urlBanco = banco.getEndpoint();

                    List<CompensacionResponseDto> detalleDto = grupo.stream()
                            .map(this::mapToDto)
                            .toList();

                    CompensacionPaqueteDto paquete = CompensacionPaqueteDto.builder()
                            .paisDestino(moneda.equals("COP") ? "Colombia" : "Otro")
                            .cantidadTransacciones(detalleDto.size())
                            .transacciones(detalleDto)
                            .build();

                    log.info("Enviando Paquete a {} con {} transacciones", urlBanco, detalleDto.size());
                    restTemplate.postForObject(urlBanco, paquete, Void.class);
                    grupo.forEach(c -> c.setEjecutado(true));
                    repository.saveAll(grupo);
                    log.info("[ENVÍO]Correcto de transacciones");
                });
            } catch (Exception e) {
                log.error("❌ Error procesando grupo de compensaciones: {}", e.getMessage(), e);

            }

        }

        /**
         * Realiza la conversión de moneda utilizando el tipo de cambio actual.
         *
         * @param montoOriginal monto original.
         * @param monedaOrigen moneda de origen.
         * @param monedaDestino moneda de destino.
         * @return monto convertido.
         */

        public BigDecimal calcularMontoConvertido(BigDecimal montoOriginal, String monedaOrigen, String monedaDestino) {
            BigDecimal tipoCambio = fxService.obtenerTipoCambio(monedaOrigen, monedaDestino);
            return montoOriginal.multiply(tipoCambio);
        }

        /**
         * Ejecuta automáticamente la compensación de paquetes cada 12 horas.
         * Cron: 0 0 /12 * * *  cada 12 horas exactas.
                */
        @Scheduled(cron = "0 0 */12 * * *")
       //@Scheduled(cron = "*/30 * * * * *")
       public void ejecutarCada12Horas() {
            this.ejecutarCompensacion();
        }


        /**
         * Convierte una entidad Compensacion a su DTO correspondiente.
         *
         * @param c entidad de compensación.
         * @return DTO con los datos.
         */
        private CompensacionResponseDto mapToDto(Compensacion c) {
            return CompensacionResponseDto.builder()
                    .id(c.getId())
                    .monto(c.getMonto())
                    .monedaOrigen(c.getMonedaOrigen())
                    .monedaDestino(c.getMonedaDestino())
                    .tipoCambio(c.getTipoCambio())
                    .montoConvertido(c.getMontoConvertido())
                    .ejecutado(c.isEjecutado())
                    .build();
        }


    }
