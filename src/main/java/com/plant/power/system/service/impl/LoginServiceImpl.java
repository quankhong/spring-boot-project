package com.plant.power.system.service.impl;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.plant.power.system.dto.indto.LoginInDto;
import com.plant.power.system.dto.outdto.BaseOutDto;
import com.plant.power.system.dto.outdto.LoginOutDto;
import com.plant.power.system.security.JwtUserDetailsService;
import com.plant.power.system.security.TokenManager;
import com.plant.power.system.service.interfaces.LoginService;
import com.plant.power.system.util.Messages;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
	@NonNull
	private MessageSource messageSource;

	@NonNull
	private JwtUserDetailsService userDetailsService;

	@NonNull
	private AuthenticationManager authenticationManager;

	@NonNull
	private TokenManager tokenManager;

	@Override
	public BaseOutDto createToken(LoginInDto inDto) {
		LoginOutDto outDto = new LoginOutDto();

		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(inDto.getUsername(), inDto.getPassword()));
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					messageSource.getMessage(Messages.ERROR_LOGIN, null, null));
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(inDto.getUsername());
		final String jwtToken = tokenManager.generateJwtToken(userDetails);

		outDto.setToken(jwtToken);

		return outDto;
	}

}
