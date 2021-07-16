package com.bancoexterior.app.cce.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bancoexterior.app.cce.model.CceTransaccion;

//@Repository
public interface ICceTransaccionRepository extends JpaRepository<CceTransaccion, String>{

	public static final String SELECTCCETRANSACCIONFECHAS ="SELECT endtoend_id, referencia, cod_transaccion, cuenta_origen, tipo_identificacion, "
			+ "numero_identificacion, monto, moneda, telefono_origen, alias_origen, concepto, canal, fecha_modificacion, banco_origen, alias_destino, "
			+ "cuenta_destino, telefono_destino, banco_destino, reverso, tipo_transaccion, nombre_banco_destino, estadobcv, rsnbcv, "
			+ "tipo_identificacion_destino, numero_identificacion_destino, beneficiario_origen, beneficiario_destino\r\n"
			+ "FROM cce.cce_transaccion\r\n"
			+ "where cod_transaccion = (case when ?1 = '' then cod_transaccion else ?1 end) \r\n"
			+ "and banco_destino = (case when ?2 = '' then banco_destino else ?2 end)\r\n"
			+ "and numero_identificacion = (case when ?3 = '' then numero_identificacion else ?3 end)\r\n"
			+ "and fecha_modificacion between to_timestamp(?4, 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp(?5, 'YYYY-MM-DD HH24:MI:SS')";
	
	
	public static final String SELECTCCETRANSACCIONFECHASCOUNT ="SELECT  count(cod_transaccion = (case when ?1 = '' then cod_transaccion else ?1 end)\r\n"
			+ "and banco_destino = (case when ?2 = '' then banco_destino else ?2 end)\r\n"
			+ "and numero_identificacion = (case when ?3 = '' then numero_identificacion else ?3 end)\r\n"
			+ "and fecha_modificacion between to_timestamp(?4, 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp(?5, 'YYYY-MM-DD HH24:MI:SS'))\r\n"
			+ "FROM cce.cce_transaccion\r\n"
			+ "where cod_transaccion = (case when ?1 = '' then cod_transaccion else ?1 end) \r\n"
			+ "and banco_destino = (case when ?2 = '' then banco_destino else ?2 end)\r\n"
			+ "and numero_identificacion = (case when ?3 = '' then numero_identificacion else ?3 end)\r\n"
			+ "and fecha_modificacion between to_timestamp(?4, 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp(?5, 'YYYY-MM-DD HH24:MI:SS')";
	
	
	
	
	public static final String SELECTCCETRANSACCIONFECHASPRUEBA ="SELECT endtoend_id, referencia, cod_transaccion, cuenta_origen, tipo_identificacion, "
			+ "numero_identificacion, monto, moneda, telefono_origen, alias_origen, concepto, canal, fecha_modificacion, banco_origen, alias_destino, "
			+ "cuenta_destino, telefono_destino, banco_destino, reverso, tipo_transaccion, nombre_banco_destino, estadobcv, rsnbcv, "
			+ "tipo_identificacion_destino, numero_identificacion_destino, beneficiario_origen, beneficiario_destino\r\n"
			+ "FROM cce.cce_transaccion\r\n"
			+ "where fecha_modificacion between to_timestamp(:fechaDesde, 'YYYY-MM-DD HH24:MI:SS') AND to_timestamp(:fechaHasta, 'YYYY-MM-DD HH24:MI:SS')";
	
	
	
	@Query(value = SELECTCCETRANSACCIONFECHAS,
		    countQuery = SELECTCCETRANSACCIONFECHASCOUNT,
		    nativeQuery = true)
	public Page<CceTransaccion> consultaMovimientosConFechas(String codTransaccion, String bancoDestino, String numeroIdentificacion, 
					String fechaDesde, String fechaHasta, Pageable pageable);
	
	
	
	
	
	
	
	
	
	
	@Query(value = SELECTCCETRANSACCIONFECHAS, nativeQuery = true)
	public List<CceTransaccion> consultaMovimientosConFechas(String codTransaccion, String bancoDestino, String numeroIdentificacion, 
																String fechaDesde, String fechaHasta);
	
	@Query(value = SELECTCCETRANSACCIONFECHASPRUEBA, nativeQuery = true)
	public List<CceTransaccion> consultaMovimientosConFechasPrueba(String fechaDesde, String fechaHasta);
	
	
	public List<CceTransaccion> findByCodTransaccion(String codTransaccion);
	
	
	
	
}
