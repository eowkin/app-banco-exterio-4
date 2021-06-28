package com.bancoexterior.app.cce.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class Filtros implements Serializable{

	

	@JsonProperty("montoDesde")
	private BigDecimal montoDesde;
	
	@JsonProperty("montoHasta")
	private BigDecimal montoHasta;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("referencia")
	private Integer referencia;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
