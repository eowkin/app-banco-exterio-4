package com.bancoexterior.app.inicio.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;



import lombok.Data;

@Embeddable
@Data
public class GruposMenuPk implements Serializable{

	@Column(name= "id_grupo", nullable = false)
	private Integer idGrupoPk;
	@Column(name= "id_menu", nullable = false)
	private Integer idMenuPk;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
