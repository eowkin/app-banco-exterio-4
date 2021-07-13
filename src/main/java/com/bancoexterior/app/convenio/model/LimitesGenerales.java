package com.bancoexterior.app.convenio.model;

import java.io.Serializable;
import java.math.BigDecimal;


import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class LimitesGenerales implements Serializable{
	
	@JsonProperty("codMoneda")
	@NotEmpty(message = "Codigo moneda no puede ser vacia")
	@Size(min = 1, max = 3 , message = "Codigo moneda debe tener maximo 3 caracteres")
	private String codMoneda;
	
	@JsonProperty("tipoTransaccion")
	@NotEmpty(message = "Codigo moneda no puede ser vacia")
	@Size(min = 1, max = 1 , message = "Tipo transaccion debe tener maximo 1 caracteres")
	private String tipoTransaccion;
	
	@JsonProperty("tipoCliente")
	@NotEmpty(message = "Codigo moneda no puede ser vacia")
	@Size(min = 1, max = 1 , message = "Tipo cliente debe tener maximo 1 caracteres")
	private String tipoCliente;
	
	@JsonProperty("montoMin")
	@Digits(integer=13, fraction=2, message = "El monte minimo debe ser numerico y tener maximo 13 digitos y 2 decimales")
	private BigDecimal montoMin;
	
	@JsonProperty("montoMax")
	@Digits(integer=13, fraction=2, message = "El monte maximo debe ser numerico y tener maximo 13 digitos y 2 decimales")
	private BigDecimal montoMax;
	
	@JsonProperty("montoTope")
	@Digits(integer=13, fraction=2, message = "El monte tope debe ser numerico y tener maximo 13 digitos y 2 decimales")
	private BigDecimal montoTope;
	
	@JsonProperty("montoMensual")
	@Digits(integer=13, fraction=2, message = "El monte mensual debe ser numerico y tener maximo 13 digitos y 2 decimales")
	private BigDecimal montoMensual;
	
	@JsonProperty("montoDiario")
	@Digits(integer=13, fraction=2, message = "El monte diario debe ser numerico y tener maximo 13 digitos y 2 decimales")
	private BigDecimal montoDiario;
	
	@JsonProperty("montoBanco")
	@Digits(integer=13, fraction=2, message = "El monte banco debe ser numerico y tener maximo 13 digitos y 2 decimales")
	private BigDecimal montoBanco;
	
	@JsonProperty("codUsuario")
	private String codUsuario;
	
	@JsonProperty("flagActivo")
	private Boolean flagActivo;
	
	
	@JsonProperty("fechaModificacion")
	private String fechaModificacion;
	
	private String montoMinString;
	
	private String montoMaxString;
	
	private String montoTopeString;
	
	private String montoMensualString;
	
	private String montoDiarioString;
	
	private String montoBancoString;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


}
