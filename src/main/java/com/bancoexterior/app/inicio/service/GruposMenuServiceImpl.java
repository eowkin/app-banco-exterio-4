package com.bancoexterior.app.inicio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.inicio.model.GruposMenu;
import com.bancoexterior.app.inicio.model.GruposMenuPk;
import com.bancoexterior.app.inicio.repository.IGruposMenuRepository;



@Service
public class GruposMenuServiceImpl implements IGruposMenuService{

	@Autowired
	private IGruposMenuRepository repo;
	
	
	@Override
	public void borrarRealcion(GruposMenuPk id) {
		if(repo.existsById(id))
			repo.deleteById(id);
		
	}

	@Override
	public GruposMenu guardarGrupoMenus(GruposMenu gruposMenu) {
		
		return repo.save(gruposMenu);
	}

	@Override
	public int borrarPorIdGrupo(int idGrupo) {
		return repo.borrarPorIdGrupo(idGrupo);
		
	}

}
