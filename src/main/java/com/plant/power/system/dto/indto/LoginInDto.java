package com.plant.power.system.dto.indto;

import javax.validation.constraints.NotBlank;

import com.plant.power.system.util.Messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginInDto {
	@NotBlank(message = Messages.ERROR_FIELD_MISSING)
	private String username;
	@NotBlank(message = Messages.ERROR_FIELD_MISSING)
	private String password;
}
