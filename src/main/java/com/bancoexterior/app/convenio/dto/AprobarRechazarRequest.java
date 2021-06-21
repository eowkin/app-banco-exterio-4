package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class AprobarRechazarRequest implements Serializable{

	
	@JsonProperty("idSesion")
	private String idSesion;
	
	@JsonProperty("idUsuario")
	private String idUsuario;
	
	@JsonProperty("codUsuario")
	private String codUsuario;
	
	@JsonProperty("canal")
	private String canal;
	
	@JsonProperty("ip")
	private String ip;
	
	@JsonProperty("origen")
	private String origen;
	
	@JsonProperty("codSolicitud")
	private String codSolicitud;
	
	@JsonProperty("estatus")
	private Integer estatus;
	
	@JsonProperty("tasa")
	private BigDecimal tasa;
	
	@JsonProperty("fechaLiquidacion")
	private String fechaLiquidacion;
	
	@JsonProperty("tipoPacto")
	private String tipoPacto;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
