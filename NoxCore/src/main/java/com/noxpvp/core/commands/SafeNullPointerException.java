package com.noxpvp.core.commands;

public class SafeNullPointerException extends NullPointerException {

	public SafeNullPointerException() {
		super();
	}

	public SafeNullPointerException(String s) {
		super(s);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 915720991502429516L;
	
}