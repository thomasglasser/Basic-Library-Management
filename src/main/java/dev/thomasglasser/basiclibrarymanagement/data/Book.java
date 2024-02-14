package dev.thomasglasser.basiclibrarymanagement.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class Book
{
	// Serializer
	public static final Codec<Book> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.LONG.fieldOf("isbn").forGetter(Book::getIsbn),
		Codec.STRING.fieldOf("title").forGetter(Book::getTitle),
		Codec.STRING.fieldOf("author").forGetter(Book::getAuthor),
		Codec.SHORT.fieldOf("publication_year").forGetter(Book::getPublicationYear),
		Codec.STRING.xmap(Status::valueOf, Status::name).optionalFieldOf("status", Status.AVAILABLE).forGetter(Book::getStatus)
	).apply(instance, Book::new));

	private long isbn;
	private String title;
	private String author;
	private short publicationYear;
	private Status status;

	private Book(long isbn, String title, String author, short publicationYear, Status status)
	{
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.publicationYear = publicationYear;
		this.status = status;
	}

	public Book(long isbn, String title, String author, short publicationYear)
	{
		this(isbn, title, author, publicationYear, Status.AVAILABLE);
	}

	public long getIsbn()
	{
		return isbn;
	}

	public String getTitle()
	{
		return title;
	}

	public String getAuthor()
	{
		return author;
	}

	public short getPublicationYear()
	{
		return publicationYear;
	}

	public Status getStatus()
	{
		return status;
	}

	public void setStatus(Status status)
	{
		this.status = status;
	}

	public void setPublicationYear(short publicationYear)
	{
		this.publicationYear = publicationYear;
	}

	public void setIsbn(long isbn)
	{
		this.isbn = isbn;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public boolean isCheckedOut()
	{
		return status == Status.CHECKED_OUT;
	}

	// Makes book data readable by user
	@Override
	public String toString()
	{
		return "Book[ISBN=" + isbn +
				", Title=" + title +
				", Author=" + author +
				", Publication Year=" + publicationYear +
				", Status=" + status.getName() + "]";
	}

	// Check out status, whether available or not, and holds user readable name
	public enum Status
	{
		AVAILABLE("Available"),
		CHECKED_OUT("Checked Out");

		final String name;

		Status(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}
	}
}
