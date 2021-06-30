package com.bancoexterior.app.cce.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class ParamIdentificacion implements Serializable{
	
	

	@JsonProperty("IdSesion")
	@SerializedName("IdSesion")
	private String idSesion;
	
	@JsonProperty("IdUsuario")
	@SerializedName("IdUsuario")
	private String idUsuario;
	
	@JsonProperty("CodTransaccion")
	@SerializedName("CodTransaccion")
	private String codTransaccion;
	
	@JsonProperty("BancoReceptor")
	@SerializedName("BancoReceptor")
	private String bancoReceptor;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
