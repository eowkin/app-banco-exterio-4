package com.bancoexterior.app.cce.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class Identificacion implements Serializable{
	
	@JsonProperty("Nm")
	@SerializedName("Nm")
	private String nm;
	
	@JsonProperty("Id")
	@SerializedName("Id")
	private String id;
	
	@JsonProperty("SchmeNm")
	@SerializedName("SchmeNm")
	private String schmeNm;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
