package com.bancoexterior.app.cce.dto;

import java.io.Serializable;

import com.bancoexterior.app.cce.model.ParamIdentificacion;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class FiToFiCustomerCreditTransferRequest implements Serializable{

	@JsonProperty("ParamIdentificacion")
	@SerializedName("ParamIdentificacion")
	private ParamIdentificacion paramIdentificacion;
	
	@JsonProperty("GrpHdr")
	@SerializedName("GrpHdr")
	private Sglbtr sglbtr; 
	
	
	
	
	public FiToFiCustomerCreditTransferRequest() {
		super();
		this.paramIdentificacion = new ParamIdentificacion();
		this.sglbtr = new Sglbtr();
	}






	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

}
