package com.qualityeclipse.genealogy.commands;

import org.eclipse.gef.commands.Command;

import com.qualityeclipse.genealogy.model.*;

/**
 * Command to delete a person from the genealogy graph
 */
public class DeletePersonCommand extends Command
{
	private final GenealogyGraph graph;
	private final Person person;
	private Marriage marriage;
	private Marriage parentsMarriage;

	public DeletePersonCommand(GenealogyGraph graph, Person person) {
		super("Delete " + person.getName());
		this.graph = graph;
		this.person = person;
	}
	
	/**
	 * Delete the person and any connection to and from the person
	 * from the genealogy graph
	 */
	public void execute() {
		marriage = person.getMarriage();
		parentsMarriage = person.getParentsMarriage();
		person.setMarriage(null);
		person.setParentsMarriage(null);
		graph.removePerson(person);
	}
	
	/**
	 * Restore the deleted person and any connections to and from the person
	 * to the genealogy graph
	 */
	public void undo() {
		graph.addPerson(person);
		person.setParentsMarriage(parentsMarriage);
		person.setMarriage(marriage);
	}
}
