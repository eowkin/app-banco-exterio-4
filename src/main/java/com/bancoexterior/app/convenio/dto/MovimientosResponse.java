package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bancoexterior.app.convenio.model.DatosPaginacion;
import com.bancoexterior.app.convenio.model.Movimiento;
import com.bancoexterior.app.convenio.response.Resultado;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class MovimientosResponse implements Serializable{

	@JsonProperty("resultado")
	private Resultado resultado;
	
	@JsonProperty("datosPaginacion")
	private DatosPaginacion datosPaginacion;
	
	@JsonProperty("movimientos")
	private List<Movimiento> movimientos;
	
	
	
	
	public MovimientosResponse() {
		super();
		this.resultado = new Resultado();
		this.datosPaginacion = new DatosPaginacion();
		this.movimientos = new ArrayList<>();
		
	}




	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

}
