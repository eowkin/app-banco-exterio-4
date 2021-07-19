package com.bancoexterior.app.inicio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bancoexterior.app.inicio.model.Menu;
import com.bancoexterior.app.inicio.repository.IMenuRepository;

@Service
public class MenuServiceImpl implements IMenuService{

	@Autowired
	IMenuRepository repo;
	
	@Override
	public List<Menu> todoMenu() {
		return repo.menuOrdenado();
	}

	@Override
	public List<Menu> todoMenuRole(int valores) {
		return repo.menuRole(valores);
	}

	@Override
	public List<Menu> todoMenuRoleIn(List<Integer> valores) {
		
		return repo.menuRoleIn(valores);
	}

	@Override
	public List<Menu> findAll() {
		
		return repo.findAll();
	}

	@Override
	public List<Menu> todoMenuNombreGrupoIn(List<String> valores) {
		
		return repo.menuNombreGrupoIn(valores);
	}

}
