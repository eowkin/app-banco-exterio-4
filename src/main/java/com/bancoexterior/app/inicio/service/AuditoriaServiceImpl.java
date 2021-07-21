package com.bancoexterior.app.inicio.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.inicio.model.Auditoria;
import com.bancoexterior.app.inicio.repository.IAuditoriaRepository;

@Service
public class AuditoriaServiceImpl implements IAuditoriaService{

	private static final Logger LOGGER = LogManager.getLogger(AuditoriaServiceImpl.class);
	
	@Autowired
	private IAuditoriaRepository repo;
	
	private static final String AUDITORIASERVICESAVEI = "[==== INICIO Save Auditoria - Service ====]";
	
	private static final String AUDITORIASERVICESAVEF = "[==== FIN Save Auditoria - Service ====]";
	
	
	@Override
	public Auditoria save(String codUsuario, String opcionMenu, String accion, String codRespuesta, boolean resultado,
			String detalle, String ipOrigen) {
		LOGGER.info(AUDITORIASERVICESAVEI);
		Auditoria auditoria = new Auditoria();
		auditoria.setCodUsuario(codUsuario);
		auditoria.setOpcionMenu(opcionMenu);
		auditoria.setAccion(accion);
		auditoria.setCodRespuesta(codRespuesta);
		auditoria.setResultado(resultado);
		auditoria.setDetalle(detalle);
		auditoria.setIpOrigen(ipOrigen);
		LOGGER.info(AUDITORIASERVICESAVEF);
		return repo.save(auditoria);
	}

}
