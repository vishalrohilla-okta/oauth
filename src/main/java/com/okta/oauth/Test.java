package com.okta.oauth;

import java.io.StringReader;
import java.security.PublicKey;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.okta.oauth.bean.User;

import io.jsonwebtoken.Jwts;

public class Test {
	
	  public static void main(String[] args) {
	        System.out.println("Hello World!");
//	        String jwt = "sub":"00u9kit1lwqolCQB00h7","zoneinfo":"America/Los_Angeles","address":{"street_address":"street","country":"US","locality":"Francisco","region":"CA","postal_code":"94105"},"updated_at":1486961831,"name":"Brad Pitt","nickname":"bpitt","preferred_username":"brad.pitt@mailinator.com","locale":"US","given_name":"Brad","family_name":"Pitt";
//	       User user = getUserFromJWT();
	       // boolean flag = validateJWT();
	    }
	  
	  private static boolean parseJWT(){
		  boolean flag = false;
		  String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImRDZGxoSlpDSkd1b3FrVUpsZ3Y4UVEwbEhwMmhCOGQ5bWZrVWlnWHN3dDQifQ.eyJ2ZXIiOjEsImp0aSI6IkFULmF2RmxzZkxqTG94dmM3UFZXU1NMNDctTnZFS0JQaFJzZG9pQ3R4bWlyR1EiLCJpc3MiOiJodHRwczovL3NvY2lhbGlkbS5va3RhcHJldmlldy5jb20vb2F1dGgyL2F1czkxNWhnNnlnSVVJMDF2MGg3IiwiYXVkIjoiaHR0cHM6Ly9nb29nbGUuY29tIiwiaWF0IjoxNDgzNTE3MDkzLCJleHAiOjE0ODM1MjA2OTMsImNpZCI6IjJaWm93Z0t5NnJ6d1h5WGZKTVpwIiwidWlkIjoiMDB1NWM2dW9nOWU0NHNneEIwaDciLCJzY3AiOlsib3BlbmlkIiwicHJvZmlsZSIsInJlYWQiXSwic3ViIjoib2t0YWFkbWluQG9rdGEuY29tIiwidGVzdCI6WyJzZXJ2aWNlIGNsYWltcyJdfQ.hABalfl_NU8YrhqrHLmRKelgjNIkkOPm5M2FKjEprKYR7q_JQmZGlosdDUUe_Kc12Np4eoABXR0YzP3XL7hhJjFUvLkfoPvUrYzq47CyJvj6rApmFUYnHVGwFtDanUJIY3w-H4PQQdi_25npsekWADsPu2nJGUmIWwtccGmjVNEN4GGIaofOkRFDVuea12p5mHNu4i4zGPbGgSsnOiBMkUa0sMcNCiZCuyqFBaeCPsf5wLReguNLjLunBvWiHgNe9dubDK4ixxoQEbrvVprenL8gMWBVcrn_iKIYXTsCo2h2rJrJqJyKAEJA2k7ZmoeH3Lz1-1G7AvQ7_GjnL32PGQ";
		  JWT decodedJWT = JWT.decode(token);
		  String alg = decodedJWT.getAlgorithm();
		  System.out.println("alg = "+alg);
		  String keyId = decodedJWT.getKeyId();
		  System.out.println("keyId = "+keyId);
		  Claim scp = decodedJWT.getClaim("scp");
		  
		  System.out.println("Claim SCP = "+ scp.toString());
		  Claim test = decodedJWT.getClaim("test");
		  System.out.println("Claim test = "+ test.toString());
		  
//		  PublicKey key = dCdlhJZCJGuoqkUJlgv8QQ0lHp2hB8d9mfkUigXswt4;
/*		  try {
		      JWTVerifier verifier = JWT.require(Algorithm.RSA256(key))
		          .withIssuer("auth0")
		          .build(); //Reusable verifier instance
		      JWT jwt = verifier.verify(token);
		  } catch (JWTVerificationException exception){
		      //Invalid signature/claims
		  }*/
		  return flag;
	  }
	  
	  private static boolean validateJWT() {
		  boolean flag = false;
		  String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImRDZGxoSlpDSkd1b3FrVUpsZ3Y4UVEwbEhwMmhCOGQ5bWZrVWlnWHN3dDQifQ.eyJzdWIiOiIwMHU1YzZ1b2c5ZTQ0c2d4QjBoNyIsIm5hbWUiOiJva3RhIGFkbWluIiwidmVyIjoxLCJpc3MiOiJodHRwczovL3NvY2lhbGlkbS5va3RhcHJldmlldy5jb20vb2F1dGgyL2F1czkxNWhnNnlnSVVJMDF2MGg3IiwiYXVkIjoiMlpab3dnS3k2cnp3WHlYZkpNWnAiLCJpYXQiOjE0ODM1MDgwMzQsImV4cCI6MTQ4MzUxMTYzNCwianRpIjoiSUQudjBOY254c2xVTlgwZlhjbTl5UVNocVBfVlAzSFhqd3B0SEdFOFppSEJUMCIsImFtciI6WyJwd2QiXSwiaWRwIjoiMG9hOHU5ZnoxOHFqeGtGQmIwaDciLCJub25jZSI6InRlc3QiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJva3RhYWRtaW5Ab2t0YS5jb20iLCJhdXRoX3RpbWUiOjE0ODM1MDgwMzEsImF0X2hhc2giOiJQUDhKSzZ5OXB4Rmk3ZG00a2U0dFRBIn0.FH8iIfcadI446MHPxxvct5LFCTU06wsNOSmsoyKjyrb5QLkxPFwLV-B_JZ46eyqD6wzvUFyq7FPRkT6k2IKsDhqHHDTgIi_bgIxm5laXcpghrSwh1QmbZAX4eMHYxfN9ALBzlzerpoTn7H_oVJ0TCTRdlHYWVmF8CqaUHisU97zu6YQ6gr_tZYXxx9OP_2nHt01Jn2J9l3u0PEX_ZS4N-_MVj_uBhx4d-XG0wu4zLBLLVQEUCgw2z5x4HkAmmDHKWvtpukTP78828xHd1LpKiRsjF6S0BRUUYW_4WctQ2hNZtpVI7mB3FdjIHVxeBSPwAqy8YwL3DgJw8NvDNgO0IQ";
		  String kid = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDdlatRjRjogo3WojgGHFHYLugdUWAY9iR3fy4arWNA1KoS8kVw33cJibXr8bvwUAUparCwlvdbH6dvEOfou0/gCFQsHUfQrSDv+MuSUMAe8jzKE4qW+jK+xQU9a03GUnKHkkle+Q0pX/g6jXZ7r1/xAK5Do2kQ+X5xK9cipRgEKwIDAQAB";
		  Jwts.parser().setSigningKey(kid).parseClaimsJws(token);
		  return flag;
	  }
	  
	  private static User getUserFromJWT(String jwt){
		  
		  User user = new User();
		  Gson gson = new Gson();
		  JsonReader reader = new JsonReader(new StringReader(jwt));
		  reader.setLenient(true);
		  User userUpdated = gson.fromJson(reader, User.class);
		  return userUpdated;
		  
	  }

}
