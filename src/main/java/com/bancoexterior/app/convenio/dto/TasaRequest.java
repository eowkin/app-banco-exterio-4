package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;

import com.bancoexterior.app.convenio.model.Tasa;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TasaRequest implements Serializable{
	

	@JsonProperty("idSesion")
	private String idSesion;
	
	@JsonProperty("idUsuario")
	private String idUsuario;
	
	@JsonProperty("codUsuario")
	private String codUsuario;
	
	@JsonProperty("canal")
	private String canal;
	
	@JsonProperty("tasa")
	private Tasa tasa;
	
	
	public TasaRequest() {
		super();
		this.tasa = new Tasa();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


}


