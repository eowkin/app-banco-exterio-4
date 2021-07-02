package com.bancoexterior.app.seguridad;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class MiCipher {
	
	@Autowired
	private static final Logger LOGGER = LogManager.getLogger(MiCipher.class);	
	

	private MiCipher() {
		super();
	}
	
	/**
     * Nombre:                  encrypt
     * Descripcion:             Encriptar Valores
     *
     * @param  value  valor a encriptar
     * @param  key semilla para encriptar
     * @return String valor encriptado
     * @version 1.0
     * @author Wilmer Vieira
	 * @since 08/06/20
     */
	public static String encrypt(String value, String key) {
		try {
			

			SecretKeySpec skeySpec = new SecretKeySpec(sha256(key.getBytes(StandardCharsets.UTF_8)), SConfig.CSET);

			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec,new GCMParameterSpec(SConfig.TAG_LENGTH_BIT, SConfig.IV));

			byte[] encrypted = cipher.doFinal(value.getBytes());
			return Base64.getEncoder().encodeToString(encrypted);

		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}
		return "";
	}

	private  static byte[] sha256(byte[] data) {
	
		byte[] reto=null;
		try {
			return MessageDigest.getInstance(SConfig.SHA256).digest(data);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage());
		}
		return reto;
	}
	
	/**
     * Nombre:                  decrypt
     * Descripcion:             Decriptar Valores
     *
     * @param  value  valor a decriptar
     * @param  key semilla para decriptar
     * @return String valor decriptado
     * @version 1.0
     * @author Wilmer Vieira
	 * @since 08/06/20
     */
	public static String decrypt(String value, String key) {
		try {
	
		
		    
			SecretKeySpec skeySpec = new SecretKeySpec(sha256(key.getBytes(StandardCharsets.UTF_8)), SConfig.CSET);
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, new GCMParameterSpec(SConfig.TAG_LENGTH_BIT, SConfig.IV));
			byte[] original = cipher.doFinal(Base64.getDecoder().decode(value));

			return new String(original);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}
		return "";
	}

}
