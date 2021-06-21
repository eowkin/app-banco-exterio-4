package com.bancoexterior.app.convenio.service;


import java.util.List;


import com.bancoexterior.app.convenio.dto.TasaRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Tasa;


public interface ITasaServiceApiRest {

	
	public List<Tasa> listaTasas(TasaRequest tasaRequest) throws CustomException;
	
	public Tasa buscarTasa(TasaRequest tasaRequest) throws CustomException;
	
	public String actualizar(TasaRequest tasaRequest) throws CustomException;
	
	public String crear(TasaRequest tasaRequest) throws CustomException;
}
