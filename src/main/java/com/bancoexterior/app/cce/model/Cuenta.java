package com.bancoexterior.app.cce.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class Cuenta implements Serializable{

	@JsonProperty("Tp")
	@SerializedName("Tp")
	private String tp;
	
	@JsonProperty("Id")
	@SerializedName("Id")
	private String id;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
