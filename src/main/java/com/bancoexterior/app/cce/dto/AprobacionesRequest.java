package com.bancoexterior.app.cce.dto;

import java.io.Serializable;

import com.bancoexterior.app.cce.model.DatosAprobacion;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AprobacionesRequest implements Serializable{

	@JsonProperty("idSesion")
	private String idSesion;
	
	@JsonProperty("idUsuario")
	private String idUsuario;
	
	@JsonProperty("ip")
	private String ip;
	
	@JsonProperty("idCanal")
	private String idCanal;
	
	@JsonProperty("origen")
	private String origen;
	
	@JsonProperty("datosAprobacion")
	private DatosAprobacion datosAprobacion;
	
	
	
	public AprobacionesRequest() {
		super();
		this.datosAprobacion = new DatosAprobacion();
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
}
