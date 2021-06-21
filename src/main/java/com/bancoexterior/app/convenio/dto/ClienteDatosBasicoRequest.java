package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class ClienteDatosBasicoRequest implements Serializable{
	
	@JsonProperty("idSesion")
	private String idSesion;
	
	@JsonProperty("idUsuario")
	private String idUsuario;
	
	@JsonProperty("codUsuario")
	private String codUsuario;
	
	@JsonProperty("canal")
	private String canal;
	
	@JsonProperty("ip")
	private String ip;
	
	@JsonProperty("codigoIbs")
	private String codigoIbs;
	
	@JsonProperty("nroIdCliente")
	private String nroIdCliente;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
