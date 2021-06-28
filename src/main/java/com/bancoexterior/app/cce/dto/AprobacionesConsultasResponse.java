package com.bancoexterior.app.cce.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bancoexterior.app.cce.model.BCVLBT;
import com.bancoexterior.app.cce.model.DatosPaginacion;
import com.bancoexterior.app.convenio.response.Resultado;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class AprobacionesConsultasResponse implements Serializable{

	
	@JsonProperty("resultado")
	private Resultado resultado;
	@JsonProperty("datosPaginacion")
	private DatosPaginacion datosPaginacion;
	@JsonProperty("operaciones")
	private List<BCVLBT> operaciones;
	
	public AprobacionesConsultasResponse (){
		
		this.resultado = new Resultado();
		this.datosPaginacion = new DatosPaginacion();
		this.operaciones  = new ArrayList<>();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
