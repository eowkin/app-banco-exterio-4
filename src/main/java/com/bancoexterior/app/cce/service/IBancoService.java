package com.bancoexterior.app.cce.service;

import java.util.List;

import com.bancoexterior.app.cce.dto.BancoRequest;
import com.bancoexterior.app.cce.model.Banco;
import com.bancoexterior.app.convenio.exception.CustomException;

public interface IBancoService {

	public List<Banco> listaBancos (BancoRequest bancoRequest) throws CustomException;
	
	public Banco buscarBanco(BancoRequest bancoRequest) throws CustomException;
	
}
