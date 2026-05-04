package com.example.exception;

public class BookingAlreadyFoundException extends RuntimeException{
	public BookingAlreadyFoundException(String message) {
		super(message);
	}

}
