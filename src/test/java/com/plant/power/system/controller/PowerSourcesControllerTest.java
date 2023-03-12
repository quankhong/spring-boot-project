package com.plant.power.system.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.plant.power.system.controller.BatteryController;
import com.plant.power.system.dto.indto.BatteryInDto;
import com.plant.power.system.service.interfaces.BatteryService;

class PowerSourcesControllerTest {
	private BatteryService batteryService = Mockito.mock(BatteryService.class);
	private BatteryController controller = new BatteryController(batteryService);

	@Test
	void testAddBattery() {
		BatteryInDto inDto = new BatteryInDto();
		controller.addBatteries(inDto);
		verify(batteryService, times(1)).addBatteries(inDto);
	}

	@Test
	void testSearchBattery() {
		controller.searchBattery("", "");
		verify(batteryService, times(1)).search("", "");
	}
}
