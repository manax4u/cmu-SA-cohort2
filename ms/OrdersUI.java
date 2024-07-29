/******************************************************************************************************************
* File:OrdersUI.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2018 Carnegie Mellon University
* Versions:
*	1.0 February 2018 - Initial write of assignment 3 (ajl).
*
* Description: This class is the console for the an orders database. This interface uses a webservices or microservice
* client class to update the ms_orderinfo MySQL database. 
*
* Parameters: None
*
* Internal Methods: None
*
* External Dependencies (one of the following):
*	- MSlientAPI - this class provides an interface to a set of microservices
*	- RetrieveServices - this is the server-side micro service for retrieving info from the ms_orders database
*	- CreateServices - this is the server-side micro service for creating new orders in the ms_orders database
*
******************************************************************************************************************/
import java.lang.Exception;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.Console;
import java.util.logging.Level;

public class OrdersUI
{
	private static boolean authenticated = false;
	private static String currentUser = null;
	public static void main(String args[]) throws Exception {

		boolean done = false;                        // main loop flag
		boolean error = false;                        // error flag
		char option;                                // Menu choice from user
		Console c = System.console();                // Press any key
		String date = null;                        // order date
		String first = null;                        // customer first name
		String last = null;                        // customer last name
		String address = null;                        // customer address
		String phone = null;                        // customer phone number
		String orderid = null;                        // order ID
		String response = null;                    // response string from REST
		Scanner keyboard = new Scanner(System.in);    // keyboard scanner object for user input
		DateTimeFormatter dtf = null;                // Date object formatter
		LocalDate localDate = null;                    // Date object
		MSClientAPI api = new MSClientAPI();
		LoggerClient logger = new LoggerClient();


		/////////////////////////////////////////////////////////////////////////////////
		// Main UI loop
		/////////////////////////////////////////////////////////////////////////////////

		while (!done) {
			// Here, is the main menu set of choices

			if (!authenticated) {
				// Prompt for login or registration
				System.out.println("Please log in or register:");
				System.out.println("1: Log In");
				System.out.println("2: Register");
				System.out.print(">>>> ");
				option = keyboard.next().charAt(0);
				keyboard.nextLine(); // Clear buffer

				switch (option) {
					case '1':
						login(keyboard, api,logger);
						break;
					case '2':
						register(keyboard, api,logger);
						break;
					default:
						System.out.println("Invalid option. Try again.");
				}

				if (!authenticated) {
					System.out.println("Login/Registration failed. Try again.");
					continue; // Prompt login/registration again
				}
			}

			System.out.println("\n\n");
			System.out.println("Orders Database User Interface: \n");
			System.out.println("Select an Option: \n");
			System.out.println("1: Retrieve all orders in the order database.");
			System.out.println("2: Retrieve an order by ID.");
			System.out.println("3: Add a new order to the order database.");
			System.out.println("4: Delete an order in the order database.");
			System.out.println("X: Exit\n");
			System.out.print("\n>>>> ");
			option = keyboard.next().charAt(0);
			keyboard.nextLine();    // Removes data from keyboard buffer. If you don't clear the buffer, you blow
			// through the next call to nextLine()

			//////////// option 1 ////////////

			if (option == '1') {
				// Here we retrieve all the orders in the ms_orderinfo database

				System.out.println("\nRetrieving All Orders::");
				try {
					response = api.retrieveOrders(currentUser);
					System.out.println(response);

				} catch (Exception e) {

					System.out.println("Request failed:: " + e);

				}

				System.out.println("\nPress enter to continue...");
				c.readLine();

			} // if

			//////////// option 2 ////////////

			if (option == '2') {
				// Here we get the order ID from the user

				error = true;

				while (error) {
					System.out.print("\nEnter the order ID: ");
					orderid = keyboard.nextLine();

					try {
						Integer.parseInt(orderid);
						error = false;
					} catch (NumberFormatException e) {

						System.out.println("Not a number, please try again...");
						System.out.println("\nPress enter to continue...");

					} // if

				} // while

				try {
					response = api.retrieveOrders(orderid,currentUser);
					System.out.println(response);

				} catch (Exception e) {

					System.out.println("Request failed:: " + e);

				}

				System.out.println("\nPress enter to continue...");
				c.readLine();

			} // if

			//////////// option 3 ////////////

			if (option == '3') {
				// Here we create a new order entry in the database

				dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				localDate = LocalDate.now();
				date = localDate.format(dtf);

				System.out.println("Enter first name:");
				first = keyboard.nextLine();

				System.out.println("Enter last name:");
				last = keyboard.nextLine();

				System.out.println("Enter address:");
				address = keyboard.nextLine();

				System.out.println("Enter phone:");
				phone = keyboard.nextLine();

				System.out.println("Creating the following order:");
				System.out.println("==============================");
				System.out.println(" Date:" + date);
				System.out.println(" First name:" + first);
				System.out.println(" Last name:" + last);
				System.out.println(" Address:" + address);
				System.out.println(" Phone:" + phone);
				System.out.println("==============================");
				System.out.println("\nPress 'y' to create this order:");

				option = keyboard.next().charAt(0);

				if ((option == 'y') || (option == 'Y')) {
					try {
						System.out.println("\nCreating order...");
						response = api.newOrder(date, first, last, address, phone,currentUser);
						System.out.println(response);

					} catch (Exception e) {

						System.out.println("Request failed:: " + e);

					}

				} else {

					System.out.println("\nOrder not created...");
				}

				System.out.println("\nPress enter to continue...");
				c.readLine();

				option = ' '; //Clearing option. This incase the user enterd X/x the program will not exit.

			} // if

			if (option == '4') {
				// Here we get the order ID from the user

				error = true;

				while (error) {
					System.out.print("\nEnter the order ID: ");
					orderid = keyboard.nextLine();

					try {
						Integer.parseInt(orderid);
						error = false;
					} catch (NumberFormatException e) {

						System.out.println("Not a number, please try again...");
						System.out.println("\nPress enter to continue...");

					} // if

				} // while

				try {
					response = api.deleteOrders(orderid,currentUser);
					System.out.println(response);

				} catch (Exception e) {

					System.out.println("Request failed:: " + e);

				}

				System.out.println("\nPress enter to continue...");
				c.readLine();

			}

			//////////// option X ////////////

			if ((option == 'X') || (option == 'x')) {
				// Here the user is done, so we set the Done flag and halt the system

				done = true;
				System.out.println("\nDone...\n\n");

			} // if

		} // while
	}

	  // main
	  private static void login(Scanner keyboard, MSClientAPI api, LoggerClient logger) {
		  System.out.print("Enter username: ");
		  String username = keyboard.nextLine();
		  System.out.print("Enter password: ");
		  String password = new String(System.console().readPassword());

		  try {
			  authenticated = api.authenticate(username, password);
			  if (authenticated) {
				  currentUser = username;
				  logger.log(Level.INFO.getName(), "Login Successful for "+currentUser);


			  } else {
				  logger.log(Level.INFO.getName(),"Invalid username or password.");
			  }
		  } catch (Exception e) {
			  logger.log(Level.SEVERE.getName()," Login failed: " + e.getMessage());
		  }
	  }
	private static void register(Scanner keyboard, MSClientAPI api, LoggerClient logger) {
		System.out.print("Enter username: ");
		String username = keyboard.nextLine();
		System.out.print("Enter password: ");
		String password = new String(System.console().readPassword());
		System.out.print("Enter role: ");
		String role = keyboard.nextLine();

		try {
			boolean success = api.register(username, password, role);
			if (success) {
				currentUser = username;
				authenticated = true;
				logger.log(Level.INFO.getName(),"Registration successful. You are now logged in as "+currentUser);
			} else {
				logger.log(Level.INFO.getName(),"Registration failed. Username might already exist.");
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE.getName(),"Registration failed: " + e.getMessage());
		}
	}

} // OrdersUI
