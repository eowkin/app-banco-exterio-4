package com.bancoexterior.app.inicio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bancoexterior.app.inicio.model.Menu;

public interface IMenuRepository extends JpaRepository<Menu, Integer>{
	public static final String SELECTCCEMENUORDENADO ="SELECT id_menu, nombre, nivel, orden, id_menu_padre, direccion, flag_activo\r\n"
			+ "FROM monitor_financiero.menu\r\n"
			+ "order by nivel asc, orden asc ;";
	
	@Query(value = SELECTCCEMENUORDENADO, nativeQuery = true)
	public List<Menu> menuOrdenado();
	
	
	public static final String SELECTCMENUROLE ="with recursive menu_usuario as\r\n"
			+ "(\r\n"
			+ "	SELECT c.id_menu, c.nombre, c.nivel, c.orden, c.id_menu_padre, c.direccion, c.flag_activo\r\n"
			+ "	FROM monitor_financiero.menu c\r\n"
			+ "	where c.id_menu in (?1)\r\n"
			+ "	\r\n"
			+ "	union all \r\n"
			+ "	SELECT s.id_menu, s.nombre, s.nivel, s.orden, s.id_menu_padre, s.direccion, s.flag_activo\r\n"
			+ "	FROM menu_usuario t\r\n"
			+ "	inner join public.menu s on s.id_menu = t.id_menu_padre\r\n"
			+ ")\r\n"
			+ "\r\n"
			+ "SELECT id_menu, nombre, nivel, orden, id_menu_padre, direccion, flag_activo FROM menu_usuario\r\n"
			+ "where flag_activo = true \r\n"
			+ "group by id_menu, nombre, nivel, orden, id_menu_padre, direccion, flag_activo\r\n"
			+ "order by nivel asc, orden asc";
	
	
	public static final String SELECTCMENUROLEIN ="with recursive menu_usuario as\r\n"
			+ "(\r\n"
			+ "	SELECT c.id_menu, c.nombre, c.nivel, c.orden, c.id_menu_padre, c.direccion, c.flag_activo\r\n"
			+ "	FROM monitor_financiero.menu c\r\n"
			+ "	where c.id_menu in (?1)\r\n"
			+ "	\r\n"
			+ "	union all \r\n"
			+ "	SELECT s.id_menu, s.nombre, s.nivel, s.orden, s.id_menu_padre, s.direccion, s.flag_activo\r\n"
			+ "	FROM menu_usuario t\r\n"
			+ "	inner join monitor_financiero.menu s on s.id_menu = t.id_menu_padre\r\n"
			+ ")\r\n"
			+ "\r\n"
			+ "SELECT id_menu, nombre, nivel, orden, id_menu_padre, direccion, flag_activo FROM menu_usuario\r\n"
			+ "where flag_activo = true \r\n"
			+ "group by id_menu, nombre, nivel, orden, id_menu_padre, direccion, flag_activo\r\n"
			+ "order by nivel asc, orden asc";
	
	@Query(value = SELECTCMENUROLE, nativeQuery = true)
	public List<Menu> menuRole(int valores); 
	
	@Query(value = SELECTCMENUROLEIN, nativeQuery = true)
	public List<Menu> menuRoleIn(List<Integer>  valores);
	
}
