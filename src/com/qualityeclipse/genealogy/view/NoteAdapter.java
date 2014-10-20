package com.qualityeclipse.genealogy.view;

import com.qualityeclipse.genealogy.figures.NoteFigure;
import com.qualityeclipse.genealogy.model.GenealogyGraph;
import com.qualityeclipse.genealogy.model.Note;
import com.qualityeclipse.genealogy.model.listener.NoteListener;

/**
 * Creates and manages a {@link NoteFigure} to represent an associated {@link Note}
 * in a {@link GenealogyGraph}.
 */
public class NoteAdapter extends GenealogyElementAdapter
	implements NoteListener
{
	private final Note note;

	public NoteAdapter(Note note) {
		super(note, new NoteFigure(note.getText()));
		this.note = note;
		note.addNoteListener(this);
	}
	
	public NoteFigure getFigure() {
		return (NoteFigure) super.getFigure();
	}

	public void textChanged(String text) {
		getFigure().setText(text);
	}
	
	public void dispose() {
		note.removeNoteListener(this);
	}
}
