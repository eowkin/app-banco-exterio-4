package com.bancoexterior.app.inicio.dto;


import java.util.Date;
import java.util.List;


import com.bancoexterior.app.inicio.model.Menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class GrupoDto {
	
	private Integer idGrupo;
	
	private String nombreGrupo;
	
	private String codUsuario;
	
	private boolean flagActivo;
	
	private List<Menu> menus;
	
	private Date fechaModificacion;
	
	private Date fechaIngreso;

}
