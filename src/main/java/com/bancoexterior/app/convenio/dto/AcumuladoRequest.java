package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;

import com.bancoexterior.app.convenio.model.DatosConsulta;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class AcumuladoRequest implements Serializable{
	

	@JsonProperty("idSesion")
	private String idSesion;
	
	@JsonProperty("idUsuario")
	private String idUsuario;
	
	@JsonProperty("canal")
	private String canal;
	
	@JsonProperty("tipoAcumulado")
	private String tipoAcumulado;
	
	@JsonProperty("datosConsulta")
	private DatosConsulta datosConsulta;
	
	public AcumuladoRequest() {
		super();
		this.datosConsulta = new DatosConsulta();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
