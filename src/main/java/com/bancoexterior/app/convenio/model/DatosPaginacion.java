package com.bancoexterior.app.convenio.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class DatosPaginacion implements Serializable{

	@JsonProperty("paginaActual")
	private Integer paginaActual;
	
	@JsonProperty("tamanoPagina")
	private Integer tamanoPagina;
	
	@JsonProperty("totalPaginas")
	private Integer totalPaginas;
	
	@JsonProperty("totalRegistros")
	private Integer totalRegistros;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
