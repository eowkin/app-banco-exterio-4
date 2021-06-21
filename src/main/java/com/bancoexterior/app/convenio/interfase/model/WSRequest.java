package com.bancoexterior.app.convenio.interfase.model;

public class WSRequest {
	
	private String url;
	private String body;
	private String contenType;
	private int socketTimeout;
	private int connectTimeout;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getContenType() {
		return contenType;
	}
	public void setContenType(String contenType) {
		this.contenType = contenType;
	}
	public int getSocketTimeout() {
		return socketTimeout;
	}
	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	public int getConnectTimeout() {
		return connectTimeout;
	}
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WSRequest [url=");
		builder.append(url);
		builder.append(", body=");
		builder.append(body);
		builder.append(", contenType=");
		builder.append(contenType);
		builder.append(", socketTimeout=");
		builder.append(socketTimeout);
		builder.append(", connectTimeout=");
		builder.append(connectTimeout);
		builder.append("]");
		return builder.toString();
	}
	
	

}
