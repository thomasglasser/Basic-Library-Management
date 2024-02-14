package dev.thomasglasser.basiclibrarymanagement.util;

import static dev.thomasglasser.basiclibrarymanagement.Main.data;
import static dev.thomasglasser.basiclibrarymanagement.Main.in;

public class Utils
{
	// Sends goodbye message, saves, and closes scanner before exiting
	public static void exit()
	{
		System.out.println("Goodbye!");
		data.save();
		in.close();
		System.exit(0);
	}

	// Prints generic invalid choice statement
	public static void invalidChoice()
	{
		System.out.println("Invalid choice. Please try again.");
	}
}
