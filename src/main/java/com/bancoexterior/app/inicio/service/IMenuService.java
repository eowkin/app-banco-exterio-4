package com.bancoexterior.app.inicio.service;

import java.util.List;

import com.bancoexterior.app.inicio.model.Menu;

public interface IMenuService {
	
	public List<Menu> todoMenu();
	
	public List<Menu> todoMenuRole(int valores);
}
