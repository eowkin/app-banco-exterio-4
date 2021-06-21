package com.bancoexterior.app.convenio.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor 
public class AcumuladosPorAprobar implements Serializable{

	

	@JsonProperty("compra")
	private List<Compra> compra;
	
	@JsonProperty("venta")
	private List<Venta> venta;
	
	public AcumuladosPorAprobar() {
		super();
		this.compra = new ArrayList<>();
		this.venta = new ArrayList<>();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
