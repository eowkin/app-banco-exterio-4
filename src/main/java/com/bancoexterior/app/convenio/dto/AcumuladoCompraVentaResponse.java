package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;

import com.bancoexterior.app.convenio.model.AcumuladosCompraVenta;
import com.bancoexterior.app.convenio.response.Resultado;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AcumuladoCompraVentaResponse implements Serializable{

	@JsonProperty("resultado")
	private Resultado resultado;
	
	@JsonProperty("acumuladosCompraVenta")
	private AcumuladosCompraVenta acumuladosCompraVenta;
	
	public AcumuladoCompraVentaResponse() {
		super();
		this.resultado = new Resultado();
		this.acumuladosCompraVenta = new AcumuladosCompraVenta();
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
