package dev.thomasglasser.basiclibrarymanagement.util;

import dev.thomasglasser.basiclibrarymanagement.data.Book;
import dev.thomasglasser.basiclibrarymanagement.data.Patron;

import java.util.List;

public class Finders
{
	// Finds book in list based on data given
	public static class Books
	{
		public static List<Book> fromISBN(List<Book> books, long isbn)
		{
			return books.stream().filter(book -> String.valueOf(book.getIsbn()).contains(String.valueOf(isbn))).toList();
		}

		public static List<Book> fromTitle(List<Book> books, String title)
		{
			return books.stream().filter(book -> book.getTitle().contains(title)).toList();
		}

		public static List<Book> fromAuthor(List<Book> books, String author)
		{
			return books.stream().filter(book -> book.getAuthor().contains(author)).toList();
		}

		public static List<Book> fromStatus(List<Book> books, Book.Status status)
		{
			return books.stream().filter(book -> book.getStatus() == status).toList();
		}
	}

	// Finds patron in list based on data given
	public static class Patrons
	{
		public static List<Patron> fromId(List<Patron> patrons, long id)
		{
			return patrons.stream().filter(patron -> String.valueOf(patron.getId()).contains(String.valueOf(id))).toList();
		}
	}
}
