package com.bancoexterior.app.cce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.model.CceMontoMaximoAproAuto;
import com.bancoexterior.app.cce.repository.ICceMontoMaximoAproAutoRepository;

@Service
public class CceMontoMaximoAproAutoServiceImpl implements ICceMontoMaximoAproAutoService{

	//@Autowired
	private ICceMontoMaximoAproAutoRepository repo;
	
	@Override
	public CceMontoMaximoAproAuto buscarMontoMaximoAproAutoActual() {
		return repo.getMontoMaximoAproAutoActual();
	}

}
