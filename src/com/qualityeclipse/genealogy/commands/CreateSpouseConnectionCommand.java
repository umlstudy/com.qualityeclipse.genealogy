package com.qualityeclipse.genealogy.commands;

import com.qualityeclipse.genealogy.model.*;

/**
 * Command for creating a spouse connection
 * between a {@link Person} and a {@link Marriage}.
 */
public class CreateSpouseConnectionCommand extends CreateConnectionCommand
{
	private Person person;
	private Marriage marriage;
	private Marriage oldMarriage;
	private Person oldHusband;
	private Person oldWife;

	public CreateSpouseConnectionCommand(Person person) {
		this.person = person;
	}
	
	public String getConnectionName() {
		return "Spouse Connection";
	}

	/**
	 * Determine if the specified model object is an appropriate source for the
	 * connection. This method is appropriate after {@link #setTarget(Object)} has been
	 * called with a non-<code>null</code> value.
	 * 
	 * @see CreateConnectionCommand#isValidSource(Object)
	 */
	public boolean isValidSource(Object source) {
		if (!(source instanceof Person))
			return false;
		if (marriage == null)
			return true;
		if (marriage.getOffspring().contains(source))
			return false;
		if (marriage.getHusband() == source)
			return false;
		if (marriage.getWife() == source)
			return false;
		return true;
	}
	
	public void setSource(Object source) {
		person = (Person) source;
	}

	/**
	 * Determine if the specified object 
	 * is an appropriate target for the receiver
	 * @see CreateConnectionCommand#isValidTarget(Object)
	 */
	public boolean isValidTarget(Object target) {
		if (!(target instanceof Marriage))
			return false;
		if (person.getMarriage() == target)
			return false;
		if (person.getParentsMarriage() == target)
			return false;
		return true;
	}

	/**
	 * Set the target for the connection to be created.
	 * 
	 * @param target the target
	 * @throws IllegalArgumentException if the target is not valid for the receiver
	 */
	public void setTarget(Object target) {
		this.marriage = (Marriage) target;
	}

	/**
	 * Set the person as a spouse for the marriage
	 * while caching the original marriage for which the person was a spouse.
	 */
	public void execute() {
		oldMarriage = person.getMarriage();
		oldHusband = marriage.getHusband();
		oldWife = marriage.getWife();
		person.setMarriage(marriage);
	}
	
	/**
	 * Restore the person as a spouse for the original marriage.
	 */
	public void undo() {
		marriage.setWife(oldWife);
		marriage.setHusband(oldHusband);
		person.setMarriage(oldMarriage);
	}
}
