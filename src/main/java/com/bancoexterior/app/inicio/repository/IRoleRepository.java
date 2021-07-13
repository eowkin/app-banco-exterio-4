package com.bancoexterior.app.inicio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bancoexterior.app.inicio.model.Role;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer>{
	
	public Role findByNombre(String nombre);
}
