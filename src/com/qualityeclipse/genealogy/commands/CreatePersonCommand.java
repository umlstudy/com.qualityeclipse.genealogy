package com.qualityeclipse.genealogy.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.qualityeclipse.genealogy.model.GenealogyGraph;
import com.qualityeclipse.genealogy.model.Person;

public class CreatePersonCommand extends Command {
	private final GenealogyGraph graph;
	private final Person person;
	private final Rectangle box;
	
	
	public CreatePersonCommand(GenealogyGraph g, Person p, Rectangle box ) {
		super("Create Person");
		this.graph = g;
		this.person = p;
		this.box = box;
	}
	
	public void execute() {
		person.setLocation(box.x, box.y);
		person.setSize(box.width, box.height);
		graph.addPerson(person);
	}
	
	public void undo() {
		graph.removePerson(person);
	}
}
