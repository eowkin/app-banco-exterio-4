package com.bancoexterior.app.convenio.service;



import java.util.List;

import com.bancoexterior.app.convenio.dto.AgenciaRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Agencia;

public interface IAgenciaServiceApiRest {

	
	public List<Agencia> listaAgencias(AgenciaRequest agenciaRequest)throws CustomException;
	
	public Agencia buscarAgencia(AgenciaRequest agenciaRequest)throws CustomException;
	
	public String actualizar(AgenciaRequest agenciaRequest)throws CustomException;
	
	public String crear(AgenciaRequest agenciaRequest)throws CustomException;
	
}
