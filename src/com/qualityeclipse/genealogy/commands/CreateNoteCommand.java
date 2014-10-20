package com.qualityeclipse.genealogy.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.qualityeclipse.genealogy.model.*;

/**
 * Command to add a note to the genealogy graph
 */
public class CreateNoteCommand extends Command
{
	private final NoteContainer container;
	private final Note note;
	private final Rectangle box;

	public CreateNoteCommand(NoteContainer c, Note n, Rectangle box) {
		super("Create Note");
		this.container = c;
		this.note = n;
		this.box = box;
	}
	
	/**
	 * Add the note to the graph at the specified location
	 */
	public void execute() {
		if (box != null) {
			note.setLocation(box.x, box.y);
			note.setSize(box.width, box.height);
		}
		container.addNote(note);
	}
	
	/**
	 * Remove the note from the graph
	 */
	public void undo() {
		container.removeNote(note);
	}
}
