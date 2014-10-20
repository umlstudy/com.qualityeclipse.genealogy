package com.qualityeclipse.genealogy.commands;

import org.eclipse.gef.commands.Command;

public abstract class CreateConnectionCommand extends Command {

	public CreateConnectionCommand() {
		setLabel("Create " + getConnectionName());
	}

	public abstract String getConnectionName();
	
	public abstract boolean isValidSource(Object source);
	public abstract boolean isValidTarget(Object target);

	public abstract void setSource(Object source);
	public abstract void setTarget(Object target);

}
