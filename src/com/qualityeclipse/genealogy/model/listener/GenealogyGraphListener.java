package com.qualityeclipse.genealogy.model.listener;

import com.qualityeclipse.genealogy.model.GenealogyGraph;
import com.qualityeclipse.genealogy.model.Marriage;
import com.qualityeclipse.genealogy.model.Person;

/**
 * Used by {@link GenealogyGraph} to notify others when changes occur.
 */
public interface GenealogyGraphListener
	extends NoteContainerListener
{
	void personAdded(Person p);
	void personRemoved(Person p);
	void marriageAdded(Marriage m);
	void marriageRemoved(Marriage m);
	void graphCleared();
}
