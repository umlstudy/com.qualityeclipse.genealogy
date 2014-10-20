package com.qualityeclipse.genealogy.commands;

import org.eclipse.gef.commands.Command;

import com.qualityeclipse.genealogy.model.Note;

/**
 * Command to delete a note from a note container
 */
public class RenameNoteCommand extends Command
{
	private final Note note;
	private final String oldValue;
	private final String newValue;

	public RenameNoteCommand(Note note, String newValue) {
		super("Rename Note");
		this.oldValue = note.getText();
		this.newValue = newValue;
		this.note = note;
	}
	
	/**
	 * Delete the note from the container
	 */
	public void execute() {
		note.setText(newValue);
	}
	
	/**
	 * Restore the note to the container
	 */
	public void undo() {
		note.setText(oldValue);
	}
}
