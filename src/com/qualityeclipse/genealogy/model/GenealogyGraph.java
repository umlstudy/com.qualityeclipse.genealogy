package com.qualityeclipse.genealogy.model;

import java.util.*;

import com.qualityeclipse.genealogy.model.listener.GenealogyGraphListener;

/**
 * The root model object representing a genealogy graph and directly or indirectly
 * containing all other model objects.
 */
public class GenealogyGraph
	implements NoteContainer
{
	private final Collection<Person> people = new HashSet<Person>();
	private final Collection<Marriage> marriages = new HashSet<Marriage>();
	private final List<Note> notes = new ArrayList<Note>();
	private final Collection<GenealogyGraphListener> listeners = new HashSet<GenealogyGraphListener>();
	
	/**
	 * Discard all elements so that new information can be loaded
	 */
	public void clear() {
		people.clear();
		marriages.clear();
		notes.clear();
		for (GenealogyGraphListener l : listeners)
			l.graphCleared();
	}

	//============================================================
	// People

	public Collection<Person> getPeople() {
		return people;
	}

	public boolean addPerson(Person p) {
		if (p == null || !people.add(p))
			return false;
		for (GenealogyGraphListener l : listeners)
			l.personAdded(p);
		return true;
	}

	public boolean removePerson(Person p) {
		if (!people.remove(p))
			return false;
		for (GenealogyGraphListener l : listeners)
			l.personRemoved(p);
		return true;
	}

	//============================================================
	// Marriages

	public Collection<Marriage> getMarriages() {
		return marriages;
	}

	public boolean addMarriage(Marriage m) {
		if (m == null || !marriages.add(m))
			return false;
		for (GenealogyGraphListener l : listeners)
			l.marriageAdded(m);
		return true;
	}

	public boolean removeMarriage(Marriage m) {
		if (!marriages.remove(m))
			return false;
		for (GenealogyGraphListener l : listeners)
			l.marriageRemoved(m);
		return true;
	}

	//============================================================
	// Notes
	
	public List<Note> getNotes() {
		return notes;
	}

	public boolean addNote(Note n) {
		return addNote(notes.size(), n);
	}

	public boolean addNote(int index, Note n) {
		if (n == null || notes.contains(n))
			return false;
		notes.add(index, n);
		for (GenealogyGraphListener l : listeners)
			l.noteAdded(index, n);
		return true;
	}

	public boolean removeNote(Note n) {
		if (n == null || !notes.remove(n))
			return false;
		for (GenealogyGraphListener l : listeners)
			l.noteRemoved(n);
		return true;
	}

	//============================================================
	// Listeners
	
	public void addGenealogyGraphListener(GenealogyGraphListener l) {
		listeners.add(l);
	}
	
	public void removeGenealogyGraphListener(GenealogyGraphListener l) {
		listeners.remove(l);
	}
}
