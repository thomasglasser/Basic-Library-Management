package dev.thomasglasser.basiclibrarymanagement.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public class Patron
{
	// Serializer
	public static final Codec<Patron> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.LONG.fieldOf("id").forGetter(Patron::getId),
		Codec.STRING.fieldOf("name").forGetter(Patron::getName),
		Codec.LONG.listOf().fieldOf("checked_out_books").forGetter(Patron::getCheckedOutBooks)
	).apply(instance, Patron::new));

	private long id;
	private String name;
	private final ArrayList<Long> checkedOutBooks;

	public Patron(long id, String name, List<Long> checkedOutBooks)
	{
		this.id = id;
		this.name = name;
		this.checkedOutBooks = new ArrayList<>(checkedOutBooks);
	}

	public Patron(long id, String name)
	{
		this.id = id;
		this.name = name;
		this.checkedOutBooks = new ArrayList<>();
	}

	public long getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public ArrayList<Long> getCheckedOutBooks()
	{
		return checkedOutBooks;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	// Makes patron data readable by user
	@Override
	public String toString()
	{
		return "Patron[ID=" + id +
				", Name=" + name +
				", Books=" + checkedOutBooks + "]";
	}
}
