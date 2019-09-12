package com.trainTicket.booking;

public class HelperClass {


	static final String QUIT = "quit";
	static final String EXIT = "exit";
	
	public static String generateLocCode(String source, String destination) {
		source = source.substring(0, 3);
		destination = destination.substring(0, 3);
		return source.toLowerCase() + " to " + destination.toLowerCase();
	}

	public static void exit() {
		System.out.println("Thanku for visiting us");
		System.exit(1);
	}
	
	public static void wannaExit(String query) {
		if(query.toLowerCase().equals(QUIT) || query.toLowerCase().equals(EXIT)) {
			exit();
		}
	}
	public static char trimOption(String c) {
		wannaExit(c);
		c = c.substring(0,1);
		return c.charAt(0);
	}
}

