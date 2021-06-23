package com.bancoexterior.app.cce.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bancoexterior.app.cce.model.Banco;
import com.bancoexterior.app.convenio.response.Resultado;

import lombok.Data;


@Data
public class BancoResponse implements Serializable{

	
	private Resultado resultado;
	
	private List<Banco> lisBancos;
	
	
	
	public BancoResponse() {
		super();
		this.resultado = new Resultado();
		this.lisBancos = new ArrayList<>();
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
