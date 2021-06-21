package com.bancoexterior.app.convenio.service;

import java.util.List;

import com.bancoexterior.app.convenio.dto.AprobarRechazarRequest;
import com.bancoexterior.app.convenio.dto.MovimientosRequest;
import com.bancoexterior.app.convenio.dto.MovimientosResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Movimiento;

public interface IMovimientosApiRest {

	public MovimientosResponse consultarMovimientosPorAprobar(MovimientosRequest movimientosRequest) throws CustomException;
	
	public MovimientosResponse consultarMovimientosPorAprobarVenta(MovimientosRequest movimientosRequest) throws CustomException;
	
	public MovimientosResponse consultarMovimientos(MovimientosRequest movimientosRequest) throws CustomException;
	
	public List<Movimiento> getListaMovimientos(MovimientosRequest movimientosRequest) throws CustomException;
	
	public String rechazarCompra(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException;
	
	public String aprobarCompra(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException;
	
	public String rechazarVenta(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException;
	
	public String aprobarVenta(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException;
}
