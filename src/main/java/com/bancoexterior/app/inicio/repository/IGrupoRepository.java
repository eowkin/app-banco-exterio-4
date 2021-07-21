package com.bancoexterior.app.inicio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bancoexterior.app.inicio.model.Grupo;



@Repository
public interface IGrupoRepository extends JpaRepository<Grupo, Integer>{
	public static final String SELECTUPDATEGRUPO = "UPDATE monitor_financiero.grupos\r\n"
			+ "SET  nombre_grupo=?1, cod_usuario=?2, fecha_modificacion = current_timestamp\r\n"
			+ "WHERE id_grupo=?3";
	
	
	
	@Modifying
	@Transactional
	@Query(value = SELECTUPDATEGRUPO, nativeQuery = true)
	public void updateNombreGrupo(String nombreGrupo, String codUsuario, int id);
	
	
	
	public static final String UPDATEACTIVARDESACTIVARGRUPO = "UPDATE monitor_financiero.grupos\r\n"
			+ "SET  flag_activo=?1, cod_usuario=?2, fecha_modificacion = current_timestamp\r\n"
			+ "WHERE id_grupo=?3";
	
	
	
	@Modifying
	@Transactional
	@Query(value = UPDATEACTIVARDESACTIVARGRUPO, nativeQuery = true)
	public void updateEditarFlagGrupo(boolean flagActivo, String codUsuario, int id);
	
	
	public Grupo findByNombreGrupo(String nombre);

	public Grupo findByNombreGrupoAndFlagActivo(String nombre, boolean flagActivo);
	
	
	
	
	
}
