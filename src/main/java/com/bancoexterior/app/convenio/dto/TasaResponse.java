package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bancoexterior.app.convenio.model.Tasa;
import com.bancoexterior.app.convenio.response.Resultado;

import lombok.Data;




@Data
public class TasaResponse implements Serializable{

	private Resultado resultado;
	
	private List<Tasa> tasa;
	
	
	
	public TasaResponse() {
		super();
		this.resultado = new Resultado();
		this.tasa = new ArrayList<>();		
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
