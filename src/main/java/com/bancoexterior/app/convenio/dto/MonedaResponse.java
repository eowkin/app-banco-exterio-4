package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.response.Resultado;

import lombok.Data;


@Data
public class MonedaResponse implements Serializable{
	
	private Resultado resultado;
	
	private List<Moneda> monedas;

	public MonedaResponse(){
		super();
		this.resultado = new Resultado();
		this.monedas = new ArrayList<>();
	}
	
	public void addMonedas(Moneda moneda) {
		monedas.add(moneda);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
