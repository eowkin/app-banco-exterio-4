package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;

import com.bancoexterior.app.convenio.model.DatosClientes;
import com.bancoexterior.app.convenio.response.Resultado;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class ClienteDatosBasicosResponse implements Serializable{

	
	@JsonProperty("resultado")
	private Resultado resultado;
	
	@JsonProperty("datosCliente")
	private DatosClientes datosCliente;
	
	
	
	
	public ClienteDatosBasicosResponse() {
		super();
		this.resultado = new Resultado();
		this.datosCliente = new DatosClientes();
		
	}




	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
