package com.ibm.poc.util;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ResponseDetails {

	private boolean status;
	private String message;
	private String error;
	private Object data;
	private HttpStatus statusCode;

}
