package com.bancoexterior.app.convenio.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class DatosReverso implements Serializable{

	@JsonProperty("codigoRespuesta")
	private String codigoRespuesta;
	
	
	@JsonProperty("descripcion")
	private String descripcion;
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

}
