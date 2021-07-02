package com.bancoexterior.app.cce.dto;

import java.io.Serializable;

import com.bancoexterior.app.cce.model.FIToFICstmrCdtTrfInitnDetalle;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.Data;


@Data
public class Sglbtr implements Serializable{
	
	@JsonProperty("FIToFICstmrCdtTrfInitn")
	@SerializedName("FIToFICstmrCdtTrfInitn")
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
