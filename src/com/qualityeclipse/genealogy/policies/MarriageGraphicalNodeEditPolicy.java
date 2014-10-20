package com.qualityeclipse.genealogy.policies;

import org.eclipse.draw2d.Connection;
import org.eclipse.gef.Request;

import com.qualityeclipse.genealogy.commands.*;
import com.qualityeclipse.genealogy.model.Marriage;
import com.qualityeclipse.genealogy.parts.*;

/**
 * The graphical node editing policy for {@link MarriageEditPart}
 * for handling connection creation and modification.
 */
public final class MarriageGraphicalNodeEditPolicy extends GenealogyElementGraphicalNodeEditPolicy
{
	private final Marriage marriage;

	public MarriageGraphicalNodeEditPolicy(Marriage marriage) {
		this.marriage = marriage;
	}

	/**
	 * Answer the model element associated with the receiver
	 */
	protected Object getModel() {
		return marriage;
	}

	/**
	 * Answer a new connection command for the receiver.
	 */
	public CreateConnectionCommand createConnectionCommand() {
		return new CreateOffspringConnectionCommand(marriage);
	}
	
	/**
	 * Answer a figure to be used during connection creation
	 */
	protected Connection createDummyConnection(Request req) {
		return GenealogyConnectionEditPart.createFigure(true);
	}
}