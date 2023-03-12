package com.plant.power.system.service.interfaces;

import java.util.List;

import com.plant.power.system.dto.indto.BatteryInfo;
import com.plant.power.system.dto.outdto.BaseOutDto;

public interface BatteryService {
	public void addBatteries(List<BatteryInfo> batteries);
	public BaseOutDto search(String start, String end);
}
