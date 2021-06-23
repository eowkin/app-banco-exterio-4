package com.bancoexterior.app.cce.model;

import java.io.Serializable;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class Banco implements Serializable{

	
	private String codBanco;
	private String nbBanco;
	private String nbSiglas;
	private String status;
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
