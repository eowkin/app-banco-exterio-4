package com.bancoexterior.app.cce.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.CceTransaccionDto;
import com.bancoexterior.app.cce.model.CceTransaccion;
import com.bancoexterior.app.cce.repository.ICceTransaccionRepository;
import com.bancoexterior.app.util.Mapper;




@Service
public class CceTransaccionServiceImpl implements ICceTransaccionService{

	private static final Logger LOGGER = LogManager.getLogger(CceTransaccionServiceImpl.class);
	
	
	//@Autowired 
	private ICceTransaccionRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	private static final String CCETRANSACCIONSERVICECONSULTAMOVIMIENTOSCONFECHASPAGEI = "[==== INICIO ConsultaMovimientosConFechasPage CceTransaccion Consultas - Service ====]";
	
	private static final String CCETRANSACCIONSERVICECONSULTAMOVIMIENTOSCONFECHASPAGEF = "[==== FIN ConsultaMovimientosConFechasPage CceTransaccion Consultas - Service ====]";
	
	private static final String CCETRANSACCIONSERVICECONSULTAMOVIMIENTOSCONFECHASI = "[==== INICIO ConsultaMovimientosConFechas CceTransaccion Consultas - Service ====]";
	
	private static final String CCETRANSACCIONSERVICECONSULTAMOVIMIENTOSCONFECHASF = "[==== FIN ConsultaMovimientosConFechas CceTransaccion Consultas - Service ====]";
	
	private static final String CCETRANSACCIONSERVICEFINDBYENDTOENDIDI = "[==== INICIO FindByEndtoendId CceTransaccion Consultas - Service ====]";
	
	private static final String CCETRANSACCIONSERVICEFINDBYENDTOENDIDF = "[==== FIN FindByEndtoendId CceTransaccion Consultas - Service ====]";
	
	//findByEndtoendId
	
	@Override
	public List<CceTransaccionDto> consultar() {
		List<CceTransaccion> listaCceTransacciones = repo.findAll();
		List<CceTransaccionDto> listaCceTransaccionesDto = new ArrayList<>();
		for (CceTransaccion cceTransaccion : listaCceTransacciones) {
			CceTransaccionDto cceTransaccionDto = mapper.map(cceTransaccion, CceTransaccionDto.class);
			listaCceTransaccionesDto.add(cceTransaccionDto);
		}
		return listaCceTransaccionesDto;
	}

	@Override
	public List<CceTransaccionDto> findByCodTransaccion(String codTransaccion) {
		List<CceTransaccion> listaCceTransacciones = repo.findByCodTransaccion(codTransaccion);
		List<CceTransaccionDto> listaCceTransaccionesDto = new ArrayList<>();
		for (CceTransaccion cceTransaccion : listaCceTransacciones) {
			CceTransaccionDto cceTransaccionDto = mapper.map(cceTransaccion, CceTransaccionDto.class);
			listaCceTransaccionesDto.add(cceTransaccionDto);
		}
		return listaCceTransaccionesDto;
	}

	

	@Override
	public List<CceTransaccionDto> consultaMovimientosConFechas(String codTransaccion, String bancoDestino,
			String numeroIdentificacion, String fechaDesde, String fechaHasta) {
		LOGGER.info(CCETRANSACCIONSERVICECONSULTAMOVIMIENTOSCONFECHASI);
		
		fechaDesde = fechaDesde +" 00:00:00";
		fechaHasta = fechaHasta +" 23:59:00";
		LOGGER.info("codTransaccion: "+codTransaccion);
		LOGGER.info("bancoDestino: "+bancoDestino);
		LOGGER.info("numeroIdentificacion: "+numeroIdentificacion);
		LOGGER.info("fechaDesde: "+fechaDesde);
		LOGGER.info("fechaHasta: "+fechaHasta);
		
		List<CceTransaccion> listaCceTransacciones = repo.consultaMovimientosConFechas(codTransaccion, bancoDestino, numeroIdentificacion, fechaDesde, fechaHasta);
		List<CceTransaccionDto> listaCceTransaccionesDto = new ArrayList<>();
		for (CceTransaccion cceTransaccion : listaCceTransacciones) {
			CceTransaccionDto cceTransaccionDto = mapper.map(cceTransaccion, CceTransaccionDto.class);
			listaCceTransaccionesDto.add(cceTransaccionDto);
		}
		LOGGER.info(CCETRANSACCIONSERVICECONSULTAMOVIMIENTOSCONFECHASF);
		return listaCceTransaccionesDto;
	}

	@Override
	public List<CceTransaccionDto> consultaMovimientosConFechasPrueba(String fechaDesde, String fechaHasta) {
		fechaDesde = fechaDesde +" 00:00:00";
		fechaHasta = fechaHasta +" 23:59:00";
		
		LOGGER.info("fechaDesde: "+fechaDesde);
		LOGGER.info("fechaHasta: "+fechaHasta);
		
		
		List<CceTransaccion> listaCceTransacciones = repo.consultaMovimientosConFechasPrueba(fechaDesde, fechaHasta);
		List<CceTransaccionDto> listaCceTransaccionesDto = new ArrayList<>();
		for (CceTransaccion cceTransaccion : listaCceTransacciones) {
			CceTransaccionDto cceTransaccionDto = mapper.map(cceTransaccion, CceTransaccionDto.class);
			listaCceTransaccionesDto.add(cceTransaccionDto);
		}
		return listaCceTransaccionesDto;
	}

	@Override
	public Page<CceTransaccion> consultar(Pageable page) {
		return repo.findAll(page);
		
	}

	@Override
	public Page<CceTransaccion> consultaMovimientosConFechas(String codTransaccion, String bancoDestino,
			String numeroIdentificacion, String fechaDesde, String fechaHasta, Pageable page) {
		LOGGER.info(CCETRANSACCIONSERVICECONSULTAMOVIMIENTOSCONFECHASPAGEI);
		
		fechaDesde = fechaDesde +" 00:00:00";
		fechaHasta = fechaHasta +" 23:59:00";
		LOGGER.info("codTransaccion: "+codTransaccion);
		LOGGER.info("bancoDestino: "+bancoDestino);
		LOGGER.info("numeroIdentificacion: "+numeroIdentificacion);
		LOGGER.info("fechaDesde: "+fechaDesde);
		LOGGER.info("fechaHasta: "+fechaHasta);
		LOGGER.info(CCETRANSACCIONSERVICECONSULTAMOVIMIENTOSCONFECHASPAGEF);
		return repo.consultaMovimientosConFechas(codTransaccion, bancoDestino, numeroIdentificacion, fechaDesde, fechaHasta, page); 
	}

	

	@Override
	public CceTransaccionDto findByEndtoendId(String endtoendId) {
		LOGGER.info(CCETRANSACCIONSERVICEFINDBYENDTOENDIDI);
		CceTransaccion cceTransaccion = repo.findById(endtoendId).orElse(null);
		if(cceTransaccion != null) {
			LOGGER.info(CCETRANSACCIONSERVICEFINDBYENDTOENDIDF);
			return mapper.map(cceTransaccion, CceTransaccionDto.class);
		}
		LOGGER.info(CCETRANSACCIONSERVICEFINDBYENDTOENDIDF);
		return null;
	}

	@Override
	public List<CceTransaccionDto> consultaMovimientosPorAprobarAltoValor() {
		List<CceTransaccionDto> listaMovimientosPorAprobarAltoValor = new ArrayList<>();
		CceTransaccionDto cceTransaccion1 = new CceTransaccionDto();
		cceTransaccion1.setEndtoendId("00012021061704035900172704");
		cceTransaccion1.setReferencia("00477749");
		cceTransaccion1.setCuentaOrigen("01150081131002918355");
		cceTransaccion1.setCuentaDestino("01740111431114240037");
		cceTransaccion1.setMonto(new BigDecimal(1500000000));
		cceTransaccion1.setFechaModificacion(new Date());
		listaMovimientosPorAprobarAltoValor.add(cceTransaccion1);
		
		
		return listaMovimientosPorAprobarAltoValor;
	}

	

}
