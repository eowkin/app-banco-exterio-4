package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bancoexterior.app.convenio.model.Agencia;
import com.bancoexterior.app.convenio.response.Resultado;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class AgenciaResponse implements Serializable{

	
	@JsonProperty("resultado")
	private Resultado resultado;
	
	@JsonProperty("agencia")
	private List<Agencia> listaAgencias;
	
	public AgenciaResponse() {
		super();
		this.resultado = new Resultado();
		this.listaAgencias = new ArrayList<>();
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
