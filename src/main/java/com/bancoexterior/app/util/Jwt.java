package com.bancoexterior.app.util;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class Jwt {

	public static String createJWT(String id, String issuer, String subject, Date exp, String secret, Date issuedat) {

		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		// We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		// Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT").setId(id).setIssuedAt(issuedat)
				.setSubject(subject).setIssuer(issuer).setExpiration(exp).signWith(signatureAlgorithm, signingKey);

		// Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}
	

	public String genrearBearar(String secret, String iss) {
		SignatureAlgorithm algoritmoHS256 = SignatureAlgorithm.HS256;
		byte[] secretBytes = DatatypeConverter.parseBase64Binary(secret);
		Key key = new SecretKeySpec(secretBytes, algoritmoHS256.getJcaName());
		Instant timeIni = Instant.now();
		Instant timeFin = timeIni.plusSeconds(3600*24);
		String s = new String();
		try {
			s = Jwts.builder()
					.setHeaderParam("alg", "HS256")
					.setHeaderParam("typ", "JWT")
					.setIssuer(iss)
					.setExpiration((Date.from(Instant.ofEpochSecond(timeFin.getEpochSecond()))))
					.claim("iss", iss)
					//.signWith(SignatureAlgorithm.HS256, secret.getBytes())
					.signWith(algoritmoHS256 , key)
					.compact();
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} 
	}
	
	
}
