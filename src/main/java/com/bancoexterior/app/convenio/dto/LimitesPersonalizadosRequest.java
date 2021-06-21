package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;

import com.bancoexterior.app.convenio.model.LimitesPersonalizados;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class LimitesPersonalizadosRequest implements Serializable{

	@JsonProperty("idSesion")
	private String idSesion;
	
	@JsonProperty("idUsuario")
	private String idUsuario;
	
	@JsonProperty("codUsuario")
	private String codUsuario;
	
	@JsonProperty("canal")
	private String canal;
	
	@JsonProperty("limiteCliente")
	private LimitesPersonalizados limiteCliente; 
	
	
	
	
	public LimitesPersonalizadosRequest() {
		super();
		this.limiteCliente = new LimitesPersonalizados();
	}




	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
