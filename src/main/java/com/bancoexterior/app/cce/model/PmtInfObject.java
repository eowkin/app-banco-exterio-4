package com.bancoexterior.app.cce.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class PmtInfObject implements Serializable{

	

	@JsonProperty("RegId")
	@SerializedName("RegId")
	private  int regId;
	
	@JsonProperty("EndToEndId")
	@SerializedName("EndToEndId")
	private  String endToEndId;
	
	@JsonProperty("ClrSysRef")
	@SerializedName("ClrSysRef")
	private  String clrSysRef;
	
	@JsonProperty("TxId")
	@SerializedName("TxId")
	private  String txId;
	
	@JsonProperty("Amount")
	@SerializedName("Amount")
	private  Moneda amount;
	
	@JsonProperty("Purp")
	@SerializedName("Purp")
	private  String purp;
	
	@JsonProperty("DbtrAgt")
	@SerializedName("DbtrAgt")
	private  String dbtrAgt;
	
	@JsonProperty("CdtrAgt")
	@SerializedName("CdtrAgt")
	private  String cdtrAgt;
	
	@JsonProperty("Dbtr")
	@SerializedName("Dbtr")
	private Identificacion dbtr;
	
	@JsonProperty("DbtrAcct")
	@SerializedName("Dbtr")
	private Cuenta dbtrAcct;
	
	@JsonProperty("DbtrAgtAcct")
	@SerializedName("Dbtr")
	private Cuenta dbtrAgtAcct;
	
	@JsonProperty("CdtrAcct")
	@SerializedName("CdtrAcct")
	private Cuenta cdtrAcct;
	
	@JsonProperty("CdtrAgtAcct")
	@SerializedName("CdtrAgtAcct")
	private Cuenta cdtrAgtAcct;
	
	@JsonProperty("RmtInf")
	@SerializedName("RmtInf")
	private  String rmtInf;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
