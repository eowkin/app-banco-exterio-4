package com.bancoexterior.app.inicio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bancoexterior.app.inicio.model.Grupo;



@Repository
public interface IGrupoRepository extends JpaRepository<Grupo, Integer>{
	
	public Grupo findByNombreGrupo(String nombre);
}
