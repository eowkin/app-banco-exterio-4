package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;

import com.bancoexterior.app.convenio.model.Agencia;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class AgenciaRequest  implements Serializable{
	
	@JsonProperty("idSesion")
	private String idSesion;
	
	@JsonProperty("idUsuario")
	private String idUsuario;
	
	@JsonProperty("codUsuario")
	private String codUsuario;
	
	@JsonProperty("canal")
	private String canal;
	
	@JsonProperty("agencia")
	private Agencia agencia;

	public AgenciaRequest() {
		super();
		this.agencia = new Agencia();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
