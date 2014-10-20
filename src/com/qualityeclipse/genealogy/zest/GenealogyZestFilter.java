package com.qualityeclipse.genealogy.zest;

import org.eclipse.jface.viewers.*;

import com.qualityeclipse.genealogy.model.Person;
import com.qualityeclipse.genealogy.model.Person.Gender;

/**
 * Filter the {@link GenealogyZestView}'s content based upon the specified gender.
 */
class GenealogyZestFilter extends ViewerFilter
{
	private final Gender gender;

	GenealogyZestFilter(Gender gender) {
		this.gender = gender;
	}

	public boolean select(Viewer viewer, Object parent, Object element) {
		if (element instanceof Person) {
			Person p = (Person) element;
			if (p.getGender() != gender)
				return false;
		}
		return true;
	}
}