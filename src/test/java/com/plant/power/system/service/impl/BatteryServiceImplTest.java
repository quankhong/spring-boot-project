package com.plant.power.system.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.plant.power.system.database.entity.Battery;
import com.plant.power.system.database.repository.BatteryRepository;
import com.plant.power.system.dto.indto.BatteryInfo;
import com.plant.power.system.dto.outdto.BatteryListOutDto;

class BatteryServiceImplTest {

	private MessageSource messageSource = Mockito.mock(MessageSource.class);

	private BatteryRepository batteryRepository = Mockito.mock(BatteryRepository.class);

	public BatteryServiceImpl batteryServiceImpl = new BatteryServiceImpl(messageSource, batteryRepository);

	@Test
	void testAddBattery() {
		BatteryInfo batteryInfo = new BatteryInfo();
		batteryInfo.setCapacity(1.0f);
		batteryInfo.setName("testBattery");
		batteryInfo.setPostcode("60000");

		List<BatteryInfo> batteryInfos = new ArrayList<>();
		batteryInfos.add(batteryInfo);

		given(batteryRepository.findByName(anyString())).willReturn(Optional.empty());

		batteryServiceImpl.addBatteries(batteryInfos);
		verify(batteryRepository, times(1)).save(any());
	}

	@Test
	void testAddBatteryExisted() {
		BatteryInfo batteryInfo = new BatteryInfo();
		batteryInfo.setCapacity(1.0f);
		batteryInfo.setName("testBattery");
		batteryInfo.setPostcode("60000");

		List<BatteryInfo> batteryInfos = new ArrayList<>();
		batteryInfos.add(batteryInfo);

		Battery battery = new Battery();
		battery.setName("testBattery");

		given(batteryRepository.findByName(anyString())).willReturn(Optional.of(battery));

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			batteryServiceImpl.addBatteries(batteryInfos);
	    });
		
		assertEquals(exception.getStatus(), HttpStatus.CONFLICT);
		verify(batteryRepository, times(0)).save(any());
	}

	@Test
	void testAddBatteryException() {
		BatteryInfo batteryInfo = new BatteryInfo();
		batteryInfo.setCapacity(1.0f);
		batteryInfo.setName("testBattery");
		batteryInfo.setPostcode("60000");

		List<BatteryInfo> batteryInfos = new ArrayList<>();
		batteryInfos.add(batteryInfo);

		Battery battery = new Battery();
		battery.setName("testBattery");

		given(batteryRepository.findByName(anyString())).willThrow(RuntimeException.class);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			batteryServiceImpl.addBatteries(batteryInfos);
	    });
		
		verify(batteryRepository, times(0)).save(any());
		assertNotNull(exception);
	}

	@Test
	void testSearchAll() {
		assertThrows(ResponseStatusException.class, () -> {
			batteryServiceImpl.search("", "");
	    });

		verify(batteryRepository, times(1)).findAll(Sort.by("name"));
		verify(batteryRepository, times(0)).findAllByPostcodeLessThanEqualOrderByNameAsc(any());
		verify(batteryRepository, times(0)).findAllByPostcodeGreaterThanEqualOrderByNameAsc(any());
		verify(batteryRepository, times(0))
				.findAllByPostcodeGreaterThanEqualAndPostcodeLessThanEqualOrderByNameAsc(any(), any());
	}

	@Test
	void testSearchByStart() {
		assertThrows(ResponseStatusException.class, () -> {
			batteryServiceImpl.search("50000", "");
	    });

		verify(batteryRepository, times(0)).findAll(Sort.by("name"));
		verify(batteryRepository, times(0)).findAllByPostcodeLessThanEqualOrderByNameAsc(any());
		verify(batteryRepository, times(1)).findAllByPostcodeGreaterThanEqualOrderByNameAsc(any());
		verify(batteryRepository, times(0))
				.findAllByPostcodeGreaterThanEqualAndPostcodeLessThanEqualOrderByNameAsc(any(), any());
	}

	@Test
	void testSearchByEnd() {
		assertThrows(ResponseStatusException.class, () -> {
			batteryServiceImpl.search("", "50000");
	    });

		verify(batteryRepository, times(0)).findAll(Sort.by("name"));
		verify(batteryRepository, times(1)).findAllByPostcodeLessThanEqualOrderByNameAsc(any());
		verify(batteryRepository, times(0)).findAllByPostcodeGreaterThanEqualOrderByNameAsc(any());
		verify(batteryRepository, times(0))
				.findAllByPostcodeGreaterThanEqualAndPostcodeLessThanEqualOrderByNameAsc(any(), any());
	}

	@Test
	void testSearchByRange() {
		assertThrows(ResponseStatusException.class, () -> {
			batteryServiceImpl.search("50000", "60000");
	    });

		verify(batteryRepository, times(0)).findAll(Sort.by("name"));
		verify(batteryRepository, times(0)).findAllByPostcodeLessThanEqualOrderByNameAsc(any());
		verify(batteryRepository, times(0)).findAllByPostcodeGreaterThanEqualOrderByNameAsc(any());
		verify(batteryRepository, times(1))
				.findAllByPostcodeGreaterThanEqualAndPostcodeLessThanEqualOrderByNameAsc(any(), any());
	}

	@Test
	void testSearchNoData() {
		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			batteryServiceImpl.search("", "");
	    });
		
		assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
	}

	@Test
	void testSearchHaveData() {
		given(batteryRepository.findByName(anyString())).willThrow(RuntimeException.class);

		Battery battery = new Battery();
		battery.setCapacity(1.0f);
		battery.setName("testBattery");
		battery.setPostcode("60000");

		List<Battery> batteries = new ArrayList<>();
		batteries.add(battery);
		given(batteryRepository.findAll(Sort.by("name"))).willReturn(batteries);
		BatteryListOutDto outDto = (BatteryListOutDto) batteryServiceImpl.search("", "");
		assertEquals(outDto.getBatteries().size(), 1);
	}

}
