package com.qualityeclipse.genealogy.model.listener;

import com.qualityeclipse.genealogy.model.Marriage;
import com.qualityeclipse.genealogy.model.Person;

/**
 * Used by {@link Person} to notify others when changes occur.
 */
public interface PersonListener
	extends NoteContainerListener, GenealogyElementListener
{
	void nameChanged(String newName);
	void birthYearChanged(int birthYear);
	void deathYearChanged(int deathYear);
	void marriageChanged(Marriage marriage, Marriage oldMarriage);
	void parentsMarriageChanged(Marriage marriage, Marriage oldMarriage);
}
