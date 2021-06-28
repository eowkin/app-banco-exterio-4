package com.bancoexterior.app.cce.dto;


import java.io.Serializable;

import com.bancoexterior.app.cce.model.Filtros;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class AprobacionesConsultasRequest implements Serializable{

	

	@JsonProperty("idUsuario")
	private String idUsuario;
	
	@JsonProperty("idSesion")
	private String idSesion;
	
	@JsonProperty("idCanal")
	private String idCanal;
	
	@JsonProperty("numeroPagina")
	private Integer numeroPagina;
	
	@JsonProperty("tamanoPagina")
	private Integer tamanoPagina;
	
	@JsonProperty("filtros")
	private Filtros filtros;
	
	
	
	
	public AprobacionesConsultasRequest() {
		super();
		this.filtros = new Filtros();
	}




	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
