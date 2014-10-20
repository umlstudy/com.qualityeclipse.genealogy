package com.qualityeclipse.genealogy.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.qualityeclipse.genealogy.model.GenealogyGraph;
import com.qualityeclipse.genealogy.model.Marriage;

public class CreateMarriageCommand extends Command {
	private final GenealogyGraph graph;
	private final Marriage marriage;
	private final Rectangle box;
	
	public CreateMarriageCommand(GenealogyGraph g, Marriage m, Rectangle box) {
		super("Create Marriage");
		this.graph = g;
		this.marriage = m;
		this.box = box;
	}
	
	public void execute() {
		marriage.setLocation(box.x, box.y);
		marriage.setSize(box.width, box.height);
		graph.addMarriage(marriage);
	}
	
	public void undo() {
		graph.removeMarriage(marriage);
	}
}
