package dev.thomasglasser.basiclibrarymanagement.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public record LibraryData(List<Book> books, List<Patron> patrons)
{
	// Path for the data file
	public static final String DATA_PATH = "library_data.json";

	// Serializer
	public static final Codec<LibraryData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.list(Book.CODEC).fieldOf("books").forGetter(LibraryData::books),
			Codec.list(Patron.CODEC).fieldOf("patrons").forGetter(LibraryData::patrons)
	).apply(instance, LibraryData::new));

	public LibraryData()
	{
		this(new ArrayList<>(), new ArrayList<>());
	}

	public LibraryData(List<Book> books, List<Patron> patrons)
	{
		this.books = new ArrayList<>(books);
		this.patrons = new ArrayList<>(patrons);
	}

	// Writes current data to file
	public void save()
	{
		try
		{
			FileWriter writer = new FileWriter(DATA_PATH);

			Gson gson = new GsonBuilder().setPrettyPrinting().setVersion(1).create();

			gson.toJson(CODEC.encodeStart(JsonOps.INSTANCE, this).result().orElseThrow(), writer);

			writer.close();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	// Reads data from file
	public static LibraryData load()
	{
		try
		{
			FileReader reader = new FileReader(DATA_PATH);

			Gson gson = new GsonBuilder().setPrettyPrinting().setVersion(1).create();

			LibraryData libraryData = CODEC.parse(JsonOps.INSTANCE, gson.fromJson(reader, JsonElement.class)).result().orElseThrow();

			libraryData.books.removeIf(book ->
			{
				if (libraryData.books.stream().anyMatch(otherBook -> book != otherBook && otherBook.getIsbn() == book.getIsbn()))
				{
					System.out.println("File loading error: Duplicate ISBN found! Deleting conflicting entries...");
					return true;
				}
				return false;
			});

			libraryData.patrons.removeIf(patron ->
			{
				if (libraryData.patrons.stream().anyMatch(otherPatron -> patron != otherPatron && otherPatron.getId() == patron.getId()))
				{
					System.out.println("File loading error: Duplicate patron ID found! Deleting conflicting entries...");
					return true;
				}
				return false;
			});

			reader.close();

			return libraryData;
		}
		catch (FileNotFoundException e)
		{
			// No file, nothing to load
			return new LibraryData();
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
