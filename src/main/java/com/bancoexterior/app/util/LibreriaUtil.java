package com.bancoexterior.app.util;

import java.time.LocalDateTime;


import org.springframework.stereotype.Component;

@Component
public class LibreriaUtil {
	
	public String obtenerIdSesion() {
		LocalDateTime ahora = LocalDateTime.now();
		String valorAno = "";
		valorAno = ahora.getYear()+"";
		
		
		String valorMes = "";
		if(ahora.getMonthValue() < 10) {
			valorMes = "0"+ahora.getMonthValue();
		}else {
			valorMes = ""+ahora.getMonthValue();
		}
		
		
		String valorDia = "";
		if(ahora.getDayOfMonth() < 10) {
			valorDia = "0"+ahora.getDayOfMonth();
		}else {
			valorDia = ""+ahora.getDayOfMonth();
		}
		
		
		String valorHora = "";
		if(ahora.getHour() < 10) {
			valorHora = "0"+ahora.getHour();
		}else {
			valorHora = ""+ahora.getHour();
		}
		
		
		String valorMin = "";
		if(ahora.getMinute() < 10) {
			valorMin = "0"+ahora.getMinute();
		}else {
			valorMin = ""+ahora.getMinute();
		}
		
		
		String valorSeg = "";
		if(ahora.getSecond() < 10) {
			valorSeg = "0"+ahora.getSecond();
		}else {
			valorSeg = ""+ahora.getSecond();
		}
		
		
	
		return valorAno+valorMes+valorDia+valorHora+valorMin+valorSeg;
	}

}
