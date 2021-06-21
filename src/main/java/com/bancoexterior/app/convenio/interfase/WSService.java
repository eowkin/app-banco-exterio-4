package com.bancoexterior.app.convenio.interfase;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.xml.bind.DatatypeConverter;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class WSService implements IWSService{
	
	 private static final Logger LOGGER = LogManager.getLogger(WSService.class);
	 private static final boolean VERIFYSSL = false;
	 private static final HostnameVerifier VERIFIER = NoopHostnameVerifier.INSTANCE;
	 
	 @Override
	 public WSResponse post(WSRequest request) {
		
		 HttpResponse<String> retorno = null;
		 WSResponse response;
		 
		 
		 String secret = "VuL4HVoKOVvhNZhzHUkaOTZKbLB9Kwh7";
		 String iss  =  "GBpXZjE9BCjQlh9S0umaYgFfz0TW98nI";
			
		 //Utils.createJWT(null, env.getProperty("microservicio.jwt.key"), null, new Date(System.currentTimeMillis()+Long.parseLong(env.getProperty("microservicio.jwt.time"))),env.getProperty("microservicio.jwt.secret"),new Date(System.currentTimeMillis())))
		 String bearer2 = this.createJWT(null, iss, null, new Date(System.currentTimeMillis()+ 900000), secret, new Date(System.currentTimeMillis()));
		 try {
			 initUniRest (request.getSocketTimeout(),request.getConnectTimeout() );
			 retorno = Unirest.post(request.getUrl()) 
			  .header("Content-Type", request.getContenType())
			  .header("Accept-Charset", "UTF-8")
			  .header("Authorization","Bearer "+ bearer2)
			  .header("x-api-key","Fr6feu2Pjfzyg6QZKscxzpADxAl2w3k0")
			  .body(request.getBody()).asString();
			  response = new WSResponse(retorno);
			 
			  
		} catch (HttpStatusCodeException e) {
			LOGGER.error(String.format("HttpStatusCodeException: %1$s" ,e));
			LOGGER.error(String.format("ResponseBody: %1$s" ,e.getResponseBodyAsString()));
			response = new WSResponse(e);
		}catch (Exception e) {
			LOGGER.error(String.format("Exception UNI: %1$s" ,e));
			response = new WSResponse(e);
		}	

		 return response;
	 }
	 
	 
	 /**
		 * Nombre:                  initUniRest
		 * Descripcion:             Inicializar Objeto Unirest
		 *
		 * @version 1.0
		 * @author Wilmer Vieira
		 * @since 13/10/20
		 */
     
		private void initUniRest (int socketTimeout, int connectTimeout ) {
			Unirest.config().reset();
			Unirest.config()
		    .socketTimeout(socketTimeout)
		    .connectTimeout(connectTimeout)
			.verifySsl(VERIFYSSL)
			.hostnameVerifier(VERIFIER);
			
		}
		
		
		public static String createJWT(String id, String issuer, String subject, Date exp ,String secret, Date issuedat) {
			
			
	        //The JWT signature algorithm we will be using to sign the token
	        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	   
	   
	        //We will sign our JWT with our ApiKey secret
	        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
	        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	   
	        //Let's set the JWT Claims
	        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
	                                    .setId(id)
	                                    .setIssuedAt(issuedat)
	                                    .setSubject(subject)
	                                    .setIssuer(issuer)
	                                    .setExpiration(exp)
	                                    .signWith(signatureAlgorithm, signingKey);
	           
	   
	        //Builds the JWT and serializes it to a compact, URL-safe string
	        return builder.compact();
	    }


		@Override
		public WSResponse put(WSRequest request) {
			HttpResponse<String> retorno = null;
			 WSResponse response;
			 
			 
			 String secret = "VuL4HVoKOVvhNZhzHUkaOTZKbLB9Kwh7";
			 String iss  =  "GBpXZjE9BCjQlh9S0umaYgFfz0TW98nI";
				
			 //Utils.createJWT(null, env.getProperty("microservicio.jwt.key"), null, new Date(System.currentTimeMillis()+Long.parseLong(env.getProperty("microservicio.jwt.time"))),env.getProperty("microservicio.jwt.secret"),new Date(System.currentTimeMillis())))
			 String bearer2 = this.createJWT(null, iss, null, new Date(System.currentTimeMillis()+ 900000), secret, new Date(System.currentTimeMillis()));
				
			 
			 log.info("bearer2: "+bearer2);
			 
			 
			    

			 
			 try {
				 initUniRest (request.getSocketTimeout(),request.getConnectTimeout() );
				 retorno = Unirest.put(request.getUrl()) 
				  .header("Content-Type", request.getContenType())
				  .header("Accept-Charset", "UTF-8")
				  .header("Authorization","Bearer "+ bearer2)
				  .header("x-api-key","Fr6feu2Pjfzyg6QZKscxzpADxAl2w3k0")
				  .body(request.getBody()).asString();
				  response = new WSResponse(retorno);
				 
				  
			} catch (HttpStatusCodeException e) {
				LOGGER.error(String.format("HttpStatusCodeException: %1$s" ,e));
				LOGGER.error(String.format("ResponseBody: %1$s" ,e.getResponseBodyAsString()));
				response = new WSResponse(e);
			}catch (Exception e) {
				LOGGER.error(String.format("Exception UNI: %1$s" ,e));
				response = new WSResponse(e);
			}	

			 return response;
		}


}
