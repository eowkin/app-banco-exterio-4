package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;

import com.bancoexterior.app.convenio.model.DatosReverso;
import com.bancoexterior.app.convenio.model.ResultadoOperacion;
import com.bancoexterior.app.convenio.response.Resultado;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class AprobarRechazarResponse implements Serializable{

	@JsonProperty("resultado")
	private Resultado resultado;
	
	
	@JsonProperty("resultadoOperacion")
	private ResultadoOperacion resultadoOperacion; 
	
	@JsonProperty("datosReverso")
	private DatosReverso datosReverso;
	
	
	
	public AprobarRechazarResponse() {
		super();
		this.resultado = new Resultado();
		this.resultadoOperacion = new ResultadoOperacion();
		this.datosReverso = new DatosReverso();
	}





	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
