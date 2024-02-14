package dev.thomasglasser.basiclibrarymanagement;

import dev.thomasglasser.basiclibrarymanagement.data.Book;
import dev.thomasglasser.basiclibrarymanagement.util.Finders;
import dev.thomasglasser.basiclibrarymanagement.util.Utils;
import dev.thomasglasser.basiclibrarymanagement.util.Validator;

import java.util.List;
import java.util.Optional;

import static dev.thomasglasser.basiclibrarymanagement.Main.data;
import static dev.thomasglasser.basiclibrarymanagement.Main.in;

public class BookMenus
{
	public static void menu()
	{
		System.out.println("Book Menu");
		System.out.println("""
				What would you like to do?
				1. Add a book
				2. Update a book
				3. Delete a book
				4. Search for books
				5. List all books
				6. Return to main menu
				7. Exit""");
		int choice = Validator.validateInt(in.next()).orElse(-1);
		in.nextLine();
		switch (choice)
		{
			case 1:
				addBook();
				break;
			case 2:
				updateBook(null);
				break;
			case 3:
				deleteBook();
				break;
			case 4:
				searchBooks();
				break;
			case 5:
				listBooks();
				break;
			case 6:
				Main.main(null);
				break;
			case 7:
				Utils.exit();
			default:
				Utils.invalidChoice();
				menu();
		}
	}

	// Finds a book in the database, validating data
	public static Book retrieveBook()
	{
		Book book = null;
		do
		{
			System.out.print("Enter the ISBN of the book, ? if you don't know it, or q to quit: ");
			String input = in.next();
			in.nextLine();
			if (input.equalsIgnoreCase("q"))
			{
				menu();
				return null;
			}
			else if (input.equalsIgnoreCase("?"))
			{
				searchBooks();
				return null;
			}
			Optional<Long> isbn = Validator.validateIsbn(input);
			if (isbn.isPresent())
			{
				List<Book> found = Finders.Books.fromISBN(data.books(), isbn.get());
				if (found.isEmpty())
				{
					System.out.println("No books found with that ISBN. Please try again.");
				}
				else
				{
					book = found.get(0);
				}
			}
		} while (book == null);

		return book;
	}

	// Adds a book, validating data, and notifies the user
	public static void addBook()
	{
		long isbn;
		do
		{
			System.out.print("Enter the ISBN of the book or q to quit: ");
			String input = in.next();
			in.nextLine();
			if (input.equalsIgnoreCase("q"))
			{
				menu();
				return;
			}
			isbn = Validator.validateIsbn(input).orElse(-1L);
		} while (isbn == -1L);
		String title;
		do
		{
			System.out.print("Enter the title of the book or q to quit: ");
			title = in.nextLine();
			if (title.equalsIgnoreCase("q"))
			{
				menu();
				return;
			}
		} while (!Validator.isNonEmptyString(title, "Title"));
		String author;
		do
		{
			System.out.print("Enter the author of the book or q to quit: ");
			author = in.nextLine();
			if (author.equalsIgnoreCase("q"))
			{
				menu();
				return;
			}
		} while (!Validator.isNonEmptyString(author, "Author"));
		short year;
		do
		{
			System.out.print("Enter the year of the book or q to quit: ");
			String input = in.next();
			in.nextLine();
			if (input.equalsIgnoreCase("q"))
			{
				menu();
				return;
			}
			year = Validator.validateYear(input).orElse((short) -1);
		} while (year == -1);

		data.books().add(new Book(isbn, title, author, year));
		System.out.println("Book " + isbn + " - \"" + title + "\" added successfully!");
		data.save();

		menu();
	}

	// Modifies a book, validating data, and notifies the user
	public static void updateBook(Book book)
	{
		if (book == null)
			book = retrieveBook();

		System.out.println("Updating book: " + book);

		System.out.println("""
				What would you like to update?
				1. ISBN
				2. Title
				3. Author
				4. Year
				5. Return to menu
				6. Exit""");
		int choice = Validator.validateInt(in.next()).orElse(-1);
		in.nextLine();
		String input;
		switch (choice)
		{
			case 1:
				System.out.print("Enter the new ISBN or q to quit: ");
				input = in.next();
				in.nextLine();
				if (input.equalsIgnoreCase("q"))
				{
					menu();
					return;
				}
				Optional<Long> isbn = Validator.validateIsbn(input);
				isbn.ifPresent(book::setIsbn);
				break;
			case 2:
				System.out.print("Enter the new title or q to quit: ");
				input = in.nextLine();
				if (input.equalsIgnoreCase("q"))
				{
					menu();
					return;
				}
				if (Validator.isNonEmptyString(input, "Title"))
				{
					book.setTitle(input);
				}
				break;
			case 3:
				System.out.print("Enter the new author or q to quit: ");
				input = in.nextLine();
				if (input.equalsIgnoreCase("q"))
				{
					menu();
					return;
				}
				if (Validator.isNonEmptyString(input, "Author"))
				{
					book.setAuthor(input);
				}
				break;
			case 4:
				System.out.print("Enter the new year or q to quit: ");
				input = in.next();
				in.nextLine();
				if (input.equalsIgnoreCase("q"))
				{
					menu();
					return;
				}
				Optional<Short> year = Validator.validateYear(input);
				year.ifPresent(book::setPublicationYear);
				break;
			case 5:
				menu();
				return;
			case 6:
				Utils.exit();
			default:
				Utils.invalidChoice();
				updateBook(book);
				return;
		}
		data.save();
		System.out.println("Book updated.");
		updateBook(book);
	}

	// Deletes a book, validating data, and notifies the user
	public static void deleteBook()
	{
		Book book = retrieveBook();
		data.books().remove(book);
		data.save();
		System.out.println("Book deleted.");
		menu();
	}

	// Takes user input and lists matching books based off of title, author, or status, validating input
	public static void searchBooks()
	{
		System.out.println("""
				What would you like to search by?
				1. Title
				2. Author
				3. Status
				4. Return to menu
				5. Exit""");
		int choice = Validator.validateInt(in.next()).orElse(-1);
		in.nextLine();
		String input;
		List<Book> found = List.of();
		switch (choice)
		{
			case 1:
				System.out.print("Enter the book title or q to quit: ");
				input = in.nextLine();
				if (input.equalsIgnoreCase("q"))
				{
					menu();
					return;
				}
				if (Validator.isNonEmptyString(input, "Title"))
				{
					found = Finders.Books.fromTitle(data.books(), input);
				}
				break;
			case 2:
				System.out.print("Enter the book author or q to quit: ");
				input = in.nextLine();
				if (input.equalsIgnoreCase("q"))
				{
					menu();
					return;
				}
				if (Validator.isNonEmptyString(input, "Author"))
				{
					found = Finders.Books.fromAuthor(data.books(), input);
				}
				break;
			case 3:
				System.out.println("""
						What books would you like to see?
						1. Available Books
						2. Checked Out Books
						3. Return to menu""");
				int searchChoice = Validator.validateInt(in.next()).orElse(-1);
				in.nextLine();
				Book.Status status;
				switch (searchChoice)
				{
					case 1:
						status = Book.Status.AVAILABLE;
						break;
					case 2:
						status = Book.Status.CHECKED_OUT;
						break;
					default:
						Utils.invalidChoice();
					case 3:
						searchBooks();
						return;
				}
				found = Finders.Books.fromStatus(data.books(), status);
				break;
			case 4:
				Main.main(null);
				return;
			case 5:
				Utils.exit();
			default:
				Utils.invalidChoice();
				searchBooks();
				return;
		}
		System.out.println("Books found:");
		found.forEach(System.out::println);
		Main.main(null);
	}

	// Lists all books
	public static void listBooks()
	{
		System.out.println("Books:");
		data.books().forEach(System.out::println);
		menu();
	}
}
