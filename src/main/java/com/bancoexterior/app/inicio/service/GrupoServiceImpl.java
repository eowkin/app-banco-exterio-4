package com.bancoexterior.app.inicio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bancoexterior.app.inicio.dto.GrupoDto;
import com.bancoexterior.app.inicio.model.Grupo;
import com.bancoexterior.app.inicio.repository.IGrupoRepository;
import com.bancoexterior.app.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GrupoServiceImpl implements IGrupoService{

	@Autowired
	private IGrupoRepository repo;
	
	@Autowired
	private Mapper mapper;
	
	@Override
	public List<Grupo> findAll() {
		return repo.findAll();
	}

	@Override
	public GrupoDto findById(int id) {
		
		Grupo grupo = repo.findById(id).orElse(null);
		if(grupo != null) {
			return mapper.map(grupo, GrupoDto.class);
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Grupo findByNombre(String nombre) {
		return repo.findByNombreGrupo(nombre);
	}

	@Override
	public GrupoDto save(GrupoDto grupoDto) {
		log.info("[---------------------GrupoServiceImpl - Antes del Save------------------ ]");
		log.info("getIdGrupo(): "+grupoDto.getIdGrupo());
		log.info("getNombreGrupo(): "+grupoDto.getNombreGrupo());
		log.info("grupoDto.getCodUsuario(): "+grupoDto.getCodUsuario());
		log.info("getFechaIngreso(): "+grupoDto.getFechaIngreso());
		log.info("grupoDto.getFechaModificacion(): "+grupoDto.getFechaModificacion());
		log.info("grupoDto.getMenus(): "+grupoDto.getMenus());
		Grupo grupo = mapper.map(grupoDto, Grupo.class);
		
		log.info("[---------------------GrupoServiceImpl - Luego del mapper------------------ ]");
		log.info("grupo.getIdGrupo(): "+grupo.getIdGrupo());
		log.info("grupo.getNombreGrupo(): "+grupo.getNombreGrupo());
		log.info("grupo.getCodUsuario(): "+grupo.getCodUsuario());
		log.info("grupo.getFechaIngreso(): "+grupo.getFechaIngreso());
		log.info("grupo.getFechaModificacion(): "+grupo.getFechaModificacion());
		log.info("grupo.getMenus(): "+grupo.getMenus());
		
		Grupo grupoSave = repo.save(grupo);
		
		if(grupoSave != null) {
			return mapper.map(grupoSave, GrupoDto.class);
		}
		return null;
		
	}

	@Override
	public void updateNombreGrupo(String nombreGrupo, String codUsuario, int id) {
		repo.updateNombreGrupo(nombreGrupo, codUsuario,id);
		
	}

	@Override
	public void updateActivarDesactivarGrupo(boolean flagActivo, String codUsuario, int id) {
		repo.updateEditarFlagGrupo(flagActivo, codUsuario,id);
		
	}

	@Override
	public Grupo findByNombreAndFlagActivo(String nombre, boolean flagActivo) {
		return repo.findByNombreGrupoAndFlagActivo(nombre, flagActivo);
	}

}
