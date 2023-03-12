package com.plant.power.system.service.interfaces;

import com.plant.power.system.dto.indto.LoginInDto;
import com.plant.power.system.dto.outdto.BaseOutDto;

public interface LoginService {
	public BaseOutDto createToken(LoginInDto inDto);
}
