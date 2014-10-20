package com.qualityeclipse.genealogy.commands;

import org.eclipse.gef.commands.Command;

import com.qualityeclipse.genealogy.model.*;

/**
 * Command to delete a note from a note container
 */
public class DeleteNoteCommand extends Command
{
	private final NoteContainer container;
	private final Note note;
	private int index;

	public DeleteNoteCommand(NoteContainer container, Note note) {
		super("Delete Note");
		this.container = container;
		this.note = note;
	}
	
	/**
	 * Delete the note from the container
	 */
	public void execute() {
		index = container.getNotes().indexOf(note);
		container.removeNote(note);
	}
	
	/**
	 * Restore the note to the container
	 */
	public void undo() {
		container.addNote(index, note);
	}
}
