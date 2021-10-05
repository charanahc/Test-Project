package com.ibm.poc.util;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class Utility {

	public static ResponseDetails formResponseObject(boolean status, String message, String error, Object data,
			HttpStatus statusCode) {
		return new ResponseDetails(status, message, error, data, statusCode);
	}
}
