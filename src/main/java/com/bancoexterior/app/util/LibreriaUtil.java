package com.bancoexterior.app.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
	
	public String obtenerFechaHoy() {
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
		
		
		return valorDia+"/"+valorMes+"/"+valorAno;
	}

	
	/**
	* Convierte un tipo de dato String a BigDecimal.
	* Ideal para obtener el dato de un JTextField u otro componente y realizar las operaciones
	* matemáticas sobre ese dato.
	* @param num
	* @return BigDecimal
	*/
	public static BigDecimal stringToBigDecimal(String num)
	{
	//se inicializa en 0
	BigDecimal money = BigDecimal.ZERO;
	//sino esta vacio entonces
	if(!num.isEmpty())
	{
	/**
	* primero elimina los puntos y luego remplaza las comas en puntos.
	*/
	String formatoValido = num.replace(".", "").replace(",", ".");
	//System.out.println(formatoValido);
	money = new BigDecimal(formatoValido);
	}//if
	return money;
	}//metodo
	/**
	* Convierte un tipo de dato BigDecimal a String.
	* Ideal para mostrar el dato BigDecimal en un JTextField u otro componente de texto.
	* @param big
	* @return String
	*/
	public static String bigDecimalToString(BigDecimal big)
	{
	double datoDoubleD = 0;
	//se verifica que sean correctos los argumentos recibidos
	if(big != null)
	datoDoubleD = big.doubleValue();
	/**
	* Los # indican valores no obligatorios
	* Los 0 indican que si no hay valor se pondrá un cero
	*/
	NumberFormat formatter = new DecimalFormat("#,#00.00");
	return formatter.format(datoDoubleD);
	}//metodo
	
	
	
}
