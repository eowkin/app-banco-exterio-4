package com.bancoexterior.app.inicio.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;


@Data
@Entity 
@Table(name = "\"auditoria\"", schema = "\"monitor_financiero\"")
public class Auditoria {

	@Id
	@Column(name = "id_auditoria", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idAuditoria;
	
	@Column(name = "fecha", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	
	@Column(name = "cod_usuario")
	private String codUsuario;
	
	@Column(name = "opcion_menu")
	private String opcionMenu;
	
	@Column(name = "accion")
	private String accion;
	
	@Column(name = "cod_respuesta")
	private String codRespuesta;
	
	@Column(name = "resultado")
	private boolean resultado;
	
	@Column(name = "detalle")
	private String detalle;
	
	@Column(name = "ip_origen")
	private String ipOrigen;
	
	
	@PrePersist
	public void prePersist() {
		setFecha(new Date());
	}
	
	
	
}
