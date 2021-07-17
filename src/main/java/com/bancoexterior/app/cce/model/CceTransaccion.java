package com.bancoexterior.app.cce.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "\"cce_transaccion\"", schema = "\"cce\"")
public class CceTransaccion {
	
	@Id
	@Column(name = "endtoend_id", nullable = false)
	@Size(max = 28)
	private String endtoendId;
	
	@Column(name = "referencia", nullable = false)
	@Size(max = 8)
	private String referencia;
	
	@Column(name = "cod_transaccion", nullable = false)
	@Size(max = 6)
	private String codTransaccion;
	
	@Column(name = "cuenta_origen", nullable = false)
	@Size(max = 20)
	private String cuentaOrigen;
	
	@Column(name = "tipo_identificacion", nullable = false)
	@Size(max = 1)
	private String tipoIdentificacion;
	
	@Column(name = "numero_identificacion", nullable = false)
	@Size(max = 10)
	private String numeroIdentificacion;
	
	@Column(name = "monto", nullable = false)
	//@Column(nullable = false, precision = 19, scale = 6)
	@Digits(integer=13, fraction=2)
	//@NumberFormat(pattern = "##,###.##")
	//@NumberFormat(style = NumberFormat.Style.CURRENCY)
	private BigDecimal monto;
	
	@Column(name = "moneda", nullable = false)
	@Size(max = 5)
	private String moneda;
	
	@Column(name = "telefono_origen")
	@Size(max = 15)
	private String telefonoOrigen;
	
	@Column(name = "alias_origen")
	@Size(max = 30)
	private String aliasOrigen;
	
	@Column(name = "concepto")
	@Size(max = 140)
	private String concepto;
	
	@Column(name = "canal")
	@Size(max = 6)
	private String canal;
	
	@Column(name = "banco_origen")
	@Size(max = 4)
	private String bancoOrigen;
	
	@Column(name = "alias_destino")
	@Size(max = 30)
	private String aliasDestino;
	
	@Column(name = "cuenta_destino")
	@Size(max = 20)
	private String cuentaDestino;
	
	@Column(name = "telefono_destino")
	@Size(max = 15)
	private String telefonoDestino;
	
	@Column(name = "banco_destino")
	@Size(max = 4)
	private String bancoDestino;
	
	@Column(name = "reverso")
	@Size(max = 1)
	private String reverso;
	
	@Column(name = "tipo_transaccion")
	@Size(max = 5)
	private String tipoTransaccion;
	
	@Column(name = "nombre_banco_destino")
	@Size(max = 140)
	private String nombreBancoDestino;
	
	@Column(name = "estadobcv")
	@Size(max = 4)
	private String estadobcv;
	
	@Column(name = "rsnbcv")
	@Size(max = 4)
	private String rsnbcv;
	
	@Column(name = "tipo_identificacion_destino")
	@Size(max = 1)
	private String tipoIdentificacionDestino;
	
	@Column(name = "numero_identificacion_destino")
	@Size(max = 10)
	private String numeroIdentificacionDestino;
	
	@Column(name = "beneficiario_origen")
	@Size(max = 50)
	private String beneficiarioOrigen;
	
	@Column(name = "beneficiario_destino")
	@Size(max = 50)
	private String beneficiarioDestino;
	
	@Column(name = "fecha_modificacion", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaModificacion;
	
	
	@Transient
	private String montoString;

}
