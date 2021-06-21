package com.bancoexterior.app.convenio.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class Fechas implements Serializable{

	@JsonProperty("fechaDesde")
	private String fechaDesde;
	
	@JsonProperty("fechaHasta")
	private String fechaHasta;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
