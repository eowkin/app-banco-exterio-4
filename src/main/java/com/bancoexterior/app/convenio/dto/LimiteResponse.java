package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bancoexterior.app.convenio.model.LimitesGenerales;
import com.bancoexterior.app.convenio.response.Resultado;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class LimiteResponse implements Serializable{

	@JsonProperty("resultado")
	private Resultado resultado;
	
	@JsonProperty("limite")
	private List<LimitesGenerales> limites;
	
	
	
	
	
	
	public LimiteResponse() {
		super();
		this.resultado = new Resultado();
		this.limites = new ArrayList<>();
		
	}






	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
