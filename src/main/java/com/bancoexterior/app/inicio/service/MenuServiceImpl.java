package com.bancoexterior.app.inicio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import com.bancoexterior.app.inicio.model.Menu;
import com.bancoexterior.app.inicio.repository.IMenuRepository;

@Service
public class MenuServiceImpl implements IMenuService{

	@Autowired
	IMenuRepository repo;
	
	@Override
	@Transactional(readOnly = true)
	public List<Menu> todoMenu() {
		return repo.menuOrdenado();
	}

	@Override
	public List<Menu> todoMenuRole(String valores) {
		return repo.menuRole(valores);
	}

}
