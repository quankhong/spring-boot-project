package com.plant.power.system.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenManager {
	public static final long TOKEN_VALIDITY = 10 * 60 * 60;
	@Value("${secret}")
	private String jwtSecret;

	public String generateJwtToken(UserDetails user) {
		Map<String, Object> claims = new HashMap<>();
		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
		return Jwts.builder().setClaims(claims).setSubject(user.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
				.signWith(key).compact();
	}

	public Boolean validateJwtToken(String token, UserDetails user) {
		String usernameToken = getUsernameFromToken(token);
		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		Boolean isTokenExpired = claims.getExpiration().before(new Date());
		return (usernameToken.equals(user.getUsername()) && !isTokenExpired);
	}

	public String getUsernameFromToken(String token) {
		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
		final Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
}
