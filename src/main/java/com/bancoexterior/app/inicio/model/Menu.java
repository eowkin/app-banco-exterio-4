package com.bancoexterior.app.inicio.model;


import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;







//@Data
@Entity 
//@Table(name = "\"menu\"", schema = "\"monitor_financiero\"")
@Table(name = "\"menu\"", schema = "\"monitor_financiero\"")
public class Menu implements Serializable{

	
	
	

	@Id
	@Column(name = "id_menu")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idMenu;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "nivel")
	private Integer nivel;
	
	@Column(name = "orden")
	private Integer orden;
	
	@Column(name = "direccion")
	private String direccion;
	
	@Column(name = "flag_activo")
	private boolean flagActivo;
	
	@ManyToOne
	@JoinColumn(name="id_menu_padre", referencedColumnName = "id_menu")
	private Menu menuPadre;
	
	@OneToMany(mappedBy = "menuPadre", fetch = FetchType.EAGER)
	private List<Menu> menuHijos;
	
	
	
	
	
	public Integer getIdMenu() {
		return idMenu;
	}





	public void setIdMenu(Integer idMenu) {
		this.idMenu = idMenu;
	}





	public String getNombre() {
		return nombre;
	}





	public void setNombre(String nombre) {
		this.nombre = nombre;
	}





	public Integer getNivel() {
		return nivel;
	}





	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}





	public Integer getOrden() {
		return orden;
	}





	public void setOrden(Integer orden) {
		this.orden = orden;
	}





	public String getDireccion() {
		return direccion;
	}





	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}





	public boolean isFlagActivo() {
		return flagActivo;
	}





	public void setFlagActivo(boolean flagActivo) {
		this.flagActivo = flagActivo;
	}





	public Menu getMenuPadre() {
		return menuPadre;
	}





	public void setMenuPadre(Menu menuPadre) {
		this.menuPadre = menuPadre;
	}





	public List<Menu> getMenuHijos() {
		return menuHijos;
	}





	public void setMenuHijos(List<Menu> menuHijos) {
		this.menuHijos = menuHijos;
	}





	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
