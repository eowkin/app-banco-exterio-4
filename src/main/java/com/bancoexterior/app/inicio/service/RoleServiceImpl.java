package com.bancoexterior.app.inicio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bancoexterior.app.inicio.model.Role;
import com.bancoexterior.app.inicio.repository.IRoleRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoleServiceImpl implements IRoleService{

	@Autowired
	private IRoleRepository repo;
	
	@Override
	public List<Role> findAll() {
		return repo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Role findById(int id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Role findByNombre(String nombre) {
		log.info(nombre);
		Role role = repo.findByNombre(nombre);
		log.info("role: "+role);
		return role;
	}

}
