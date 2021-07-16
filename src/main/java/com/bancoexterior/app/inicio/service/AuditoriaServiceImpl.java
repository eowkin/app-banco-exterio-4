package com.bancoexterior.app.inicio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.inicio.model.Auditoria;
import com.bancoexterior.app.inicio.repository.IAuditoriaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuditoriaServiceImpl implements IAuditoriaService{

	@Autowired
	private IAuditoriaRepository repo;
	
	private static final String AUDITORIASERVICESAVEI = "[==== INICIO Auditoria save - Service ====]";
	
	private static final String AUDITORIASERVICESAVEF = "[==== FIN Auditoria save - Service ====]";
	
	@Override
	public Auditoria save(Auditoria auditoria) {
		log.info(AUDITORIASERVICESAVEI);
		log.info(AUDITORIASERVICESAVEI);
		return repo.save(auditoria);
	}

	@Override
	public Auditoria save(String codUsuario, String opcionMenu, String accion, String codRespuesta, boolean resultado, String detalle,
			String ip, int idMenu) {
		log.info(AUDITORIASERVICESAVEI);
		Auditoria auditoria = new Auditoria();
		auditoria.setCodUsuario(codUsuario);
		auditoria.setOpcionMenu(opcionMenu);
		auditoria.setAccion(accion);
		auditoria.setCodRespuesta(codRespuesta);
		auditoria.setResultado(true);
		auditoria.setDetalle(detalle);
		auditoria.setIpOrigen(ip);
		auditoria.setIdMenu(idMenu);
		log.info(AUDITORIASERVICESAVEF);
		return repo.save(auditoria);
	}

}
