package com.bancoexterior.app.inicio.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;







@Entity 
@Table(name = "\"grupos\"", schema = "\"monitor_financiero\"")
public class Grupo {

	@Id
	@Column(name = "id_grupo", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idGrupo;
	
	@Column(name = "nombre_grupo")
	private String nombreGrupo;
	
	@Column(name = "cod_usuario")
	private String codUsuario;
	
	@Column(name = "flag_activo")
	private boolean flagActivo;
	
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "monitor_financiero.grupos_menu",
	    joinColumns = @JoinColumn(name = "id_grupo"),
	    inverseJoinColumns = @JoinColumn(name = "id_menu"))
	private List<Menu> menus;
	
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

	public Grupo() {
		super();
		menus = new ArrayList<>();
	}

	public Integer getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(Integer idGrupo) {
		this.idGrupo = idGrupo;
	}

	public String getNombreGrupo() {
		return nombreGrupo;
	}

	public void setNombreGrupo(String nombreGrupo) {
		this.nombreGrupo = nombreGrupo;
	}

	public String getCodUsuario() {
		return codUsuario;
	}

	public void setCodUsuario(String codUsuario) {
		this.codUsuario = codUsuario;
	}

	public boolean isFlagActivo() {
		return flagActivo;
	}

	public void setFlagActivo(boolean flagActivo) {
		this.flagActivo = flagActivo;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	
	
	
	
	
}
