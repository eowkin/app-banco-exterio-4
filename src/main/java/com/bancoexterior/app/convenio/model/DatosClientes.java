package com.bancoexterior.app.convenio.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class DatosClientes implements Serializable{

	@JsonProperty("codIbs")
	private String codIbs;
	
	@JsonProperty("nroIdCliente")
	private String nroIdCliente;
	
	@JsonProperty("tipoIdentificacion")
	private String tipoIdentificacion;
	
	@JsonProperty("codigoPais")
	private String codigoPais;
	
	@JsonProperty("identificacionFiscal")
	private String identificacionFiscal;
	
	@JsonProperty("codigoTipoIdentificacion")
	private String codigoTipoIdentificacion;
	
	@JsonProperty("codigoPaisIdentificacion")
	private String codigoPaisIdentificacion;
	
	@JsonProperty("tipoLegal")
	private String tipoLegal;
	
	@JsonProperty("empleado")
	private String empleado;
	
	@JsonProperty("nombreCorto")
	private String nombreCorto;
	
	@JsonProperty("estatusCliente")
	private String estatusCliente;
	
	@JsonProperty("nombreLegal")
	private String nombreLegal;
	
	
	@JsonProperty("direccion1")
	private String direccion1;
	
	@JsonProperty("direccion2")
	private String direccion2;
	
	@JsonProperty("direccion3")
	private String direccion3;
	
	@JsonProperty("ciudad")
	private String ciudad;
	
	@JsonProperty("codigoEstado")
	private String codigoEstado;
	
	@JsonProperty("codigoArea")
	private String codigoArea;
	
	@JsonProperty("numeroApartadoPostal")
	private String numeroApartadoPostal;
	
	@JsonProperty("pais")
	private String pais;
	
	@JsonProperty("codigoCiudadania")
	private String codigoCiudadania;
	
	@JsonProperty("tipoEmpresaoSexo")
	private String tipoEmpresaoSexo;
	
	@JsonProperty("estadoCivil")
	private String estadoCivil;
	
	@JsonProperty("numeroDeDependientes")
	private String numeroDeDependientes;
	
	
	@JsonProperty("claseCliente")
	private String claseCliente;
	
	
	@JsonProperty("tipoImpuestos")
	private String tipoImpuestos;
	
	@JsonProperty("mesNacimiento")
	private String mesNacimiento;
	
	@JsonProperty("diaNacimiento")
	private String diaNacimiento;
	
	@JsonProperty("anioNacimiento")
	private String anioNacimiento;
	
	@JsonProperty("mesInicioCliente")
	private String mesInicioCliente;
	
	@JsonProperty("diaInicioCliente")
	private String diaInicioCliente;
	
	@JsonProperty("anioInicioCliente")
	private String anioInicioCliente;
	
	@JsonProperty("nivelEducacion")
	private String nivelEducacion;
	
	@JsonProperty("profesionCodigoIndustria")
	private String profesionCodigoIndustria;
	
	@JsonProperty("areaCodigoNegocio")
	private String areaCodigoNegocio;
	
	@JsonProperty("codigoFuenteIngreso")
	private String codigoFuenteIngreso;
	
	@JsonProperty("codigoPostal")
	private String codigoPostal;
	
	@JsonProperty("tipoCliente")
	private String tipoCliente;
	
	@JsonProperty("numeroCasa")
	private String numeroCasa;
	
	@JsonProperty("numeroOficina")
	private String numeroOficina;
	
	@JsonProperty("numeroCelular")
	private String numeroCelular;
	
	@JsonProperty("celularNotificaciones")
	private String celularNotificaciones;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("nombre")
	private String nombre;
	
	@JsonProperty("primerApellido")
	private String primerApellido;
	
	@JsonProperty("segundoApellido")
	private String segundoApellido;
	
	@JsonProperty("nombreClave")
	private String nombreClave;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
