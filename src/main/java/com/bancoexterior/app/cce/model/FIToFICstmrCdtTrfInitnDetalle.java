package com.bancoexterior.app.cce.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class FIToFICstmrCdtTrfInitnDetalle implements Serializable{

	
	@JsonProperty("GrpHdr")
	@SerializedName("GrpHdr")
	private GrpHdrObject grpHdr;
	
	@JsonProperty("PmtInf")
	@SerializedName("PmtInf")
	private List<PmtInfObject> pmtInf;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
