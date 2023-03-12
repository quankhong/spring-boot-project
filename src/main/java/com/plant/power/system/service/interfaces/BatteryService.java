package com.plant.power.system.service.interfaces;

import com.plant.power.system.dto.indto.BatteryInDto;
import com.plant.power.system.dto.outdto.BaseOutDto;

public interface BatteryService {
	public void addBatteries(BatteryInDto inDto);
	public BaseOutDto search(String start, String end);
}
