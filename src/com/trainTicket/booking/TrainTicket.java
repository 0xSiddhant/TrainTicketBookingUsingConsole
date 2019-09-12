package com.trainTicket.booking;

import java.util.Scanner;
import java.sql.*;

public class TrainTicket {

	static {
		System.out.println("\t\t\t Welcome to Train Ticket Counter");
	}
	
	static String[] stations = {"Patna","Delhi","Mumbai","Agra","Darbhanga"};
	
	static Scanner sc = new Scanner(System.in);
	static Connector db;
	static int totalAvailableSeat;
	static String loc;
	
	public static void main(String[] args) {

		db = Connector.getConnector();
		
		if(!logIn()) {
			HelperClass.exit();
		}
		System.out.println("\t\t\t\t Type \"Quit\" or \"Exit\" to exit from Ticket Counter anytime");
		while(true) {
			doProcessing();
			System.out.println("Do you want to buy another ticket (Y\\N)");
			while(true) {
				String c = sc.next();
				char temp = HelperClass.trimOption(c);
				if( temp == 'n') {
						HelperClass.exit();
				}else if(temp == 'y') {
					break;
				}else {
					System.out.println("Please enter a valid answer");
				}
			}
		}
	}
	
	
	
	public static void doProcessing() {
		String source = showGetStations(true);
		// true means source
		
		String destination = showGetStations(false);
		// false means destination
		
		loc = HelperClass.generateLocCode(source, destination);
		int bookingSeat = getSeat(loc);
		
		while(true) {
			if (bookingSeat == -2) {
				System.out.println("Do you want to book another ticket (Y\\N)");
				String c = sc.next();
				checkContinue(c);
			}else if(bookingSeat == -1) {
				HelperClass.exit();
			}else {
				break;
			}
		}
		updateSeat(bookingSeat, loc );
		
	}
	
	public static void checkContinue(String c) {
		char temp = HelperClass.trimOption(c);
		if( temp == 'n') {
				HelperClass.exit();
		}else if(temp == 'y') {
			doProcessing();
		}else {
			System.out.println("Please enter a valid answer");
			String scc = sc.next();
			checkContinue(scc);
		}
	}
	
	public static void updateSeat(int seat, String query) {
		int remainingSeat = totalAvailableSeat - seat;
		PreparedStatement pt;
		try {
			pt = db.con.prepareStatement("UPDATE trainsList SET seat=? WHERE loc=?");
			pt.setInt(1, remainingSeat);
			pt.setString(2, query);
			int i = pt.executeUpdate();
			System.out.println("\t\t" + seat + " ticket is booked");
			HelperClass.exit();
			
		}catch(Exception e) {
			System.out.println("\t\tError in buying ticket");
			HelperClass.exit();
		}
	}

	public static int getSeat(String query) {
		PreparedStatement pt;
		try {
			/*
			 * 
			 * 
			 * CREATE TABLE trainsList (loc VARCHAR(50), seat VARCHAR(40));
			 * 
			 * loc = first 3 word of source + to + first 3 word of des. and every letter of loc will be in LOWERCASE.
			 * 			eg....
			 * Patna -> Delhi
			 * 
			 * then,	loc = pat to del
			 * 
			 * 
			 * 
			 * 
			 */
			
			
			
			pt = db.con.prepareStatement("SELECT seat FROM trainsList WHERE loc = ?");
			pt.setString(1, query);
			ResultSet rs = pt.executeQuery();
			if(rs.getInt(1) == 0) {
				System.out.println("\t\t No seat available");
				return -2;
			}
			System.out.println("\t Available Seats are "+rs.getInt(1));
			totalAvailableSeat = rs.getInt(1);
			while(true) {
				System.out.println("Please enter required seats");
				String s = sc.nextLine();
				HelperClass.wannaExit(s);
				try {
					int no = Integer.parseInt(s);
					if(no > totalAvailableSeat) {
						System.out.println("\tPlease enter number in range");
					}else if(no < 1) {
						System.out.println("\tPlease enter a greater number.");
					}else {
						return no;
					}
				}catch(NumberFormatException e) {
					System.out.println("Enter a valid number");
				}
			}
		}
		catch(Exception e) {
			System.out.println("\t\t\t\tError in featching seats\n"+e);
			HelperClass.exit();
			return -1;
		}
	}
	
	public static boolean logIn() {
		int counter = 3;
		while(true) {
			System.out.println("Enter your user id");
			String userID = sc.next();
			System.out.println("Enter your password");
			String passwd = sc.next();
			
			try {
				/*
				 * 
				 * 
				 * 	CREATE TABLE userDetails (userID VARCHAR(50), passwd VARCHAR(40), name VARCHAR(20));
				 * 
				 * 
				 */
				
				
				ResultSet rs = db.stmt.executeQuery("select * from userDetails");
				while (rs.next()) {
					if( userID == rs.getString(1) && passwd == rs.getString(2)) {
						System.out.println("Welcome "+rs.getString(3));
						return true;
					}
				}
				System.out.println("\tWrong UserName or Password\n\t"+ counter +" more attempt left\n");
				counter --;
			}catch(Exception e) {
				System.out.println(e);
			}
			if (counter <= 0) {
				System.out.println("No more attempt left");
				return false;
			}
		}
	}
	
	
	
	public static String showGetStations(boolean type) {
		String str = "";
		String input = "";
		int ans ;
		if(type) {
			str = "\tSelect Source Station\n";
		}else {
			str = "\tSelect Destination Station\n";
		}
		for(int i=1;i<=stations.length;i++) {
			str+= "" + i + " " + stations[i-1] + "\n";
		}
		while(true) {
			System.out.println(str);
			try {
				input = sc.nextLine();
				HelperClass.wannaExit(input);
				ans = Integer.parseInt(input);
				if (ans > stations.length || ans < 1) {
					System.out.println("Invalid Station\n");
				}else {
					return stations[ans-1];
				}
			}catch(NumberFormatException e) {
				System.out.println("Please Enter a valid number");
			}
		}
	}

	
}
