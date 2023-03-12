package com.plant.power.system.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.plant.power.system.controller.BatteryController;
import com.plant.power.system.dto.indto.BatteryInfo;
import com.plant.power.system.service.interfaces.BatteryService;

class PowerSourcesControllerTest {
	private BatteryService batteryService = Mockito.mock(BatteryService.class);
	private BatteryController controller = new BatteryController(batteryService);

	@Test
	void testAddBattery() {
		List<BatteryInfo> batteryInfos = new ArrayList<>();
		controller.addBatteries(batteryInfos);
		verify(batteryService, times(1)).addBatteries(batteryInfos);
	}

	@Test
	void testSearchBattery() {
		controller.searchBattery("", "");
		verify(batteryService, times(1)).search("", "");
	}
}
