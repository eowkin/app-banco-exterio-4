package com.bancoexterior.app.cce.service;



import com.bancoexterior.app.cce.dto.AprobacionesConsultasRequest;
import com.bancoexterior.app.cce.dto.AprobacionesConsultasResponse;
import com.bancoexterior.app.cce.dto.FiToFiCustomerCreditTransferRequest;
import com.bancoexterior.app.cce.model.BCVLBT;
import com.bancoexterior.app.convenio.exception.CustomException;

public interface IBcvlbtService {
	public AprobacionesConsultasResponse listaTransaccionesPorAporbarAltoValorPaginacion(AprobacionesConsultasRequest aprobacionesConsultasRequest) throws CustomException;
	
	public BCVLBT buscarBCVLBT(AprobacionesConsultasRequest aprobacionesConsultasRequest) throws CustomException;
	
	public void prueba(FiToFiCustomerCreditTransferRequest fiToFiCustomerCreditTransferRequest) throws CustomException;
}
