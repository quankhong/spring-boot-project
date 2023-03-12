package com.plant.power.system.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plant.power.system.dto.indto.BatteryInDto;
import com.plant.power.system.dto.outdto.BaseOutDto;
import com.plant.power.system.service.interfaces.BatteryService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/batteries")
@Log4j2
public class BatteryController {
	@NonNull
	private BatteryService batteryService;

	@PostMapping
	public ResponseEntity<Object> addBatteries(@Valid @RequestBody BatteryInDto inDto) {
		log.debug("Add new batteries");
		batteryService.addBatteries(inDto);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<BaseOutDto> searchBattery(
			@RequestParam(required = false, name = "postcode_start") String postcodeStart,
			@RequestParam(required = false, name = "postcode_end") String postcodeEnd) {
		log.debug("Search batteries");
		return new ResponseEntity<>(batteryService.search(postcodeStart, postcodeEnd), HttpStatus.OK);
	}
}
