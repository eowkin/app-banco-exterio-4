package com.bancoexterior.app.inicio.service;

import java.util.List;

import com.bancoexterior.app.inicio.model.Menu;

public interface IMenuService {
	
	public List<Menu> findAll();
	
	public List<Menu> todoMenu();
	
	public List<Menu> todoMenuRole(int valores);
	
	public List<Menu> todoMenuRoleIn(List<Integer> valores);
	
	public List<Menu> todoMenuNombreGrupoIn(List<String>  valores);
	
}
