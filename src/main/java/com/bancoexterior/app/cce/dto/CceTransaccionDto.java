package com.bancoexterior.app.cce.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class CceTransaccionDto implements Serializable{
	
	private String endtoendId;
	
	private String referencia;
	
	private String codTransaccion;
	
	private String nombreTransaccion;
	
	private String cuentaOrigen;
	
	private String tipoIdentificacion;
	
	private String numeroIdentificacion;
	
	private BigDecimal monto;
	
	private String moneda;
	
	private String telefonoOrigen;
	
	private String aliasOrigen;
	
	private String concepto;
	
	private String canal;
	
	private String bancoOrigen;
	
	private String aliasDestino;
	
	private String cuentaDestino;
	
	private String telefonoDestino;
	
	private String bancoDestino;
	
	private String reverso;
	
	private String tipoTransaccion;
	
	private String nombreBancoDestino;
	
	private String estadobcv;
	
	private String nombreEstadoBcv;
	
	private String rsnbcv;
	
	private String tipoIdentificacionDestino;
	
	private String numeroIdentificacionDestino;
	
	private String beneficiarioOrigen;
	
	private String beneficiarioDestino;
	
	private Date fechaModificacion;
	
	private String fechaDesde;
	
	private String fechaHasta;
	
	private String horaDesde;
	
	private String horaHasta;
	
	private String bancoBenefiaciario;
	
	private int numeroAprobacionesLotes;
	
	private BigDecimal montoAprobacionesLotes;
	
	private BigDecimal montoDesde;
	
	private BigDecimal montoHasta;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
