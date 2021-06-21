package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;


import com.bancoexterior.app.convenio.model.Movimiento;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class MovimientosRequest implements Serializable{

	

	@JsonProperty("idSesion")
	private String idSesion;
	
	@JsonProperty("idUsuario")
	private String idUsuario;
	
	@JsonProperty("usuario")
	private String usuario;
	
	@JsonProperty("canal")
	private String canal;
	
	@JsonProperty("numeroPagina")
	private int numeroPagina;
	
	@JsonProperty("tamanoPagina")
	private int tamanoPagina;
	
	@JsonProperty("filtros")
	private Movimiento filtros;
	
	
	
	
	public MovimientosRequest() {
		super();
		this.filtros = new Movimiento(); 
		
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
