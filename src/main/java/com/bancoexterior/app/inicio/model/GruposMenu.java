package com.bancoexterior.app.inicio.model;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;



@Data
@Entity 
@Table(name = "\"grupos_menu\"", schema = "\"monitor_financiero\"")
public class GruposMenu {

	@EmbeddedId
	private GruposMenuPk idPk;
	
	@Column(name = "cod_usuario")
	private String codUsuario;
	
	@Column(name = "fecha_modificacion", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaModificacion;
	
	@Column(name = "fecha_ingreso", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaIngreso;
	

	
	
	@PrePersist
	public void prePersist() {
		setFechaIngreso(new Date());
		setFechaModificacion(new Date());
	}
	
	@PreUpdate
	public void preUpdate() {
		setFechaModificacion(new Date());
	}
}
