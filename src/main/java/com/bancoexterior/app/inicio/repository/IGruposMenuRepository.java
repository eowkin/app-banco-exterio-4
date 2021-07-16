package com.bancoexterior.app.inicio.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bancoexterior.app.inicio.model.GruposMenu;
import com.bancoexterior.app.inicio.model.GruposMenuPk;



public interface IGruposMenuRepository extends JpaRepository<GruposMenu, GruposMenuPk>{

	public static final String SELECTBORRARPORIDGRUPO ="DELETE FROM public.grupos_menu\r\n"
			+ "WHERE id_grupo = ?1";
	
	@Query(value = SELECTBORRARPORIDGRUPO, nativeQuery = true)
	public int borrarPorIdGrupo(int idGrupo);
	
}
