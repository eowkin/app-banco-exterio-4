package com.bancoexterior.app.convenio.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class ClientesPersonalizados implements Serializable{

	
	@JsonProperty("codigoIbs")
	private String codigoIbs;
	
	@JsonProperty("nroIdCliente")
	private String nroIdCliente;
	
	@JsonProperty("nombreRif")
	private String nombreRif;
	
	@JsonProperty("codUsuario")
	private String codUsuario;
	
	@JsonProperty("flagActivo")
	private Boolean flagActivo;
	
	@JsonProperty("fechaModificacion")
	private String fechaModificacion;
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
}
