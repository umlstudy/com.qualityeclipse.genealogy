package com.qualityeclipse.genealogy.model;

import java.util.*;

import com.qualityeclipse.genealogy.model.listener.MarriageListener;

/**
 * A marriage between a husband and wife that has zero or more offspring.
 */
public final class Marriage extends GenealogyElement
{
	private int yearMarried = -1;
	private Person husband;
	private Person wife;
	private final Collection<Person> offspring = new HashSet<Person>();
	private final Collection<MarriageListener> listeners = new HashSet<MarriageListener>();

	public Marriage() {
	}

	public Marriage(int year) {
		setYearMarried(year);
	}

	public int getYearMarried() {
		return yearMarried;
	}

	public boolean setYearMarried(int newYearMarried) {
		if (yearMarried == newYearMarried)
			return false;
		yearMarried = newYearMarried;
		for (MarriageListener l : listeners)
			l.yearMarriedChanged(yearMarried);
		return true;
	}

	//============================================================
	// Husband and Wife
	
	public Person getHusband() {
		return husband;
	}
	
	public boolean setHusband(Person newHusband) {
		if (newHusband != null && newHusband.getGender() != Person.Gender.MALE)
			return false;
		if (husband == newHusband)
			return false;
		final Person oldHusband = husband;
		if (husband != null) {
			husband = null;
			oldHusband.setMarriage(null);
		}
		husband = newHusband;
		if (husband != null)
			husband.setMarriage(this);
		for (MarriageListener l : listeners)
			l.husbandChanged(husband, oldHusband);
		return true;
	}
	
	public Person getWife() {
		return wife;
	}
	
	public boolean setWife(Person newWife) {
		if (newWife != null && newWife.getGender() != Person.Gender.FEMALE)
			return false;
		if (wife == newWife)
			return false;
		final Person oldWife = wife;
		if (wife != null) {
			wife = null;
			oldWife.setMarriage(null);
		}
		wife = newWife;
		if (wife != null)
			wife.setMarriage(this);
		for (MarriageListener l : listeners)
			l.wifeChanged(wife, oldWife);
		return true;
	}

	//============================================================
	// Offspring
	
	public Collection<Person> getOffspring() {
		return offspring;
	}
	
	public boolean addOffspring(Person p) {
		if (p == null || !offspring.add(p))
			return false;
		p.setParentsMarriage(this);
		for (MarriageListener l : listeners)
			l.offspringAdded(p);
		return true;
	}
	
	public boolean removeOffspring(Person p) {
		if (!offspring.remove(p))
			return false;
		p.setParentsMarriage(null);
		for (MarriageListener l : listeners)
			l.offspringRemoved(p);
		return true;
	}

	//============================================================
	// Listeners
	
	public void addMarriageListener(MarriageListener l) {
		listeners.add(l);
	}
	
	public void removeMarriageListener(MarriageListener l) {
		listeners.remove(l);
	}

	//============================================================
	// GenealogyElement
	
	protected void fireLocationChanged(int newX, int newY) {
		for (MarriageListener l : listeners)
			l.locationChanged(newX, newY);
	}

	protected void fireSizeChanged(int newWidth, int newHeight) {
		for (MarriageListener l : listeners)
			l.sizeChanged(newWidth, newHeight);
	}
}
