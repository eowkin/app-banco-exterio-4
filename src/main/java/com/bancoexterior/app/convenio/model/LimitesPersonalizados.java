package com.bancoexterior.app.convenio.model;

import java.io.Serializable;
import java.math.BigDecimal;


import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class LimitesPersonalizados implements Serializable{

	

	@JsonProperty("codigoIbs")
	@NotEmpty(message = "Codigo ibs no puede ser vacia")
	@Size(min = 1, max = 10, message = "El codigo ibs debe tener un maximo de 10 caracteres.")
	private String codigoIbs;
	
	@JsonProperty("codMoneda")
	@NotEmpty(message = "Codigo moneda no puede ser vacia")
	@Size(min = 1, max = 3 , message = "Codigo moneda debe tener maximo 3 caracteres")
	private String codMoneda;
	
	@JsonProperty("tipoTransaccion")
	@Size(min = 1, max = 1, message="El tipo transaccion debe tener un maximo de 1 caracter")
	@NotEmpty(message = "Tipo trasaccion no puede ser vacia")
	private String tipoTransaccion;
	
	@JsonProperty("montoMin")
	@NotNull(message = "El monto minimo no puede ser vacio")
	@Digits(integer=13, fraction=2, message = "El monto minimo deber ser numerico y tener maximo 13 digitos y 2 decimales")
	private BigDecimal montoMin;
	
	@JsonProperty("montoMax")
	@NotNull(message = "El monto maximo no puede ser vacio")
	@Digits(integer=13, fraction=2, message = "El monto maximo deber ser numerico y tener maximo 13 digitos y 2 decimales")
	private BigDecimal montoMax;
	
	@JsonProperty("montoTope")
	@NotNull(message = "El monto tope no puede ser vacio")
	@Digits(integer=13, fraction=2, message = "El monto tope deber ser numerico y tener maximo 13 digitos y 2 decimales")
	private BigDecimal montoTope;
	
	@JsonProperty("montoMensual")
	@NotNull(message = "El monto mensual no puede ser vacio")
	@Digits(integer=13, fraction=2, message = "El monto menseual deber ser numerico y tener maximo 13 digitos y 2 decimales")
	private BigDecimal montoMensual;
	
	@JsonProperty("montoDiario")
	@NotNull(message = "El monto diario no puede ser vacio")
	@Digits(integer=13, fraction=2, message = "El monto diario deber ser numerico y tener maximo 13 digitos y 2 decimales")
	private BigDecimal montoDiario;
	
	@JsonProperty("flagActivo")
	@NotNull(message = "El flag activo no puede ser vacio")
	private Boolean flagActivo;
	
	@JsonProperty("codUsuario")
	@NotNull(message = "")
	@Digits(integer=13, fraction=2, message = "")
	private String codUsuario;
	
	
	@JsonProperty("fechaModificacion")
	
	private String fechaModificacion;
	
	private String montoMinString;
	
	private String montoMaxString;
	
	private String montoTopeString;
	
	private String montoMensualString;
	
	private String montoDiarioString;
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
