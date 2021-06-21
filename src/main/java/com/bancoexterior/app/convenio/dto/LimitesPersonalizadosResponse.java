package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bancoexterior.app.convenio.model.LimitesPersonalizados;
import com.bancoexterior.app.convenio.response.Resultado;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LimitesPersonalizadosResponse implements Serializable{

	@JsonProperty("resultado")
	private Resultado resultado;
	
	
	@JsonProperty("limiteCliente")
	private List<LimitesPersonalizados> limitesPersonalizados;
	
	
	
	
	
	public LimitesPersonalizadosResponse() {
		super();
		this.resultado = new Resultado();
		
		this.limitesPersonalizados = new ArrayList<>();
	}





	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
