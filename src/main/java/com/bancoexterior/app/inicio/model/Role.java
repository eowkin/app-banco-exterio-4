package com.bancoexterior.app.inicio.model;

import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



//@Data @AllArgsConstructor @NoArgsConstructor
//@Builder
@Entity 
@Table(name = "\"role\"", schema = "\"public\"")
public class Role implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	
	@Column(name = "nombre", unique = true, length = 20)
	private String nombre;
	
	
	//@ManyToOne(fetch = FetchType.LAZY)
	//@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="role_menu", joinColumns = @JoinColumn(name="id")
	, inverseJoinColumns=@JoinColumn(name="id_menu"),
	uniqueConstraints= {@UniqueConstraint(columnNames = {"id", "id_menu"})})
	private List<Menu> menus;
	/*
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="role_menu", joinColumns = @JoinColumn(name="id")
	, inverseJoinColumns=@JoinColumn(name="id_menu"),
	uniqueConstraints= {@UniqueConstraint(columnNames = {"id", "id_menu"})})
	private List<Menu> menus;*/

	
	
	
	
	public Integer getId() {
		return id;
	}

	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	
	
	
	
}
