package com.qualityeclipse.genealogy.commands;

import org.eclipse.gef.commands.Command;

import com.qualityeclipse.genealogy.model.*;

/**
 * Reorder a note within a {@link NoteContainer}.
 */
public class ReorderNoteCommand extends Command
{
	private final NoteContainer container;
	private final Note note;
	private int index;
	private int oldIndex;

	/**
	 * Instantiate a new command to reorder a note.
	 * 
	 * @param container the container in which the note will be reordered
	 * @param note the note to be reordered
	 */
	public ReorderNoteCommand(NoteContainer container, Note note) {
		super("Reorder Notes");
		this.container = container;
		this.note = note;
	}

	/**
	 * If the note is to be inserted after a particular note
	 * then call this method to specify the note before,
	 * otherwise the note will be reordered as the first note in the container.
	 * 
	 * @param afterNote the note after which the moved note will be inserted
	 */
	public void setAfterNote(Note afterNote) {
		index = container.getNotes().indexOf(afterNote) + 1;
	}
	
	/**
	 * Reorder the note in the container,
	 * while caching the old order.
	 */
	public void execute() {
		oldIndex = container.getNotes().indexOf(note);
		container.removeNote(note);
		container.addNote(index <= oldIndex ? index : index - 1, note);
	}
	
	/**
	 * Reorder the note back to the original position.
	 */
	public void undo() {
		container.removeNote(note);
		container.addNote(oldIndex <= index ? oldIndex : oldIndex - 1, note);
	}
}
