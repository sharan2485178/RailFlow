package com.example.exception;

public class AssetAlreadyAssignedException extends RuntimeException{
	public AssetAlreadyAssignedException(String message) {
		super(message);
	}

}
