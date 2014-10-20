package com.qualityeclipse.genealogy.commands;

import com.qualityeclipse.genealogy.model.*;

/**
 * Add a {@link Person} to a {@link Marriage} as an offspring
 */
public class CreateOffspringConnectionCommand extends CreateConnectionCommand
{
	private Marriage marriage;
	private Person person;
	private Marriage oldMarriage;

	public CreateOffspringConnectionCommand(Marriage marriage) {
		this.marriage = marriage;
	}
	
	public String getConnectionName() {
		return "Offspring Connection";
	}

	/**
	 * Determine if the specified model object is an appropriate source for the
	 * connection. This method is appropriate after {@link #setTarget(Object)} has been
	 * called with a non-<code>null</code> value.
	 * 
	 * @see CreateConnectionCommand#isValidSource(Object)
	 */
	public boolean isValidSource(Object source) {
		if (!(source instanceof Marriage))
			return false;
		if (person == null)
			return true;
		if (person.getParentsMarriage() == source)
			return false;
		if (person.getMarriage() == source)
			return false;
		return true;
	}
	
	public void setSource(Object source) {
		marriage = (Marriage) source;
	}

	/**
	 * Determine if the specified object is an appropriate target for the receiver and
	 * that the specified object is not already connected to the receiver.
	 * 
	 * @see CreateConnectionCommand#isValidTarget(Object)
	 */
	public boolean isValidTarget(Object target) {
		if (!(target instanceof Person))
			return false;
		if (marriage.getOffspring().contains(target))
			return false;
		if (marriage.getHusband() == target)
			return false;
		if (marriage.getWife() == target)
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
		this.person = (Person) target;
	}

	/**
	 * Add the person as an offspring of the marriage while caching the original marriage
	 * that had the person as an offspring
	 */
	public void execute() {
		oldMarriage = person.getParentsMarriage();
		person.setParentsMarriage(marriage);
	}

	/**
	 * Restore the original marriage having the person as an offspring
	 */
	public void undo() {
		person.setParentsMarriage(oldMarriage);
	}
}
