package com.qualityeclipse.genealogy.policies;

import org.eclipse.draw2d.Connection;
import org.eclipse.gef.Request;

import com.qualityeclipse.genealogy.commands.*;
import com.qualityeclipse.genealogy.model.Person;
import com.qualityeclipse.genealogy.parts.*;

/**
 * The graphical node editing policy for {@link PersonEditPart}
 * for handling connection creation and modification.
 */
public class PersonGraphicalNodeEditPolicy extends GenealogyElementGraphicalNodeEditPolicy
{
	private final Person person;

	public PersonGraphicalNodeEditPolicy(Person person) {
		this.person = person;
	}

	/**
	 * Answer the model element associated with the receiver
	 */
	protected Object getModel() {
		return person;
	}

	/**
	 * Answer a new connection command for the receiver.
	 */
	public CreateConnectionCommand createConnectionCommand() {
		return new CreateSpouseConnectionCommand(person);
	}
	
	/**
	 * Answer a figure to be used during connection creation
	 */
	protected Connection createDummyConnection(Request req) {
		return GenealogyConnectionEditPart.createFigure(false);
	}
}