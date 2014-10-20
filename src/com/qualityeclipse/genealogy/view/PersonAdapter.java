package com.qualityeclipse.genealogy.view;

import java.util.*;

import org.eclipse.swt.graphics.Image;

import com.qualityeclipse.genealogy.figures.*;
import com.qualityeclipse.genealogy.model.*;
import com.qualityeclipse.genealogy.model.listener.PersonListener;

/**
 * Creates and manages a {@link PersonFigure} to represent an associated {@link Person}
 * in a {@link GenealogyGraph}.
 */
public class PersonAdapter extends GenealogyElementAdapter
	implements PersonListener
{
	private final Person person;
	private final Map<Note, NoteAdapter> noteAdapters = new HashMap<Note, NoteAdapter>();

	public PersonAdapter(Person person) {
		super(person,
			new PersonFigure(person.getName(), getImage(person), person.getBirthYear(), person.getDeathYear()));
		this.person = person;
		List<Note> notes = person.getNotes();
		int notesSize = notes.size();
		for (int i = 0; i < notesSize; i++)
			noteAdded(i, notes.get(i));
		person.addPersonListener(this);
	}

	private static Image getImage(Person person) {
		return person.getGender() == Person.Gender.MALE ? PersonFigure.MALE : PersonFigure.FEMALE;
	}

	public PersonFigure getFigure() {
		return (PersonFigure) super.getFigure();
	}

	public void nameChanged(String newName) {
		getFigure().setName(newName);
	}
	
	public void birthYearChanged(int birthYear) {
		getFigure().setBirthAndDeathYear(birthYear, person.getDeathYear());
	}

	public void deathYearChanged(int deathYear) {
		getFigure().setBirthAndDeathYear(person.getBirthYear(), deathYear);
	}

	public void marriageChanged(Marriage marriage, Marriage oldMarriage) {
		// Ignored... see MarriageAdapter
	}

	public void parentsMarriageChanged(Marriage marriage, Marriage oldMarriage) {
		// Ignored... see MarriageAdapter
	}

	public void noteAdded(int index, Note note) {
		NoteAdapter adapter = new NoteAdapter(note);
		getFigure().getNotesContainer().add(adapter.getFigure(), index);
		noteAdapters.put(note, adapter);
	}

	public void noteRemoved(Note n) {
		NoteAdapter adapter = noteAdapters.get(n);
		getFigure().getNotesContainer().remove(adapter.getFigure());
		adapter.dispose();
	}

	public void dispose() {
		for (NoteAdapter adapter : noteAdapters.values())
			adapter.dispose();
		person.removePersonListener(this);
	}
}
