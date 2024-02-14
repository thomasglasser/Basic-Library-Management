package dev.thomasglasser.basiclibrarymanagement.util;

import java.util.Optional;

public class Validator
{
	// Prints an invalid field error
	public static void showInvalidMessage(String erroredField, String fieldName)
	{
		System.out.println(erroredField + " is not a valid " + fieldName + ".");
	}

	// Ensures the text is a valid short, otherwise returns an empty Optional
	public static Optional<Short> validateShort(String maybe)
	{
		try {
			return Optional.of(Short.parseShort(maybe));
		}
		catch (NumberFormatException e)
		{
			showInvalidMessage(maybe, "number");
			return Optional.empty();
		}
	}

	// Ensures the text is a valid integer, otherwise returns an empty Optional
	public static Optional<Integer> validateInt(String maybe)
	{
		try {
			return Optional.of(Integer.parseInt(maybe));
		}
		catch (NumberFormatException e)
		{
			showInvalidMessage(maybe, "number");
			return Optional.empty();
		}
	}

	// Ensures the text is a valid long, otherwise returns an empty Optional
	public static Optional<Long> validateLong(String maybe)
	{
		try {
			return Optional.of(Long.parseLong(maybe));
		}
		catch (NumberFormatException e)
		{
			showInvalidMessage(maybe, "number");
			return Optional.empty();
		}
	}

	// Ensures the text is a non-empty string, otherwise returns false
	public static boolean isNonEmptyString(String maybe, String fieldName)
	{
		if (maybe.isEmpty())
		{
			System.out.println(fieldName + " must not be empty.");
			return false;
		}
		return true;
	}

	// Ensures the text is a valid isbn (10 or 13 digit long), otherwise returns an empty Optional
	public static Optional<Long> validateIsbn(String maybe)
	{
		Optional<Long> optionalLong = validateLong(maybe);
		if (optionalLong.isPresent())
		{
			if (maybe.length() == 10 || maybe.length() == 13)
				return optionalLong;
			showInvalidMessage(maybe, "ISBN");
		}
		return Optional.empty();
	}

	// Ensures the text is a valid year (4 digit positive or negative short), otherwise returns an empty Optional
	public static Optional<Short> validateYear(String maybe)
	{
		Optional<Short> optionalShort = validateShort(maybe);
		if (optionalShort.isPresent())
		{
			if (maybe.replaceAll("-", "").length() == 4)
				return optionalShort;
			showInvalidMessage(maybe, "Year");
		}
		return Optional.empty();
	}
}
