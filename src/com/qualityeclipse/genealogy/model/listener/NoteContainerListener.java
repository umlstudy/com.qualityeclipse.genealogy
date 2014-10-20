package com.qualityeclipse.genealogy.model.listener;

import com.qualityeclipse.genealogy.model.Note;
import com.qualityeclipse.genealogy.model.NoteContainer;

/**
 * Used by {@link NoteContainer} to notify others when changes occur.
 */
public interface NoteContainerListener
{
	void noteAdded(int index, Note n);
	void noteRemoved(Note n);
}
