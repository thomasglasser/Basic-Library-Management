package dev.thomasglasser.basiclibrarymanagement;

import dev.thomasglasser.basiclibrarymanagement.data.Patron;
import dev.thomasglasser.basiclibrarymanagement.util.Finders;
import dev.thomasglasser.basiclibrarymanagement.util.Utils;
import dev.thomasglasser.basiclibrarymanagement.util.Validator;

import java.util.List;
import java.util.Optional;

import static dev.thomasglasser.basiclibrarymanagement.Main.data;
import static dev.thomasglasser.basiclibrarymanagement.Main.in;

public class PatronMenus
{
	protected static void menu()
	{
		System.out.println("Patron Menu");
		System.out.println("""
				What would you like to do?
				1. Add a patron
				2. Update a patron
				3. Delete a patron
				4. Return to main menu
				5. Exit""");

		int choice = Validator.validateInt(in.next()).orElse(-1);
		in.nextLine();
		switch (choice)
		{
			case 1:
				addPatron();
				break;
			case 2:
				updatePatron(null);
				break;
			case 3:
				deletePatron();
				break;
			case 4:
				Main.main(null);
				break;
			case 5:
				Utils.exit();
			default:
				Utils.invalidChoice();
				menu();
		}
	}

	// Finds a patron in the database, validating data
	protected static Patron retrievePatron()
	{
		Patron patron = null;
		do
		{
			System.out.print("Enter the ID of the patron or q to quit: ");
			String input = in.next();
			in.nextLine();
			if (input.equalsIgnoreCase("q"))
			{
				menu();
				return null;
			}
			Optional<Integer> id = Validator.validateInt(input);
			if (id.isPresent())
			{
				List<Patron> found = Finders.Patrons.fromId(data.patrons(), id.get());
				if (found.isEmpty())
				{
					System.out.println("No patrons found with that ID. Please try again.");
				}
				else
				{
					patron = found.get(0);
				}
			}
		} while (patron == null);

		return patron;
	}

	// Adds a patron, validating data, and notifies the user
	protected static void addPatron()
	{
		long id;
		do
		{
			System.out.print("Enter the ID of the patron or q to quit: ");
			String input = in.next();
			in.nextLine();
			if (input.equalsIgnoreCase("q"))
			{
				menu();
				return;
			}
			id = Validator.validateLong(input).orElse(-1L);
		} while (id == -1L);
		String name;
		do
		{
			System.out.print("Enter the name of the patron or q to quit: ");
			name = in.nextLine();
			if (name.equalsIgnoreCase("q"))
			{
				menu();
				return;
			}
		} while (!Validator.isNonEmptyString(name, "Name"));

		data.patrons().add(new Patron(id, name));
		System.out.println("Patron " + id + " - \"" + name + "\" added successfully!");
		data.save();

		menu();
	}

	// Modifies a patron, validating data, and notifies the user
	protected static void updatePatron(Patron patron)
	{
		if (patron == null)
			patron = retrievePatron();

		System.out.println("Updating patron: " + patron);

		System.out.println("""
				What would you like to update?
				1. ID
				2. Name
				3. Return to menu
				4. Exit""");
		int choice = Validator.validateInt(in.next()).orElse(-1);
		in.nextLine();
		String input;
		switch (choice)
		{
			case 1:
				System.out.print("Enter the new ID or q to quit: ");
				input = in.next();
				in.nextLine();
				if (input.equalsIgnoreCase("q"))
				{
					menu();
					return;
				}
				Optional<Long> id = Validator.validateLong(input);
				id.ifPresent(patron::setId);
				break;
			case 2:
				System.out.print("Enter the new name or q to quit: ");
				input = in.nextLine();
				if (input.equalsIgnoreCase("q"))
				{
					menu();
					return;
				}
				if (Validator.isNonEmptyString(input, "Name"))
				{
					patron.setName(input);
				}
				break;
			case 3:
				menu();
				return;
			case 4:
				Utils.exit();
			default:
				Utils.invalidChoice();
				updatePatron(patron);
				return;
		}
		data.save();
		System.out.println("Patron updated.");
		updatePatron(patron);
	}

	// Deletes a patron, validating data, and notifies the user
	protected static void deletePatron()
	{
		Patron patron = retrievePatron();

		data.patrons().remove(patron);
		data.save();
		System.out.println("Patron deleted.");
		menu();
	}
}