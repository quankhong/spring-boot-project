package com.plant.power.system.dto.indto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.plant.power.system.util.Messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatteryInfo {
	@NotBlank(message = Messages.ERROR_FIELD_MISSING)
	@Size(max = 255, message = Messages.ERROR_FIELD_INVALID)
	private String name;

	@NotBlank(message = Messages.ERROR_FIELD_MISSING)
	@Size(max = 16, message = Messages.ERROR_FIELD_INVALID)
	private String postcode;

	@NotNull(message = Messages.ERROR_FIELD_MISSING)
	@Min(message = Messages.ERROR_FIELD_INVALID, value = 0)
	private Float capacity;
}