package com.bancoexterior.app.inicio.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor
@Builder
@Entity 
@Table(name = "\"menu\"", schema = "\"public\"")
public class Menu implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id_menu")
	private Integer idMenu;
	
	@Column(name = "nombre", unique = true, length = 20)
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
}
