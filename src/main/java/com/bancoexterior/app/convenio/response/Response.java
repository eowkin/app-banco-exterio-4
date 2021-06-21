package com.bancoexterior.app.convenio.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Response implements Serializable{

	
	@JsonProperty("resultado")
	private Resultado resultado;

	public Response() {
		super();
		this.resultado = new Resultado();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
