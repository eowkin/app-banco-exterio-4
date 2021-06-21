package com.bancoexterior.app.convenio.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AcumuladosCompraVenta implements Serializable{
	

	@JsonProperty("compra")
	private List<Compra> compra;
	
	@JsonProperty("venta")
	private List<Venta> venta;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
