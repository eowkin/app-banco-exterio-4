package com.bancoexterior.app.convenio.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class Venta implements Serializable{

	@JsonProperty("codMoneda")
	private String codMoneda;
	
	@JsonProperty("descripcion")
	private String descripcion;
	
	@JsonProperty("monto")
	private BigDecimal monto;
	
	@JsonProperty("montoBs")
	private BigDecimal montoBs;
	
	private String montoString;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
