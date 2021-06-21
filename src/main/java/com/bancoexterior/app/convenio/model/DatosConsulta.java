package com.bancoexterior.app.convenio.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class DatosConsulta implements Serializable{
	
	

	@JsonProperty("codigoIbs")
	private String codigoIbs;
	
	@JsonProperty("fechaDesde")
	private String fechaDesde;

	@JsonProperty("fechaHasta")
	private String fechaHasta;
	
	@JsonProperty("tipoTransaccion")
	private String tipoTransaccion;
	
	@JsonProperty("tipoCliente")
	private String tipoCliente;
	
	@JsonProperty("codMoneda")
	private String codMoneda;
		
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
