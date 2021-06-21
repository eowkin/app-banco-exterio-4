package com.bancoexterior.app.convenio.service;

import com.bancoexterior.app.convenio.dto.AcumuladoCompraVentaResponse;
import com.bancoexterior.app.convenio.dto.AcumuladoRequest;
import com.bancoexterior.app.convenio.dto.AcumuladoResponse;
import com.bancoexterior.app.convenio.exception.CustomException;

public interface IAcumuladosServiceApiRest {

	public String consultarAcumulados(AcumuladoRequest acumuladoRequest) throws CustomException;
	
	public AcumuladoResponse consultarAcumuladosDiariosBanco(AcumuladoRequest acumuladoRequest) throws CustomException;
	
	public AcumuladoCompraVentaResponse consultarAcumuladosCompraVenta(AcumuladoRequest acumuladoRequest) throws CustomException;
}
