package com.bancoexterior.app.cce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.CceTransaccionDto;
import com.bancoexterior.app.cce.model.CceTransaccion;
import com.bancoexterior.app.cce.repository.ICceTransaccionRepository;
import com.bancoexterior.app.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CceTransaccionServiceImpl implements ICceTransaccionService{

	@Autowired 
	private ICceTransaccionRepository repo;
	
	@Autowired
	private Mapper mapper;
	
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
	public List<CceTransaccionDto> consultaMovimientosSinFechas(String codTransaccion, String bancoDestino,
			String numeroIdentificacion) {
		log.info("consultaMovimientosSinFechas");
		log.info("codTransaccion: "+codTransaccion);
		log.info("bancoDestino: "+bancoDestino);
		log.info("numeroIdentificacion: "+numeroIdentificacion);
		
		List<CceTransaccion> listaCceTransacciones = repo.consultaMovimientosSinFechas(codTransaccion, bancoDestino, numeroIdentificacion);
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
		log.info("consultaMovimientosConFechas");
		
		fechaDesde = fechaDesde +" 00:00:00";
		fechaHasta = fechaHasta +" 23:59:00";
		log.info("codTransaccion: "+codTransaccion);
		log.info("bancoDestino: "+bancoDestino);
		log.info("numeroIdentificacion: "+numeroIdentificacion);
		log.info("fechaDesde: "+fechaDesde);
		log.info("fechaHasta: "+fechaHasta);
		
		List<CceTransaccion> listaCceTransacciones = repo.consultaMovimientosConFechas(codTransaccion, bancoDestino, numeroIdentificacion, fechaDesde, fechaHasta);
		List<CceTransaccionDto> listaCceTransaccionesDto = new ArrayList<>();
		for (CceTransaccion cceTransaccion : listaCceTransacciones) {
			CceTransaccionDto cceTransaccionDto = mapper.map(cceTransaccion, CceTransaccionDto.class);
			listaCceTransaccionesDto.add(cceTransaccionDto);
		}
		return listaCceTransaccionesDto;
	}

	@Override
	public List<CceTransaccionDto> consultaMovimientosConFechasPrueba(String fechaDesde, String fechaHasta) {
		fechaDesde = fechaDesde +" 00:00:00";
		fechaHasta = fechaHasta +" 23:59:00";
		
		log.info("fechaDesde: "+fechaDesde);
		log.info("fechaHasta: "+fechaHasta);
		
		
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
		log.info("consultaMovimientosConFechasPageable");
		
		fechaDesde = fechaDesde +" 00:00:00";
		fechaHasta = fechaHasta +" 23:59:00";
		log.info("codTransaccion: "+codTransaccion);
		log.info("bancoDestino: "+bancoDestino);
		log.info("numeroIdentificacion: "+numeroIdentificacion);
		log.info("fechaDesde: "+fechaDesde);
		log.info("fechaHasta: "+fechaHasta);
		
		return repo.consultaMovimientosConFechas(codTransaccion, bancoDestino, numeroIdentificacion, fechaDesde, fechaHasta, page); 
	}

	@Override
	public Page<CceTransaccion> consultaMovimientosSinFechas(String codTransaccion, String bancoDestino,
			String numeroIdentificacion, Pageable page) {
		log.info("codTransaccion: "+codTransaccion);
		log.info("bancoDestino: "+bancoDestino);
		log.info("numeroIdentificacion: "+numeroIdentificacion);
		
		
		return repo.consultaMovimientosSinFechas(codTransaccion, bancoDestino, numeroIdentificacion, page);
	}

	@Override
	public CceTransaccionDto findByEndtoendId(String endtoendId) {
		CceTransaccion cceTransaccion = repo.findById(endtoendId).orElse(null);
		if(cceTransaccion != null) {
			return mapper.map(cceTransaccion, CceTransaccionDto.class);
		}
		return null;
	}

}
