package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;

import com.bancoexterior.app.convenio.model.LimitesGenerales;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class LimiteRequest implements Serializable{

	

	@JsonProperty("idSesion")
	private String idSesion;
	
	@JsonProperty("idUsuario")
	private String idUsuario;
	
	@JsonProperty("codUsuario")
	private String codUsuario;
	
	@JsonProperty("canal")
	private String canal;
	
	@JsonProperty("limite")
	private LimitesGenerales limite;
	
	
	public LimiteRequest() {
		super();
		this.limite = new LimitesGenerales();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
