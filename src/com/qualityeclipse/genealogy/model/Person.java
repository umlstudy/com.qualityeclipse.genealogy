package com.qualityeclipse.genealogy.model;

import java.util.*;

import com.qualityeclipse.genealogy.model.listener.PersonListener;

/**
 * A person in the {@link GenealogyGraph}
 */
public class Person extends GenealogyElement
	implements NoteContainer
{
	public enum Gender {
		MALE, FEMALE
	};

	private String name = "";
	private final Gender gender;
	private int birthYear = -1;
	private int deathYear = -1;
	private Marriage marriage;
	private Marriage parentsMarriage;
	private final List<Note> notes = new ArrayList<Note>();
	private final Collection<PersonListener> listeners = new HashSet<PersonListener>();

	public Person(Gender gender) {
		this.gender = gender;
	}

	public Gender getGender() {
		return gender;
	}

	//============================================================
	// Name

	public String getName() {
		return name;
	}

	public boolean setName(String newName) {
		if (newName == null)
			newName = "";
		if (name.equals(newName))
			return false;
		name = newName;
		for (PersonListener l : listeners)
			l.nameChanged(newName);
		return true;
	}

	//============================================================
	// Birth and Death

	public int getBirthYear() {
		return birthYear;
	}

	public boolean setBirthYear(int newBirthYear) {
		if (birthYear == newBirthYear)
			return false;
		birthYear = newBirthYear;
		for (PersonListener l : listeners)
			l.birthYearChanged(birthYear);
		return true;
	}

	public int getDeathYear() {
		return deathYear;
	}

	public boolean setDeathYear(int newDeathYear) {
		if (deathYear == newDeathYear)
			return false;
		deathYear = newDeathYear;
		for (PersonListener l : listeners)
			l.deathYearChanged(deathYear);
		return true;
	}

	//============================================================
	// Marriage and Offspring

	/**
	 * Answer the marriage for which this person is a husband or wife or <code>null</code>
	 * if this person is not currently a husband or wife
	 */
	public Marriage getMarriage() {
		return marriage;
	}

	/**
	 * Set the marriage for which this person is a husband or wife or <code>null</code> if
	 * this person is not currently a husband or wife
	 */
	public boolean setMarriage(Marriage newMarriage) {
		if (marriage == newMarriage)
			return false;
		final Marriage oldMarriage = marriage;
		if (marriage != null) {
			marriage = null;
			if (getGender() == Gender.MALE)
				oldMarriage.setHusband(null);
			else
				oldMarriage.setWife(null);
		}
		marriage = newMarriage;
		if (marriage != null) {
			if (getGender() == Gender.MALE)
				marriage.setHusband(this);
			else
				marriage.setWife(this);
		}
		for (PersonListener l : listeners)
			l.marriageChanged(marriage, oldMarriage);
		return true;
	}

	/**
	 * Answer the marriage for which this person is an offspring or <code>null</code> if
	 * this person is not currently recorded as an offspring.
	 */
	public Marriage getParentsMarriage() {
		return parentsMarriage;
	}

	/**
	 * Set the marriage for which this person is an offspring or <code>null</code> if this
	 * person is not currently recorded as an offspring.
	 */
	public boolean setParentsMarriage(Marriage newParentMarriage) {
		if (parentsMarriage == newParentMarriage)
			return false;
		final Marriage oldParentMarriage = parentsMarriage;
		if (parentsMarriage != null) {
			parentsMarriage = null;
			oldParentMarriage.removeOffspring(this);
		}
		parentsMarriage = newParentMarriage;
		if (parentsMarriage != null)
			parentsMarriage.addOffspring(this);
		for (PersonListener l : listeners)
			l.parentsMarriageChanged(parentsMarriage, oldParentMarriage);
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
		for (PersonListener l : listeners)
			l.noteAdded(index, n);
		return true;
	}

	public boolean removeNote(Note n) {
		if (n == null || !notes.remove(n))
			return false;
		for (PersonListener l : listeners)
			l.noteRemoved(n);
		return true;
	}

	//============================================================
	// Listeners
	
	public void addPersonListener(PersonListener l) {
		listeners.add(l);
	}
	
	public void removePersonListener(PersonListener l) {
		listeners.remove(l);
	}
	
	//============================================================
	// GenealogyElement

	protected void fireLocationChanged(int newX, int newY) {
		for (PersonListener l : listeners)
			l.locationChanged(newX, newY);
	}

	protected void fireSizeChanged(int newWidth, int newHeight) {
		for (PersonListener l : listeners)
			l.sizeChanged(newWidth, newHeight);
	}
}
