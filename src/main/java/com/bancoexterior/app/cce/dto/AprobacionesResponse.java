package com.bancoexterior.app.cce.dto;


import java.io.Serializable;

import com.bancoexterior.app.convenio.response.Resultado;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AprobacionesResponse implements Serializable{
	
	
	@JsonProperty("resultado")
	private Resultado resultado;
	
	public AprobacionesResponse (){
		
		this.resultado = new Resultado();

	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
