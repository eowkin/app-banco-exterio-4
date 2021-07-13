package com.bancoexterior.app.inicio.service;

import java.util.List;

import com.bancoexterior.app.inicio.model.Role;

public interface IRoleService {

	public List<Role> findAll();
	
	public Role findById(int id);
	
	public Role findByNombre(String nombre);
}
