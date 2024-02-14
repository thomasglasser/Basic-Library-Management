package dev.thomasglasser.basiclibrarymanagement;

import dev.thomasglasser.basiclibrarymanagement.data.Book;
import dev.thomasglasser.basiclibrarymanagement.data.Patron;
import dev.thomasglasser.basiclibrarymanagement.util.Utils;
import dev.thomasglasser.basiclibrarymanagement.util.Validator;

import static dev.thomasglasser.basiclibrarymanagement.Main.data;
import static dev.thomasglasser.basiclibrarymanagement.Main.in;

public class CheckOutMenus
{
	public static void menu()
	{
		System.out.println("Check Out Menu");
		System.out.println("""
				What would you like to do?
				1. Check out a book
				2. Return a book
				3. Return to main menu
				4. Exit""");

		int choice = Validator.validateInt(in.next()).orElse(-1);
		in.nextLine();
		switch (choice)
		{
			case 1:
				checkOutBook();
				break;
			case 2:
				returnBook();
				break;
			case 3:
				Main.main(null);
				break;
			case 4:
				Utils.exit();
				break;
			default:
				Utils.invalidChoice();
				menu();
		}
	}

	public static void checkOutBook()
	{
		Book book = BookMenus.retrieveBook();
		// Cancels operation if book is already checked out
		if (book.isCheckedOut())
		{
			System.out.println("This book is already checked out. Please try again or return this book before checking it out.");
			menu();
		}
		else
		{
			// Updates book and patron as checked out and notifies users
			Patron patron = PatronMenus.retrievePatron();
			book.setStatus(Book.Status.CHECKED_OUT);
			patron.getCheckedOutBooks().add(book.getIsbn());
			System.out.println("Checked out " + book.getTitle() + " for patron " + patron.getName());
			data.save();
			menu();
		}
	}

	public static void returnBook()
	{
		Book book = BookMenus.retrieveBook();
		Patron patron = PatronMenus.retrievePatron();
		// Cancels operation if book is not checked out
		if (patron.getCheckedOutBooks().contains(book.getIsbn()))
		{
			// Updates book and patron as not checked out and notifies user
			book.setStatus(Book.Status.AVAILABLE);
			patron.getCheckedOutBooks().remove(book.getIsbn());
			data.save();
			System.out.println("Returned " + book.getTitle() + " for patron " + patron.getName());
		}
		else
		{
			System.out.println(patron.getName() + " does not have this book checked out.");
		}

		menu();
	}
}
