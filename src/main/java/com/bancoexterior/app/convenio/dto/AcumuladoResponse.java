package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;

import com.bancoexterior.app.convenio.model.AcumuladosPorAprobar;
import com.bancoexterior.app.convenio.response.Resultado;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AcumuladoResponse implements Serializable{
	
	
	@JsonProperty("resultado")
	private Resultado resultado;
	
	@JsonProperty("acumuladosPorAprobar")
	private AcumuladosPorAprobar acumuladosPorAprobar;
	
	
	public AcumuladoResponse() {
		super();
		this.resultado = new Resultado();
		this.acumuladosPorAprobar = new AcumuladosPorAprobar();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
