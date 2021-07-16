package com.bancoexterior.app.convenio.model;

import java.io.Serializable;
import java.math.BigDecimal;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor 
public class Movimiento implements Serializable{

	@JsonProperty("codOperacion")
	private String codOperacion;
	
	@JsonProperty("fechaOperacion")
	private String fechaOperacion;

	@JsonProperty("codigoIbs")
	private String codigoIbs;
	
	
	@JsonProperty("fechaValor")
	private String fechaValor;
	
	@JsonProperty("fechaPacto")
	private String fechaPacto;
	
	
	@JsonProperty("nroIdCliente")
	private String nroIdCliente;
	
	@JsonProperty("cuentaNacional")
	private String cuentaNacional;
	
	@JsonProperty("cuentaDivisa")
	private String cuentaDivisa;
	
	@JsonProperty("tipoTransaccion")
	private String tipoTransaccion;
	
	@JsonProperty("tipoTransaccionCliente")
	private String tipoTransaccionCliente;
	
	@JsonProperty("codMoneda")
	private String codMoneda;
	
	@JsonProperty("montoDivisa")
	private BigDecimal montoDivisa;
	
	@JsonProperty("montoBsCliente")
	private BigDecimal montoBsCliente;
	
	@JsonProperty("tasaCliente")
	private BigDecimal tasaCliente;
	
	@JsonProperty("tasaOperacion")
	private BigDecimal tasaOperacion;
	
	@JsonProperty("montoBsOperacion")
	private BigDecimal montoBsOperacion;
	
	@JsonProperty("referenciaDebito")
	private String referenciaDebito;
	
	@JsonProperty("referenciaCredito")
	private String referenciaCredito;
	 
	@JsonProperty("descripcion")
	private String descripcion;
	
	@JsonProperty("tipoPacto")
	private String tipoPacto;
	
	@JsonProperty("estatus")
	private Integer estatus;
	
	@JsonProperty("origenfondo")
	private String origenfondo;
	
	@JsonProperty("destinofondo")
	private String destinofondo;
	
	@JsonProperty("nombreCliente")
	private String nombreCliente;
	
	@JsonProperty("fechas")
	private Fechas fechas;
	
	private BigDecimal nuevaTasaCliente;
	
	private String fecha;
	
	private String fechaDesde;
	
	private String fechaHasta;
	
	private Integer paginaActual;
	
	private String tipoCliente;
	
	private String tasaClienteString;
	
	private String montoDivisaString;
	
	private String montoBsClienteString;
	
	private String montoBsOperacionString;
	
	public Movimiento() {
		this.fechas = new Fechas();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
