package com.bancoexterior.app.convenio.model;

import java.io.Serializable;



import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class Moneda implements Serializable{

	
	
	@NotEmpty(message = "Codigo moneda no puede ser vacia")
	@JsonProperty("codMoneda")
	//@Max(value = 3, message = "Codigo moneda debe tener maximo 3 caracteres")
	@Size(min = 1, max = 3 , message = "Codigo moneda debe tener maximo 3 caracteres")
	private String codMoneda;
	
	@JsonProperty("descripcion")
	private String descripcion;
	
	@NotEmpty(message = "Codigo alterno no puede ser vacia")
	@JsonProperty("codAlterno")
	//@Max(value = 3, message = "Codigo alterno debe tener maximo 3 caracteres")
	@Size(min = 1, max = 3 , message = "Codigo alterno debe tener maximo 3 caracteres")
	private String codAlterno;
	
	@JsonProperty("flagActivo")
	private Boolean flagActivo;
	
	@JsonProperty("codUsuario")
	private String codUsuario;
	
	@JsonProperty("fechaModificacion")
	private String fechaModificacion;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
