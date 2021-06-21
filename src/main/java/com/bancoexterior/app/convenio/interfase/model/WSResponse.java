package com.bancoexterior.app.convenio.interfase.model;

import org.springframework.web.client.HttpStatusCodeException;
import kong.unirest.HttpResponse;

public class WSResponse {
	
	private String statusText;
	private int status;
	private String body;
	private boolean exitoso;
	private HttpResponse<String> httpRetorno;
	private HttpStatusCodeException httpError;
	private Exception error;
	private int idConstructor;
	
	
	public WSResponse( HttpResponse<String> retorno) {
		this.body          = retorno.getBody();
		this.status        = retorno.getStatus();
		this.statusText    = retorno.getStatusText();
		this.exitoso       = true;
		this.httpRetorno   = retorno;
		this.httpError     = null;
		this.error         = null;
		this.idConstructor = 1;
	}
	
	public WSResponse( HttpStatusCodeException error) {
		this.body          = error.getResponseBodyAsString();
		this.status        = error.getRawStatusCode();
		this.statusText    = error.getStatusText();
		this.exitoso       = false;
		this.httpRetorno   = null;
		this.httpError     = error;
		this.error         = null;
		this.idConstructor = 2;
	}
	
	
	public WSResponse( Exception error) {
		this.body          = error.getMessage();
		this.status        = error.hashCode();
		this.statusText    = error.getLocalizedMessage();
		this.exitoso       = false;
		this.httpRetorno   = null;
		this.httpError     = null;
		this.error         = null;
		this.idConstructor = 3;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isExitoso() {
		return exitoso;
	}

	public void setExitoso(boolean exitoso) {
		this.exitoso = exitoso;
	}

	public HttpResponse<String> getHttpRetorno() {
		return httpRetorno;
	}

	public void setHttpRetorno(HttpResponse<String> httpRetorno) {
		this.httpRetorno = httpRetorno;
	}

	public HttpStatusCodeException getHttpError() {
		return httpError;
	}

	public void setHttpError(HttpStatusCodeException httpError) {
		this.httpError = httpError;
	}

	public Exception getError() {
		return error;
	}

	public void setError(Exception error) {
		this.error = error;
	}

	public int getIdConstructor() {
		return idConstructor;
	}

	public void setIdConstructor(int idConstructor) {
		this.idConstructor = idConstructor;
	}

	@Override
	public String toString() {
		return "WSResponse [statusText=" + statusText + ", status=" + status + ", body=" + body + ", exitoso=" + exitoso
				+ ", httpRetorno=" + httpRetorno + ", httpError=" + httpError + ", error=" + error + ", idConstructor="
				+ idConstructor + "]";
	}

	

}
