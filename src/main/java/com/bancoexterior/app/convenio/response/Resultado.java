package com.bancoexterior.app.convenio.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Resultado implements Serializable{
	
	@JsonProperty("codigo")
	private String codigo;
	@JsonProperty("descripcion")
	private String descripcion;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
