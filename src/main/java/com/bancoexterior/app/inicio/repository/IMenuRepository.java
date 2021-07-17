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
	
	
	public static final String SELECTCMENUGRUPOINNOMBRES ="WITH recursive grupo_menu as(\r\n"
			+ "SELECT T02.id_menu\r\n"
			+ "FROM monitor_financiero.grupos T01 inner join\r\n"
			+ "monitor_financiero.grupos_menu T02 on (T01.id_grupo=T02.id_grupo and t01.flag_activo= true and T01.nombre_grupo in(?1))\r\n"
			+ "inner join\r\n"
			+ "monitor_financiero.menu T03 on (t02.id_menu = t03.id_menu and t03.flag_activo = true)\r\n"
			+ "),\r\n"
			+ "menu_usuario_hijo_padre AS\r\n"
			+ "(\r\n"
			+ "    SELECT\r\n"
			+ "         T01.id_menu,  T01.nombre,  T01.nivel,  T01.orden,  T01.id_menu_padre,  T01.direccion,  T01.flag_activo\r\n"
			+ "        FROM monitor_financiero.menu T01\r\n"
			+ "        WHERE T01.id_menu in(SELECT id_menu from grupo_menu) and T01.flag_activo = true\r\n"
			+ "    UNION ALL\r\n"
			+ "        SELECT\r\n"
			+ "            T02.id_menu,  T02.nombre,  T02.nivel,  T02.orden,  T02.id_menu_padre,  T02.direccion,  T01.flag_activo\r\n"
			+ "        FROM menu_usuario_hijo_padre T01\r\n"
			+ "        INNER JOIN monitor_financiero.menu T02 ON (T02.id_menu=T01.id_menu_padre )and t02.flag_activo= true\r\n"
			+ "),\r\n"
			+ "menu_usuario_padre_hijo AS\r\n"
			+ "(\r\n"
			+ "    SELECT\r\n"
			+ "         T01.id_menu,  T01.nombre,  T01.nivel,  T01.orden,  T01.id_menu_padre,  T01.direccion,  T01.flag_activo\r\n"
			+ "        FROM monitor_financiero.menu T01\r\n"
			+ "        WHERE T01.id_menu in(SELECT id_menu from grupo_menu) and T01.flag_activo = true\r\n"
			+ "    UNION all\r\n"
			+ "        SELECT\r\n"
			+ "            T02.id_menu,  T02.nombre,  T02.nivel,  T02.orden,  T02.id_menu_padre,  T02.direccion,  T01.flag_activo\r\n"
			+ "        FROM monitor_financiero.menu T02\r\n"
			+ "        INNER JOIN menu_usuario_padre_hijo T01 ON (T01.id_menu=T02.id_menu_padre) and t02.flag_activo= true\r\n"
			+ "),\r\n"
			+ "menu_union as(\r\n"
			+ "SELECT id_menu,nombre,nivel,orden,id_menu_padre,direccion, flag_activo FROM menu_usuario_hijo_padre\r\n"
			+ "union\r\n"
			+ "SELECT id_menu,nombre,nivel,orden,id_menu_padre,direccion, flag_activo FROM menu_usuario_padre_hijo\r\n"
			+ "group by id_menu,nombre,nivel,orden,id_menu_padre,direccion, flag_activo\r\n"
			+ ")\r\n"
			+ "select id_menu,nombre,nivel,orden,id_menu_padre,direccion, flag_activo from menu_union\r\n"
			+ "where flag_activo = true\r\n"
			+ "order by nivel asc, orden asc";
	
	
	
	
	@Query(value = SELECTCMENUROLE, nativeQuery = true)
	public List<Menu> menuRole(int valores); 
	
	@Query(value = SELECTCMENUROLEIN, nativeQuery = true)
	public List<Menu> menuRoleIn(List<Integer>  valores);
	
	@Query(value = SELECTCMENUGRUPOINNOMBRES, nativeQuery = true)
	public List<Menu> menuNombreGrupoIn(List<String>  valores);
	
	
	
}
