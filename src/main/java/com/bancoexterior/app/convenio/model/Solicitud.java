package com.bancoexterior.app.convenio.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class Solicitud {

	private String codOperacion;
	
	private Date fechaOperacion;
	
	private String codigoIbs;
	
	private String nroIdCliente;
	
	private String cuentaNacional;
	
	private String cuentaDivisa;
	
	private String tipoTransaccion;
	
	private String codMoneda;
	
	private BigDecimal montoDivisa;
	
	private BigDecimal montoBs;
	
	private BigDecimal tasaCliente;
	
	private String referenciaDebito;
	
	private String referenciaCredito;
	
	private String descripcion;
	
	private Integer estatus; 
	
	
	
	
}
