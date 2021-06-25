package com.bancoexterior.app.cce.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bancoexterior.app.cce.dto.CceTransaccionDto;
import com.bancoexterior.app.cce.model.CceTransaccion;


public interface ICceTransaccionService {
    public List<CceTransaccionDto> consultar();
    
    public Page<CceTransaccion> consultar(Pageable page);
    
    
    
   
    
    public List<CceTransaccionDto> consultaMovimientosConFechas(String codTransaccion, String bancoDestino, String numeroIdentificacion, 
    															String fechaDesde, String fechaHasta);
    
    
    public Page<CceTransaccion> consultaMovimientosConFechas(String codTransaccion, String bancoDestino, String numeroIdentificacion, 
			String fechaDesde, String fechaHasta, Pageable page);
    
   
    
    
    
    public List<CceTransaccionDto> consultaMovimientosConFechasPrueba(String fechaDesde, String fechaHasta);
    
    public List<CceTransaccionDto> findByCodTransaccion(String codTransaccion);
    
    public CceTransaccionDto findByEndtoendId(String endtoendId);
    
    public List<CceTransaccionDto> consultaMovimientosPorAprobarAltoValor();
    
    
}
