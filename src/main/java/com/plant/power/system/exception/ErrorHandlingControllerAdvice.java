package com.plant.power.system.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.plant.power.system.dto.outdto.BaseOutDto;
import com.plant.power.system.util.Messages;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class ErrorHandlingControllerAdvice {
	@NonNull
	private MessageSource messageSource;

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<BaseOutDto> onHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		log.error(ex.getMessage(), ex);
		BaseOutDto retDto = new BaseOutDto();
		retDto.setMessages(Arrays.asList(messageSource.getMessage(Messages.ERROR_BODY_INVALID, null, null)));

		return new ResponseEntity<>(retDto, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<BaseOutDto> onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		log.error(ex.getMessage(), ex);
		BaseOutDto retDto = new BaseOutDto();
		List<String> errorList = new ArrayList<>();
		retDto.setMessages(errorList);

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errorList.add(messageSource.getMessage(errorMessage, new String[] { fieldName }, null));
		});

		return new ResponseEntity<>(retDto, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> onException(Exception ex) {
		log.error(ex.getMessage(), ex);
		BaseOutDto retDto = new BaseOutDto();
		retDto.setMessages(Arrays.asList(messageSource.getMessage(Messages.ERROR_UNEXPECTED, null, null)));
		return new ResponseEntity<>(retDto, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
