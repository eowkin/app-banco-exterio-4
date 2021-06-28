package com.bancoexterior.app.cce.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class DatosAprobacion implements Serializable{
	
	

	@JsonProperty("referencia")
	private Integer referencia;
	
	@JsonProperty("nroIdEmisor")
	private String nroIdEmisor;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("fechaActualizacion")
	private String fechaActualizacion;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
