package com.bancoexterior.app.convenio.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class DatosCredito implements Serializable{

	@JsonProperty("referencia")
	private String referencia;
	
	@JsonProperty("montoTransaccion")
	private BigDecimal montoTransaccion;
	
	@JsonProperty("codMoneda")
	private String codMoneda;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
