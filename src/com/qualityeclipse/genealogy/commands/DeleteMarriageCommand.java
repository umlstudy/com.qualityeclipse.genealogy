package com.qualityeclipse.genealogy.commands;

import java.util.*;

import org.eclipse.gef.commands.Command;

import com.qualityeclipse.genealogy.model.*;

/**
 * Command to delete a person from the genealogy graph
 */
public class DeleteMarriageCommand extends Command
{
	private final GenealogyGraph graph;
	private final Marriage marriage;
	private Person husband;
	private Person wife;
	private Collection<Person> offspring;

	public DeleteMarriageCommand(GenealogyGraph graph, Marriage marriage) {
		super("Delete Marriage");
		this.graph = graph;
		this.marriage = marriage;
	}
	
	/**
	 * Delete the marriage and any connection to and from the marriage
	 * from the genealogy graph
	 */
	public void execute() {
		husband = marriage.getHusband();
		wife = marriage.getWife();
		offspring = new ArrayList<Person>(marriage.getOffspring());
		marriage.setHusband(null);
		marriage.setWife(null);
		for (Person p : offspring)
			marriage.removeOffspring(p);
		graph.removeMarriage(marriage);
	}
	
	/**
	 * Restore the deleted marriage and any connections to and from the marriage
	 * to the genealogy graph
	 */
	public void undo() {
		graph.addMarriage(marriage);
		for (Person p : offspring)
			marriage.addOffspring(p);
		marriage.setWife(wife);
		marriage.setHusband(husband);
	}
}
