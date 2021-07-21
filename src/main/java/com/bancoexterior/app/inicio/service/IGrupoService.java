package com.bancoexterior.app.inicio.service;

import java.util.List;

import com.bancoexterior.app.inicio.dto.GrupoDto;
import com.bancoexterior.app.inicio.model.Grupo;



public interface IGrupoService {

	public List<Grupo> findAll();
	
	public GrupoDto findById(int id);
	
	public Grupo findByNombre(String nombre);
	
	public Grupo findByNombreAndFlagActivo(String nombre, boolean flagActivo);
	
	public GrupoDto save(GrupoDto grupoDto);
	
	public void updateNombreGrupo(String nombreGrupo, String codUsuario, int id);
	
	public void updateActivarDesactivarGrupo(boolean flagActivo, String codUsuario, int id);
}
