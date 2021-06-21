package com.bancoexterior.app.convenio.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class DiarioBanco implements Serializable{
	
	@JsonProperty("fecha")
	private String fecha;

	@JsonProperty("codMoneda")
	private String codMoneda;
	
	@JsonProperty("tipoTransaccion")
	private String tipoTransaccion;
	
	@JsonProperty("monto")
	private BigDecimal monto;
	
	@JsonProperty("tipoCliente")
	private String tipoCliente;
	
	@JsonProperty("fechaModificacion")
	private String fechaModificacion;
	
	
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
