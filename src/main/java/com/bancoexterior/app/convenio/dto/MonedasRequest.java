package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;

import com.bancoexterior.app.convenio.model.Moneda;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MonedasRequest implements Serializable{

	
	@JsonProperty("idSesion")
	private String idSesion;
	
	@JsonProperty("idUsuario")
	private String idUsuario;
	
	@JsonProperty("codUsuario")
	private String codUsuario;
	
	@JsonProperty("canal")
	private String canal;
	
	@JsonProperty("moneda")
	private Moneda moneda;
	
	public MonedasRequest() {
		super();
		this.moneda = new Moneda();
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
