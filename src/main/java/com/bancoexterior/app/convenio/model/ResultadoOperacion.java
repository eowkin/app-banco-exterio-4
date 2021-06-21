package com.bancoexterior.app.convenio.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor 
public class ResultadoOperacion implements Serializable{

	@JsonProperty("codOperacion")
	private String codOperacion;
	
	@JsonProperty("estatus")
	private Integer estatus;
	
	@JsonProperty("codigoRespuesta")
	private String codigoRespuesta;
	
	@JsonProperty("descripcion")
	private String descripcion;
	
	@JsonProperty("fechaLiquidacion")
	private String fechaLiquidacion;
	
	@JsonProperty("datosDebito")
	private DatosDebito datosDebito;
	
	@JsonProperty("datosCredito")
	private DatosCredito datosCredito;
	
	
	
	
	
	
	
	public ResultadoOperacion() {
		super();
		this.datosDebito = new DatosDebito();
		this.datosCredito = new DatosCredito();
		
	}





	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
