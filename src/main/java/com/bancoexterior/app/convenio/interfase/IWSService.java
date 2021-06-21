package com.bancoexterior.app.convenio.interfase;

import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;

public interface  IWSService {
	WSResponse post(WSRequest request);
	WSResponse put(WSRequest request);
}
