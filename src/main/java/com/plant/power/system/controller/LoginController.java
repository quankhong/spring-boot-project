package com.plant.power.system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.plant.power.system.dto.indto.LoginInDto;
import com.plant.power.system.dto.outdto.BaseOutDto;
import com.plant.power.system.service.interfaces.LoginService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoginController {
	@NonNull
	private LoginService loginService;

	@PostMapping("/api/v1/user/access-token")
	public ResponseEntity<BaseOutDto> createToken(@RequestBody LoginInDto inDto) {
		return new ResponseEntity<>(loginService.createToken(inDto), HttpStatus.CREATED);
	}
}
