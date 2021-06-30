package com.bancoexterior.app.cce.dto;

import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class BancoRequest implements Serializable{

	@JsonProperty("idSesion")
	private String idSesion;
	
	@JsonProperty("idUsuario")
	private String idUsuario;
	
	@JsonProperty("codBanco")
	private String codBanco;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
