package com.plant.power.system.service.impl;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.plant.power.system.database.entity.Battery;
import com.plant.power.system.database.repository.BatteryRepository;
import com.plant.power.system.dto.indto.BatteryInDto;
import com.plant.power.system.dto.outdto.BaseOutDto;
import com.plant.power.system.dto.outdto.BatteryListOutDto;
import com.plant.power.system.service.interfaces.BatteryService;
import com.plant.power.system.util.Messages;
import com.plant.power.system.util.Utility;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class BatteryServiceImpl implements BatteryService {
	@NonNull
	private MessageSource messageSource;

	@NonNull
	private BatteryRepository batteryRepository;

	@Override
	public void addBatteries(BatteryInDto inDto) {
		log.debug("Start addBatteries");

		// We want to add some checks to the user input data, in this case I check for
		// duplicate battery by name
		List<String> invalidNameList = inDto.getBatteries().stream()
				.filter(obj -> batteryRepository.findByName(obj.getName()).isPresent()).map(obj -> obj.getName())
				.toList();

		if (invalidNameList != null && !invalidNameList.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, messageSource.getMessage(Messages.ERROR_NAME_EXIST,
					new String[] { invalidNameList.toString() }, null));
		}

		inDto.getBatteries().stream().forEach(inputBattery -> {
			Battery battery = new Battery();
			battery.setName(inputBattery.getName().trim());
			battery.setPostcode(inputBattery.getPostcode().trim());
			battery.setCapacity(inputBattery.getCapacity());

			batteryRepository.save(battery);
		});
		log.debug("End addBatteries");
	}

	@Override
	public BaseOutDto search(String start, String end) {
		log.debug("Start search");
		List<Battery> batteryList = getBatteries(start, end);
		BaseOutDto outDto = createSearchOutDto(batteryList);
		log.debug("End search");
		return outDto;

	}

	private List<Battery> getBatteries(String start, String end) {
		log.debug("Start getBatteries");
		boolean isStartBlank = Utility.isNullOrBlank(start);
		boolean isEndBlank = Utility.isNullOrBlank(end);

		List<Battery> batteryList = null;
		if (isStartBlank && isEndBlank) {
			batteryList = batteryRepository.findAll(Sort.by("name"));
		} else if (isStartBlank) {
			batteryList = batteryRepository.findAllByPostcodeLessThanEqualOrderByNameAsc(end);
		} else if (isEndBlank) {
			batteryList = batteryRepository.findAllByPostcodeGreaterThanEqualOrderByNameAsc(start);
		} else {
			batteryList = batteryRepository
					.findAllByPostcodeGreaterThanEqualAndPostcodeLessThanEqualOrderByNameAsc(start, end);
		}

		if (batteryList == null || batteryList.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					messageSource.getMessage(Messages.ERROR_NO_DATA, null, null));
		}

		log.debug("End getBatteries");
		return batteryList;
	}

	private BaseOutDto createSearchOutDto(List<Battery> batteryList) {
		log.debug("Start createSearchOutDto");
		BatteryListOutDto outDto = new BatteryListOutDto();
		List<String> batteryNameList = null;
		float totalCapacity = 0f;
		float averageCapacity = 0f;

		if (batteryList != null && !batteryList.isEmpty()) {
			totalCapacity = batteryList.stream().map(batteryObj -> batteryObj.getCapacity()).reduce(0f,
					(a, b) -> a + b);
			averageCapacity = totalCapacity / batteryList.size();
			batteryNameList = batteryList.stream().map(entity -> entity.getName()).toList();
		}

		outDto.setBatteries(batteryNameList);
		outDto.setAverageCapacity(averageCapacity);
		outDto.setTotalCapacity(totalCapacity);
		log.debug("End createSearchOutDto");
		return outDto;
	}

}
