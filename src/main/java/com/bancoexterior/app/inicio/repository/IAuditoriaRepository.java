package com.bancoexterior.app.inicio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bancoexterior.app.inicio.model.Auditoria;

@Repository
public interface IAuditoriaRepository extends JpaRepository<Auditoria, Integer>{

	
	
}
