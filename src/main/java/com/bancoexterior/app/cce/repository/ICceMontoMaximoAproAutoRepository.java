package com.bancoexterior.app.cce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bancoexterior.app.cce.model.CceMontoMaximoAproAuto;

public interface ICceMontoMaximoAproAutoRepository extends JpaRepository<CceMontoMaximoAproAuto, Integer>{
	public static final String SELECTCCEMONTOMAXIMOAPROAUTO ="SELECT id, monto\r\n"
			+ "FROM cce.monto_maximo_apro_automatica\r\n"
			+ "WHERE id = (select MAX(id) FROM cce.monto_maximo_apro_automatica)";
	
	
	
	@Query(value = SELECTCCEMONTOMAXIMOAPROAUTO, nativeQuery = true)
	public CceMontoMaximoAproAuto getMontoMaximoAproAutoActual();
}
