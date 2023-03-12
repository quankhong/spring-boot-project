package com.plant.power.system.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtFilter extends OncePerRequestFilter {
	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

	private static final String AUTHORIZATION = "Authorization";
	private static final String AUTHORIZATION_BEARER = "Bearer ";
	@Autowired
	private JwtUserDetailsService userDetailsService;
	@Autowired
	private TokenManager tokenManager;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String tokenHeader = request.getHeader(AUTHORIZATION);
		String username = null;
		String token = null;
		if (tokenHeader != null && tokenHeader.startsWith(AUTHORIZATION_BEARER)) {
			token = tokenHeader.substring(7);
			try {
				username = tokenManager.getUsernameFromToken(token);
			} catch (IllegalArgumentException e) {
				logger.info("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				logger.info("JWT Token has expired");
			}
		} else {
			logger.info("Bearer String not found in token");
		}
		if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if (tokenManager.validateJwtToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}