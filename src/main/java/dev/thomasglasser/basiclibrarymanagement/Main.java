package dev.thomasglasser.basiclibrarymanagement;

import dev.thomasglasser.basiclibrarymanagement.data.LibraryData;
import dev.thomasglasser.basiclibrarymanagement.util.Utils;
import dev.thomasglasser.basiclibrarymanagement.util.Validator;

import java.util.Scanner;

public class Main
{
	public static final Scanner in = new Scanner(System.in);
	public static LibraryData data = LibraryData.load();

	public static void main(String[] args)
	{
		System.out.println("Welcome to Basic Library Management");
		System.out.println("""
				Where would you like to go?
				1. Book Menu
				2. Patron Menu
				3. Check Out Menu
				4. Exit""");

		int choice = Validator.validateInt(in.next()).orElse(-1);
		in.nextLine();
		switch (choice)
		{
			case 1:
				BookMenus.menu();
				break;
			case 2:
				PatronMenus.menu();
				break;
			case 3:
				CheckOutMenus.menu();
				break;
			case 4:
				Utils.exit();
				break;
			default:
				Utils.invalidChoice();
				main(args);
		}

		in.close();
	}
}
