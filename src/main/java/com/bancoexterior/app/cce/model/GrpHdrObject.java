package com.bancoexterior.app.cce.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class GrpHdrObject implements Serializable{
	
	

	@JsonProperty("MsgId")
	@SerializedName("MsgId")
	private String msgId;
	
	@JsonProperty("CreDtTm")
	@SerializedName("CreDtTm")
	private String creDtTm;
	
	@JsonProperty("NbOfTxs")
	@SerializedName("NbOfTxs")
	private int nbOfTxs;
	
	@JsonProperty("CtrlSum")
	@SerializedName("CtrlSum")
	private Moneda ctrlSum;
	
	@JsonProperty("IntrBkSttlmDt")
	@SerializedName("IntrBkSttlmDt")
	private String intrBkSttlmDt;
	
	@JsonProperty("LclInstrm")
	@SerializedName("LclInstrm")
	private String lclInstrm;
	
	@JsonProperty("Channel")
	@SerializedName("Channel")
	private String channel;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
