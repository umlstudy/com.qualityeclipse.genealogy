package com.qualityeclipse.genealogy.model.listener;

import com.qualityeclipse.genealogy.model.Marriage;
import com.qualityeclipse.genealogy.model.Person;

/**
 * Used by {@link Marriage} to notify others when changes occur.
 */
public interface MarriageListener
	extends GenealogyElementListener
{
	void yearMarriedChanged(int yearMarried);
	void husbandChanged(Person husband, Person oldHusband);
	void wifeChanged(Person wife, Person oldWife);
	void offspringAdded(Person p);
	void offspringRemoved(Person p);
}
