package com.bancoexterior.app.inicio.service;

import com.bancoexterior.app.inicio.model.Auditoria;

public interface IAuditoriaService {
	public Auditoria save(String codUsuario, String opcionMenu, String accion, String codRespuesta, boolean resultado, String detalle, String ipOrigen);
}
