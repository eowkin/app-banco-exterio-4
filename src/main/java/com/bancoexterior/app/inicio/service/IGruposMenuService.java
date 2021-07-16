package com.bancoexterior.app.inicio.service;

import com.bancoexterior.app.inicio.model.GruposMenu;
import com.bancoexterior.app.inicio.model.GruposMenuPk;

public interface IGruposMenuService {
	
	public void borrarRealcion(GruposMenuPk id);
	
	public int borrarPorIdGrupo(int idGrupo);
	
	public GruposMenu guardarGrupoMenus(GruposMenu gruposMenu);
}
