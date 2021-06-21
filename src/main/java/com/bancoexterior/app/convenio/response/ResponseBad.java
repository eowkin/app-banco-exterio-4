package com.bancoexterior.app.convenio.response;



import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class ResponseBad {
	@JsonProperty("resultado")
	private Resultado resultadoBAD;
	
	public ResponseBad() {
		super();
		this.resultadoBAD = new Resultado();

	}
	
	public ResponseBad(Exception exception, Integer code) {
		super();
		this.resultadoBAD = new Resultado();
		this.resultadoBAD.setCodigo(String.valueOf(code)); 
    	this.resultadoBAD.setDescripcion(exception.getClass().getSimpleName() +" " +exception.getMessage());
	}
}
