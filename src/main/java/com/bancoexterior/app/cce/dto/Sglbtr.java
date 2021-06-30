package com.bancoexterior.app.cce.dto;

import java.io.Serializable;

import com.bancoexterior.app.cce.model.FIToFICstmrCdtTrfInitnDetalle;

import lombok.Data;


@Data
public class Sglbtr implements Serializable{
	
	
	private FIToFICstmrCdtTrfInitnDetalle fIToFICstmrCdtTrfInitn;

	public Sglbtr() {
		super();
		this.fIToFICstmrCdtTrfInitn = new FIToFICstmrCdtTrfInitnDetalle();
	} 
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
