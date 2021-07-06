package com.bancoexterior.app.cce.model;

import java.io.Serializable;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class BCVLBT implements Serializable{
	
	@JsonProperty("referencia")
    private Integer referencia;
	
	@JsonProperty("fechaTransaccion")
    private String fechaTransaccion;
	
	@JsonProperty("idCanal")
	private String idCanal;
	
	@JsonProperty("monto")
	private BigDecimal monto;
	
	@JsonProperty("codMoneda")
	private String codMoneda;
	
	@JsonProperty("producto")
	private String producto;
	
	@JsonProperty("subProducto")
	private String subProducto;
	
	@JsonProperty("bancoReceptor")
	private String bancoReceptor;
	
	@JsonProperty("bancoEmisor")
	private String bancoEmisor;
	
	@JsonProperty("nroCuentaOrigen")
	private String nroCuentaOrigen;
	
	@JsonProperty("nroCuentaDestino")
	private String nroCuentaDestino;
	
	@JsonProperty("nroIdEmisor")
	private String nroIdEmisor;
	
	@JsonProperty("nombreEmisor")
	private String nombreEmisor;
	
	@JsonProperty("nroIdReceptor")
	private String nroIdReceptor;
	
	
	@JsonProperty("nombreReceptor")
	private String nombreReceptor;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("fechaActualizacion")
	private String fechaActualizacion;
	
	@JsonProperty("codTransaccion")
	private String codTransaccion;
	
	@JsonProperty("codUsuario")
	private String codUsuario;
	
	private String montoString;
	
	private String fechaDesde;
	
	private String fechaHasta;
	
	private String horaDesde;
	
	private String horaHasta;
	
	private BigDecimal montoDesde;
	
	private BigDecimal montoHasta;
	
	private boolean seleccionado;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
