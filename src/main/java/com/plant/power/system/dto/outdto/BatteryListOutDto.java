package com.plant.power.system.dto.outdto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatteryListOutDto extends BaseOutDto {
	@JsonProperty(value="batteries")
	private List<String> batteries;
	@JsonProperty(value="total_capacity")
	private float totalCapacity;
	@JsonProperty(value="average_capacity")
	private float averageCapacity;
}
