package com.bancoexterior.app.cce.model;

import java.math.BigDecimal;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "\"monto_maximo_apro_automatica\"", schema = "\"cce\"")
public class CceMontoMaximoAproAuto {
	
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "monto", nullable = false)
	//@Column(nullable = false, precision = 19, scale = 6)
	@Digits(integer=13, fraction=2)
	//@NumberFormat(pattern = "##,###.##")
	//@NumberFormat(style = NumberFormat.Style.CURRENCY)
	private BigDecimal monto;
}
