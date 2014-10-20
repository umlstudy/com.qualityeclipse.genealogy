package com.qualityeclipse.genealogy.commands;

import org.eclipse.gef.commands.Command;

import com.qualityeclipse.genealogy.model.connection.GenealogyConnection;

/**
 * Command to delete a connection from the genealogy graph
 */
public class DeleteGenealogyConnectionCommand extends Command
{
	private final GenealogyConnection conn;
	private int connType;

	public DeleteGenealogyConnectionCommand(GenealogyConnection conn) {
		super("Delete Connection");
		this.conn = conn;
	}

	/**
	 * Delete the connection from the genealogy graph
	 */
	public void execute() {
		connType = 0;
		if (conn.person.getMarriage() == conn.marriage) {
			connType = 1;
			conn.person.setMarriage(null);
		}
		if (conn.person.getParentsMarriage() == conn.marriage) {
			connType = 2;
			conn.person.setParentsMarriage(null);
		}
	}
	
	/**
	 * Restore the connection in the genealogy graph
	 */
	public void undo() {
		switch (connType) {
			case 1 :
				conn.person.setMarriage(conn.marriage);
				break;
			case 2 :
				conn.person.setParentsMarriage(conn.marriage);
				break;
			default :
				break;
		}
	}
}
